package com.axiell.benchmark

import cats.effect._

import com.sksamuel.elastic4s.http.{ElasticClient, ElasticProperties}
import com.sksamuel.elastic4s.http.ElasticDsl._

object Main extends IOApp {
	def run(args: List[String]): IO[ExitCode] = {
		val client = ElasticClient(
      ElasticProperties("http://localhost:9200?cluster.name=elasticsearch")
    )

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
		}
	}
}
