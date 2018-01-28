package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
mport javax.inject._
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import java.sql.Timestamp
import java.time.LocalDateTime
import play.api._
import slick.jdbc.DB2Profile

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends PlaySpec
  with GuiceOneAppPerTest with Injecting {

  val log = Logger("api.events")

  "HomeController GET" should {

    "render the index page from a new instance of controller" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
    }
  }
}
