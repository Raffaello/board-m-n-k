package settings

import com.typesafe.config.{Config, ConfigFactory}
import cakes.game.Implicit.convertToPlayer
import cakes.game.Player

object Loader {
  lazy val config: Config = {
    val config: Config = ConfigFactory.load()
    config.checkValid(ConfigFactory.defaultReference())
    config
  }

  object Ai {
    lazy val config: Config = Loader.config.getConfig("ai")
    lazy val player: Player = config.getInt("player")

    object Mcts {
      lazy val config: Config = Ai.config.getConfig("mcts")
      lazy val uctParameter: Double = config.getDouble("uct.c")
      lazy val maxIter: Int = config.getInt("max_iter")
      lazy val seed: Option[Long] =  if (config.getIsNull("seed")) None else Some(config.getLong("seed"))
    }
  }
}
