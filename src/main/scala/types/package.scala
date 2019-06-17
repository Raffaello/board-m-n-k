import scala.language.implicitConversions

package object types {
  object implicits {
    implicit def convertIntToShort(x: Int): Short = x.toShort
  }
}
