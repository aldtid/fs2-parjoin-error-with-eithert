lazy val root = (project in file("."))
  .settings(
    organization         := "com.example",
    name                 := "root",
    scalaVersion         := "2.12.10",
    version              := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(dependencies.cats.core,
                                dependencies.cats.effect,
                                dependencies.fs2.core,
                                dependencies.test.core)
  )

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
