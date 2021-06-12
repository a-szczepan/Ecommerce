package controllers
import play.api.mvc._
import models.{ Opinion, OpinionRepository, UserRepository, ProductRepository}
import play.api.libs.json.{JsValue, Json}
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class OpinionController @Inject()(opinionRepository: OpinionRepository, userRepository: UserRepository, productRepository: ProductRepository,cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def createOpinion: Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Opinion].map {
        opinion =>
          opinionRepository.create(opinion.providerKey, opinion.product_id, opinion.opinion_txt).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }

  def getOpinions: Action[AnyContent] = Action.async {
    val opinions = opinionRepository.list()
    opinions.map{
      opinions => Ok(Json.toJson(opinions))
    }
  }

  def getOpinion(id: Int): Action[AnyContent] = Action.async {
    val opinion = opinionRepository.getById(id)
    opinion.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updateOpinion(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Opinion].map {
      opinion =>
        opinionRepository.update(opinion.id, opinion).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteOpinion(id: Int): Action[AnyContent] = Action.async {
    opinionRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getOpinionByUserKey(providerKey:String): Action[AnyContent] = Action.async{
    val opinions = opinionRepository.getByUserKey(providerKey)
    opinions.map {
      opinions => Ok(Json.toJson(opinions))
    }
  }

  def getOpinionByProduct(product_id:Int): Action[AnyContent] = Action.async{
    val opinions = opinionRepository.getByProduct(product_id)
    opinions.map {
      opinions => Ok(Json.toJson(opinions))
    }
  }

}


