name := "shopping-basket"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest"           % "scalatest_2.11"          % "3.0.1"   % Test,
  "org.scalatestplus.play"  %% "scalatestplus-play"     % "2.0.0-M1"   % Test,
  "com.typesafe.akka"       %% "akka-testkit"           % "2.4.14"  % Test,
  "org.mockito"             %  "mockito-all"            % "1.10.19"  % Test)



lazy val root = (project in file(".")).enablePlugins(PlayScala)


