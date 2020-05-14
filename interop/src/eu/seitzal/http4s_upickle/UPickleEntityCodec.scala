package eu.seitzal.http4s_upickle

import org.http4s._
import upickle.default._
import cats._
import cats.implicits._

/**
 * Generic [[org.http4s.EntityDecoder]] and [[org.http4s.EntityEncoder]] for any
 * type for which an implicit [[upickle.default.ReadWriter]] is in scope.
 * @tparam F The monadic type which is used for wrapping requests/responses.
 *           Usually, this will be [[cats.effect.IO]].
 * @tparam A The type of resource which this codec is supposed to read/write.
 */
final class UPickleEntityCodec[F[_], A: ReadWriter]
    (implicit me: MonadError[F, Throwable], se: EntityDecoder[F, String])
    extends EntityEncoder[F, A] with EntityDecoder[F, A] {

  private val ee = new UPickleEntityEncoder[F, A]
  private val ed = new UPickleEntityDecoder[F, A]

  def toEntity(a: A): Entity[F] =
    ee.toEntity(a)

  def headers: Headers =
    ee.headers

  def decode(m: Media[F], strict: Boolean): DecodeResult[F, A] =
    ed.decode(m, strict)

  def consumes: Set[MediaRange] =
    ed.consumes

}
