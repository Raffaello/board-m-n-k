package settings

import com.typesafe.config.{Config, ConfigFactory}

object Loader {
  val config: Config = {
    val config: Config = ConfigFactory.load()
    config.checkValid(ConfigFactory.defaultReference())
    config
  }
}
