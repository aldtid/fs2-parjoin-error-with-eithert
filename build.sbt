import sbt.ModuleID


val compilerOptions: Seq[String] =
  Seq(
    "-language:higherKinds",
    "-Ypartial-unification",
    "-feature",
    "-unchecked"
  )

val libDependencies: Seq[ModuleID] =
  Seq(
    dependencies.cats.core,
    dependencies.cats.effect,
    dependencies.fs2.core,
    dependencies.test.core
  )

lazy val root = (project in file("."))
  .settings(
    organization         := "com.example",
    name                 := "root",
    scalaVersion         := "2.12.10",
    version              := "0.1.0-SNAPSHOT",
    scalacOptions       ++= compilerOptions,
    libraryDependencies ++= libDependencies
  )

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
