import sbt._
import Keys._

object ReactDynamoProject extends Build
{
  import Resolvers._
  
  val projectName = "ReactDynamo"
  
  lazy val root = Project(projectName, file(".")) settings(coreSettings : _*)

  lazy val commonSettings: Seq[Setting[_]] = Seq(
    organization := "io.react2",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.10.3",
    resolvers ++= Seq(akkaRelease, akkaSnapshot)
  )

  val akkaVersion = "2.2.0"
    
  lazy val coreSettings = commonSettings ++ Seq(
    name := projectName,
    libraryDependencies :=
        Seq(
		  "org.specs2"        %% "specs2"                    % "2.3.10"      % "test",
		  "com.typesafe"      %  "config"                    % "1.2.0",
		  "com.typesafe.akka" %% "akka-actor"                % akkaVersion,
		  "com.typesafe.akka" %% "akka-testkit"				 % akkaVersion   % "test",
		  "com.amazonaws"     % "aws-java-sdk" 				 % "1.8.4"
        ),
    parallelExecution in Test := false,
    publishTo := Some("Artifactory Realm" at "http://4be8e2a8.ngrok.com/artifactory/libs-snapshot-local"),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    publishMavenStyle := true,
    publishArtifact in Test := false
  )
  
}

object Resolvers {
  val akkaRelease = "typesafe release repo" at "http://repo.typesafe.com/typesafe/releases/"
  val akkaSnapshot = "typesafe snapshot repo" at "http://repo.typesafe.com/typesafe/snapshots/"
}