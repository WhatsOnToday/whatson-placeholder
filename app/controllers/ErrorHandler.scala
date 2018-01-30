package controllers

import scala.concurrent.ExecutionContext
import play.api.http.HttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent._
import javax.inject.Singleton
import javax.inject._
import play.api.http.FileMimeTypes


@Singleton
class ErrorHandler @Inject()(implicit context: ExecutionContext, val fileMimeTypes: FileMimeTypes)
    extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    if(statusCode==404)
      Future.successful(NotFound.sendFile(new java.io.File("./public/not_found.html")))
    else
      Future.successful(
        Status(statusCode)("A client error occurred: " + message)
      )
  }

  def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful(
      InternalServerError("A server error occurred: " + exception.getMessage)
    )
  }
}
