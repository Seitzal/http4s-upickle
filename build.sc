import mill._
import mill.scalalib._
import mill.scalalib.publish._

object interop extends ScalaModule with PublishModule {
  
  def artifactName = "http4s-upickle"

  def publishVersion = "0.2.1"

  def pomSettings = PomSettings(
    description = "Interoperability module for http4s and uPickle",
    organization = "eu.seitzal",
    url = "https://github.com/seitzal/http4s-upickle",
    versionControl = VersionControl.github("seitzal", "http4s-upickle"),
    licenses = Seq(License.MIT),
    developers = Seq(
      Developer("seitzal", "Alex Seitz","https://github.com/Seitzal")
    )
  )

  def scalaVersion = "2.13.6"

  def ivyDeps = Agg(
    ivy"org.http4s::http4s-core:0.21.25",
    ivy"com.lihaoyi::upickle:1.4.0"
  )

  def scalacOptions = Seq("-deprecation")

  object test extends Tests {
    def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.1.1",
      ivy"org.http4s::http4s-dsl:0.21.25")
    def testFrameworks = Seq("org.scalatest.tools.Framework")
  }
}
