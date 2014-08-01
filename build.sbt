name := """zilean"""

version := "1.0"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.4",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.4",
  "com.typesafe" % "config" % "1.2.1",
  "joda-time" % "joda-time" % "2.3",
  "org.json4s" %% "json4s-jackson" % "3.2.9",
  "com.amazonaws" % "aws-java-sdk" % "1.8.6",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test",
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.10" % "test"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

