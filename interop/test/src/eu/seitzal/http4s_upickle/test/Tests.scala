package eu.seitzal.http4s_upickle.test

import org.scalatest._
import org.scalatest.funsuite.AnyFunSuite

import upickle.default._
import eu.seitzal.http4s_upickle._
import org.http4s._
import org.http4s.dsl.io._
import cats.effect.IO
import fs2.Stream

final class Tests extends AnyFunSuite {

  case class Entry(text: String, done: Boolean, priority: Option[Int])
  case class Todo(entries: List[Entry])

  implicit val entryRW = macroRW[Entry]
  implicit val todoRW = macroRW[Todo]

  val myTodo = Todo(List(
    Entry("Buy penguin", false, Some(1)),
    Entry("Learn to play bagpipes", false, Some(10)),
    Entry("Start project", true, None),
    Entry("Finish project", false, None)
  ))

  val myTodoJson = """{"entries":[{"text":"Buy penguin","done":false,"priority":[1]},{"text":"Learn to play bagpipes","done":false,"priority":[10]},{"text":"Start project","done":true,"priority":[]},{"text":"Finish project","done":false,"priority":[]}]}"""

  test("Writing json response body") {
    val response: IO[Response[IO]] = Ok(myTodo)
    val responseBodyText = new String(
      response
        .unsafeRunSync
        .body
        .compile
        .toVector
        .unsafeRunSync
        .toArray
      )
    assert(responseBodyText == myTodoJson)
  }

  test("Reading json request body") {
    val request: Request[IO] = 
      new Request(body = Stream.emits(myTodoJson.getBytes))
    assert(request.as[Todo].unsafeRunSync == myTodo)
  }

  test("Read failure: Invalid json") {
    val request: Request[IO] = 
      new Request(body = Stream.emits("THIS_IS_NOT_VALID_JSON".getBytes))
    assertThrows[InvalidMessageBodyFailure] {
      request.as[Todo].unsafeRunSync
    }
  }

  test("Read failure: Malformed json") {
    val request: Request[IO] = 
      new Request(body = Stream.emits(myTodoJson.getBytes))
    assertThrows[MalformedMessageBodyFailure] {
      request.as[Entry].unsafeRunSync
    }
  }

}