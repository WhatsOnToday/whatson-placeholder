package whatson.db

import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape.proveShapeOf
import whatson.model._

class UserTable(tag: Tag) extends Table[User](tag, "email") {
  def id = column[Int]("id",O.PrimaryKey,O.AutoInc)

  def email = column[String]("email")

  def * = (id.?,email) <> (User.tupled, User.unapply)
}

object UserTable {
  val user = TableQuery[UserTable]
}
