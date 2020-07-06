import sbt._


object dependencies {

  lazy val cats = new {
    val core: ModuleID = "org.typelevel" %% "cats-core" % "2.1.1"
    val effect: ModuleID = "org.typelevel" %% "cats-effect" % "2.1.3"
  }

  lazy val fs2 = new {
    val core: ModuleID = "co.fs2" %% "fs2-core" % "2.4.2"
  }

  lazy val test = new  {
    val core: ModuleID = "org.scalatest" %% "scalatest" % "3.1.1" % Test
  }
  
}
