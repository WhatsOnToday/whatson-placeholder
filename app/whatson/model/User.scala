package whatson.model

import play.api.libs.json._

case class User(id: Option[Int], email: String)

object User {
  implicit val userReads = Json.reads[User]

  implicit val userWrites = Json.writes[User]

  val tupled = (this.apply _).tupled
}
