package controllers

import scala.concurrent.ExecutionContext
import scala.concurrent._
import javax.inject._
import play.api._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import whatson.model.UserSignUpForm._
import whatson.model._
import play.api.libs.json._
import whatson.util.FormErrorJson._
import whatson.service._
import whatson.db._
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import scala.util._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               protected val dbConfigProvider: DatabaseConfigProvider,
                               mailService: MailService)
                            (implicit context: ExecutionContext) extends AbstractController(cc)
  with HasDatabaseConfigProvider[JdbcProfile] {

  val log = Logger("rest")

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    log.debug("Rest request for the main page")
    Ok.sendFile(new java.io.File("./public/index.html"))
  }

  /**
   * Handles the submitted JSON data.
   *
   * @return The result to display.
   */
  def signUp = Action.async(parse.json) { implicit request =>
    log.debug("Sign up request")
    UserSignUpForm.form.bindFromRequest.fold(
      form => {
        Future.successful(BadRequest(Json.toJson(form.errors)))
      },
      data => {
        val userInfo = User(None, data.email)

        db.run(UserTable.user.filter(x => x.email === data.email).result).map(_.headOption).flatMap {
          case Some(u) => Future.successful(Conflict(Json.obj("message" -> "user.exists")))
          case None => {
            db.run(UserTable.user += (userInfo))
              .flatMap(x => mailService.sendConfirmation(data.email)) transformWith {
                case Success(_) => Future.successful(Ok(Json.obj("message" -> "mail.sent")))
                case Failure(_) => {
                  db.run(UserTable.user.filter(x => x.email === data.email).delete)
                    .map(x => BadRequest(Json.obj("email" -> "error.email")))
                }
              }
          }
        }
      })
  }

  /**
    * Returns the current count of sign ups
    */
  def signUpCount = Action.async { implicit request =>
    log.debug("Sign up count request")
    db.run(UserTable.user.length.result).map { x =>
      Ok(Json.obj("count" -> x))
    }
  }
}
