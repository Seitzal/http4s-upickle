# http4s-upickle
Interoperability module for [http4s](https://github.com/http4s/http4s) and [uPickle](https://github.com/lihaoyi/upickle).

## Installation

Declare an ivy dependency in your build tool or REPL:

```scala
"eu.seitzal" %% "http4s-upickle" % "0.2.1" // SBT
ivy"eu.seitzal::http4s-upickle:0.2.1"      // Mill
$ivy.`eu.seitzal::http4s-upickle:0.2.1`    // Ammonite
```

## Usage

Use the following wildcard import to bring implicit encoders and decoders into scope for all compatible types:

```scala
import eu.seitzal.http4s_upickle._
```

You can then use any types that are readable/writable by uPickle as if they were internally supported by http4s.
For example:

```scala
import eu.seitzal.http4s_upickle._
import org.http4s._
import org.http4s.dsl.io._
import cats.effect.IO

case class Entry(text: String, done: Boolean, priority: Option[Int])

object Entry {
  implicit val rw = upickle.default.macroRW[Entry]
}

def readEntry(rq: Request[IO]): IO[Entry] = 
  rq.as[Entry] // to read

def writeEntry(entry: Entry): IO[Response[IO]] =
  Ok(entry) // to write
```

### Usage for specific types only

Currently, using the wildcard import overrides http4s's built-in decoders and encoders for uPickle-compatible types, such as strings. This means that things such as string responses will receive "Content-type: application/json" headers. To avoid this behaviour and only use the module for specific types, such as your own case classes, you can define your own implicit codec rather than using the wildcard.
Here's an example for the above-used Entry case class:

```scala
import eu.seitzal.http4s_upickle.UPickleEntityCodec
import cats.effect.IO

case class Entry(text: String, done: Boolean, priority: Option[Int])

object Entry {
  // Defining these in the companion object causes them to always be in implicit scope
  implicit val rw = upickle.default.macroRW[Entry]
  implicit val ec = new UPickleEntityCodec[IO, Entry] // Combined EntityEncoder and -Decoder
}
```
