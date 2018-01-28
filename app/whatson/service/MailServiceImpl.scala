package whatson.service

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import whatson.db._
import whatson.model._
import javax.inject._
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.libs.mailer._
import views.html._
import play.api._
import whatson.util._

class MailServiceImpl @Inject()(mailerClient: MailerClient,
                                config: Configuration,
                                applicationConfig: ApplicationConfig)(implicit context: ExecutionContext)
    extends MailService {

  def sendConfirmation(userMail: String) = {
    val email = Email(
      "Confirm your email address",
      "Whats On <no-reply@whats-on.today>",
      Seq(userMail),
      bodyHtml = Some(new UserConfirmation(userMail,applicationConfig.url)().toString())
    )

    if(applicationConfig.confirmationMails)
      Future.successful(mailerClient.send(email))
    else
      Future.successful(())
  }
}
