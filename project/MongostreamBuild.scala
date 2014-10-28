import sbt._
import sbt.Keys._

object MongostreamBuild extends Build {

  lazy val mongostream = Project(
    id = "mongostream",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "mongoStream",
      organization := "es.care.sf",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.2"
      // add other settings here
    )
  )
}
