package com.axiell.benchmark

// import cats._
import cats.data._
import cats.effect._
// import cats.syntax.all._
import cats.implicits._

// import com.sksamuel.elastic4s.http.{ElasticClient,ElasticProperties}
// import com.sksamuel.elastic4s.http.ElasticDsl._

object Main extends IOApp {
	def run(args: Array[String]): IO[ExitCode] = {

		/*
		val client = ElasticClient(ElasticProperties("http://localhost:9200?cluster.name=elasticsearch"))

		IO.fromFuture {
			IO {
				client.execute {
					searchWithType("nhm-eparties" -> "_doc")
						.matchAllQuery
						.size(10)
				}
			}
		}.map { f =>
			println(f)
			ExitCode.Success
		}*/

		IO(println("hello")).as(ExitCode.Success)
	}
}
