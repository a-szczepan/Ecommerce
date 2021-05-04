package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class OpinionController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def createOpinion: Action[AnyContent] = Action.async {
    Future{
      Ok("ok")
    }
  }

  def getOpinions: Action[AnyContent] = Action.async {
    Future{
      Ok("")
    }
  }

  def getOpinion(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def updateOpinion(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def deleteOpinion(id: Long): Action[AnyContent] = Action {
    NoContent
  }

}


