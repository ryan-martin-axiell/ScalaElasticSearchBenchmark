// Settings for server sub project
lazy val catsVersion = "1.0.0"
lazy val configVersion = "1.3.1"
lazy val elasticVersion = "6.5.0"
lazy val fs2Version = "1.0.0"
lazy val httpVersion = "0.20.0-M4"
lazy val jsonVersion = "3.6.0"
lazy val logbackVersion = "1.2.3"
lazy val loggingVersion = "3.7.2"
lazy val pegdownVersion = "1.6.0"
lazy val shimsVersion = "1.1"

lazy val master = (project in file("."))
  .aggregate(benchmark)
  .dependsOn(benchmark)

lazy val benchmark = (project in file("benchmark"))
	.settings(
		name := "Scala ElasticSearch Benchmark",
		scalaVersion := "2.12.3",
		organization := "com.axiell",
		organizationName := "Axiell",

		// Compiler options
		scalacOptions ++= Seq(
			"-deprecation",
			"-encoding", "utf8",
			"-feature",
			"-target:jvm-1.8",
			"-unchecked",
			"-Xfatal-warnings",
			"-Xfuture",
			"-Xlint",
			"-Yno-adapted-args",
			"-Ypartial-unification",
			"-Ywarn-dead-code",
			"-Ywarn-numeric-widen",
			"-Ywarn-value-discard",
			"-Ywarn-unused-import"
		),

		fork := true,
    logBuffered in Test := false,
		name := "benchmark",

		// Dependency settings
    resolvers += Resolver.sonatypeRepo("releases"),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4"),
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion withSources(),
      "co.fs2" %% "fs2-core" % fs2Version withSources(),
      "com.codecommit" %% "shims" % shimsVersion withSources(),
      "com.sksamuel.elastic4s" %% "elastic4s-core" % elasticVersion
        withSources(),
      "com.sksamuel.elastic4s" %% "elastic4s-http" % elasticVersion
        withSources(),
      "com.typesafe" % "config" % configVersion withSources(),
      "com.typesafe.scala-logging" %% "scala-logging" % loggingVersion
        withSources(),
      "org.http4s" %% "http4s-blaze-client" % httpVersion withSources(),
      "org.http4s" %% "http4s-blaze-server" % httpVersion withSources(),
      "org.http4s" %% "http4s-dsl" % httpVersion withSources(),
      "org.http4s" %% "http4s-json4s-native" % httpVersion withSources(),
      "org.json4s" %% "json4s-native" % jsonVersion withSources(),
      "org.typelevel" %% "cats-effect" % catsVersion withSources()
		)
	)
