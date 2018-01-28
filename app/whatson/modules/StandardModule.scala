package whatson.modules

import scala.collection.immutable._

import com.google.inject.{AbstractModule, Provides}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import net.codingwell.scalaguice.ScalaModule
import play.api.Configuration
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.DefaultCookieHeaderEncoding
import whatson.service._
import whatson.util._

class StandardModule extends AbstractModule with ScalaModule {
  override def configure() = {
    bind[MailService].to[MailServiceImpl]
  }
  /**
    * Provides the ApplicationConfig.
    */
  @Provides
  def provideApplicationConfig(
    configuration: Configuration): ApplicationConfig = {
    configuration.underlying.as[ApplicationConfig]("application")
  }
}
