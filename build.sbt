lazy val bcryptVersion="0.4"
lazy val catsVersion = "0.9"
lazy val configVersion = "1.3.1"
lazy val elasticVersion = "6.2.10"
lazy val fs2Version = "0.10.2"
lazy val httpVersion = "0.18.1"
lazy val jsonVersion = "3.5.3"
lazy val jwtVersion = "0.12.1"
lazy val logbackVersion = "1.2.3"
lazy val loggingVersion = "3.7.2"
lazy val matryoshkaVersion = "0.18.3"
lazy val mongoVersion = "2.2.1"
lazy val pamVersion = "1.4"
lazy val pegdownVersion = "1.6.0"
lazy val shimsVersion = "1.1"
lazy val testVersion = "3.0.4"
lazy val ldapVersion = "4.0.6"

lazy val root = (project in file("."))
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
      "com.slamdata" %% "matryoshka-core" % matryoshkaVersion withSources(),
      "com.typesafe" % "config" % configVersion withSources(),
      "com.typesafe.scala-logging" %% "scala-logging" % loggingVersion
        withSources(),
      "org.http4s" %% "http4s-blaze-client" % httpVersion withSources(),
      "org.http4s" %% "http4s-blaze-server" % httpVersion withSources(),
      "org.http4s" %% "http4s-dsl" % httpVersion withSources(),
      "org.http4s" %% "http4s-json4s-native" % httpVersion withSources(),
      "org.json4s" %% "json4s-native" % jsonVersion withSources(),
      "org.jvnet.libpam4j" % "libpam4j" % pamVersion withSources(),
      "org.mindrot" % "jbcrypt" % bcryptVersion withSources(),
      "org.mongodb.scala" %% "mongo-scala-driver" % mongoVersion withSources(),
      "com.pauldijou" %% "jwt-json4s-native" % jwtVersion withSources(),
      "org.typelevel" %% "cats-effect" % catsVersion withSources(),
      "com.unboundid" % "unboundid-ldapsdk" % ldapVersion withSources()
		)
	)
