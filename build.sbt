name := """RequestProducer"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"      % "2.3.14",
  "com.typesafe.akka" %% "akka-testkit"    % "2.3.14" % "test",
  "org.scalatest"     %% "scalatest"       % "2.2.4" % "test",
  "com.typesafe.akka" %% "akka-slf4j"      % "2.3.6",
  "com.typesafe.akka" %% "akka-testkit"    % "2.3.6",
  "ch.qos.logback"    %  "logback-classic" % "1.1.2"
)