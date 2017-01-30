name := "shopping-basket"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest"           % "scalatest_2.11"          % "3.0.1"   % "test",
  "org.scalatestplus.play"  %% "scalatestplus-play"     % "1.5.0"   % "test",
  "com.typesafe.akka"       %% "akka-testkit"           % "2.4.14"  % "test"
)



lazy val root = (project in file(".")).enablePlugins(PlayScala)


