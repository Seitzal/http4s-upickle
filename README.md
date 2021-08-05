# http4s-upickle
Interoperability module for [http4s](https://github.com/http4s/http4s) and [uPickle](https://github.com/lihaoyi/upickle).

## Installation

Artifacts are currently not published anywhere.
To build your own, you need to have [Mill](https://github.com/lihaoyi/mill) installed.
You can then run the following to build the module and publish it to your local ivy repository:

```
git clone https://github.com/Seitzal/http4s-upickle
cd http4s-upickle
mill interop.publishLocal
```

Afterwards, you can set a dependency in your own project:

```scala
ivy"eu.seitzal::http4s-upickle:0.2.2"      // Mill
"eu.seitzal" %% "http4s-upickle" % "0.2.2" // SBT
```

## Usage

Use the following import to bring the implicits into scope:

```scala
import eu.seitzal.http4s_upickle._
```

You can then use any types that are readable/writable by uPickle as if they were internally supported by http4s.
Example:

```scala
case class Entry(text: String, done: Boolean, parent: Option[Int])

object Entry {
  implicit val rw = upickle.default.macroRW[Entry]
}

def readEntry(rq: Request[IO]): IO[Entry] = 
  rq.as[Entry] // to read

def writeEntry(entry: Entry): IO[Response[IO]] =
  Ok(entry) // to write
```
