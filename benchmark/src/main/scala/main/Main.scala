package com.axiell.benchmark

import cats.effect._
import cats.effect.concurrent.Deferred
import cats.implicits._

import com.sksamuel.elastic4s.http._
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.http.search.SearchResponse
import com.sksamuel.elastic4s.searches.SearchRequest
import com.sksamuel.elastic4s.searches.sort.FieldSort
import com.sksamuel.elastic4s.searches.sort.SortOrder.Asc

import fs2._
import fs2.concurrent.Queue

import scala.language.higherKinds

object Main extends IOApp {
  class ElasticStream[T[_]](d: Deferred[T, Either[Throwable,Unit]]) {
    val client = ElasticClient(
      ElasticProperties("http://localhost:9200?cluster.name=elasticsearch")
    )

    val baseQuery = searchWithType("nhm-eregistry" -> "_doc")
                      .matchAllQuery
                      .size(5000)
                      .sortBy(
                        FieldSort("id.keyword").order(Asc)
                      )
                      .sourceInclude("id")

    def request(search: SearchRequest)(implicit F: Concurrent[T])
        : T[Response[SearchResponse]] = {
      IO.fromFuture(IO(client.execute(search))).to[T]
    }

    def nextQuery(sort: Option[Seq[AnyRef]]): SearchRequest = sort match {
      case Some(s) => baseQuery.searchAfter(s)
      case None => baseQuery
    }

    def data
        (cb: Response[SearchResponse] => Unit, sort: Option[Seq[AnyRef]] = None)
        (implicit F: Concurrent[T]): T[Unit] = {
      request(nextQuery(sort))
        .flatMap {
          case f @ RequestFailure(_, _, _, _) =>
            F.delay(cb(f))
          case f @ RequestSuccess(_, _, _, result) =>
            F.delay(cb(f))
              .flatMap { _ =>
                if (result.hits.hits.nonEmpty) {
                  data(cb, result.hits.hits.last.sort)
                } else {
                  d.complete(Right(()))
                }
              }
        }
    }
  }

  def records[T[_]](es: ElasticStream[T])(implicit F: ConcurrentEffect[T])
      : Stream[T, Response[SearchResponse]] = {
    for {
      q <- Stream.eval(Queue.unbounded[T, Response[SearchResponse]])
      _ <- Stream.eval {
        es.data(r => F.runAsync(q.enqueue1(r))(_ => IO.unit).unsafeRunSync)
      }
      row <- q.dequeue
    } yield row
  }

	def run(args: List[String]): IO[ExitCode] = {
    for {
         start <- IO(System.nanoTime)
      deferred <- Deferred[IO, Either[Throwable,Unit]]
        client =  new ElasticStream[IO](deferred)
        stream <- records[IO](client)
                    .interruptWhen(deferred)
                    .compile.drain.map { _ =>
                      client.client.close()
                      ExitCode.Success
                    }
           end <- IO(System.nanoTime)
       elapsed =  (end - start).toDouble
             _ =  println(s"Took ${elapsed/1000000000.0}s")
    } yield stream
	}
}
