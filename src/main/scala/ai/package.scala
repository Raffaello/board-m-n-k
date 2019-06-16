import com.typesafe.config.Config
import com.typesafe.scalalogging.Logger
import game.Implicit.convertToPlayer
import game.Player

package object ai {
  private[ai] val logger = Logger("ai")
  val config: Config = settings.Loader.config.getConfig("ai")
  lazy val aiPlayer: Player = config.getInt("player")
}
