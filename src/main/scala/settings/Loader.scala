package settings

import com.typesafe.config.{Config, ConfigFactory}

object Loader {

  def getConfig(): Config = {
    val config: Config = ConfigFactory.load()
    config.checkValid(ConfigFactory.defaultReference())
    config
  }
}
