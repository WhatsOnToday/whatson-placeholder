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

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, protected val dbConfigProvider: DatabaseConfigProvider)(implicit context: ExecutionContext) extends AbstractController(cc)
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
    UserSignUpForm.form.bindFromRequest.fold(
      form => {
        Future.successful(BadRequest(Json.toJson(form.errors)))
      },
      data => {
        val userInfo = User(None, data.email)

        Future(Ok("yay"))
        /*loginService.retrieve(loginInfo).flatMap {
          case Some(login) =>
            Future.successful(BadRequest(Json.obj("message" -> "user.exists")))
          case None =>
            val authInfo = passwordHasher.hash(data.password)
            val login = Login(None, data.email, None, None, None, loginInfo.providerID, loginInfo.providerKey, false)
            for {
              login <- loginService.save(login)
              authInfo <- authInfoRepository.add(loginInfo, authInfo)
              authenticator <- silhouette.env.authenticatorService.create(loginInfo)
              token <- silhouette.env.authenticatorService.init(authenticator)
              avatar <- avatarService.retrieveURL(data.email)
            } yield {
              silhouette.env.eventBus.publish(SignUpEvent(login, request))
              silhouette.env.eventBus.publish(LoginEvent(login, request))
              userService.save(login,avatar)
              mailService.sendUserConfirmation(data.email,token)
              Ok(Json.obj("message" -> "mail.sent"))
            }
        }*/
      })
  }
}
