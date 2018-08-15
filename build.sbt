scalaVersion := "2.12.6"

name := "Alpakka Cassandra Sample Project"

libraryDependencies += "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "0.20"
libraryDependencies += "com.lightbend.akka" %% "akka-stream-alpakka-file" % "0.20"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.14"

fork in run := true

