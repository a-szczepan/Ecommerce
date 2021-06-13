package controllers

import models.{Opinion, OpinionRepository, User, UserRepository, Product, ProductRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class OpinionFormController @Inject()(opinionRepository: OpinionRepository,
                                      userRepository: UserRepository,
                                      productRepository: ProductRepository,
                                      cc: MessagesControllerComponents)
                                     (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {
  var userList: Seq[User] = Seq[User]()
  var productList: Seq[Product] = Seq[Product]()

  userRepository.getAll().onComplete {
    case Success(user) => userList = user
    case Failure(e) => print("", e)
  }

  productRepository.list().onComplete {
    case Success(product) => productList = product
    case Failure(e) => print("", e)
  }

  val opinionForm: Form[CreateOpinionForm] = Form {
    mapping(
      "providerKey" -> nonEmptyText,
      "product_id" -> number,
      "opinion_txt" -> nonEmptyText,
    )(CreateOpinionForm.apply)(CreateOpinionForm.unapply)
  }

  val updateOpinionForm: Form[UpdateOpinionForm] = Form {
    mapping(
      "id" -> number,
      "providerKey" -> nonEmptyText,
      "product_id" -> number,
      "opinion_txt" -> nonEmptyText,
    )(UpdateOpinionForm.apply)(UpdateOpinionForm.unapply)
  }

  /* -------------------------------------------- */

  def updateOpinion(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val opinion = opinionRepository.getById(id)
    opinion.map(opinion => {
      val opinionForm = updateOpinionForm.fill(UpdateOpinionForm(opinion.get.id, opinion.get.providerKey, opinion.get.product_id, opinion.get.opinion_txt))
      Ok(views.html.opinion.updateOpinion(opinionForm, userList, productList))
    })
  }

  def updateOpinionHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateOpinionForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.opinion.updateOpinion(errorForm, userList, productList))
        )
      },
      opinion => {
        opinionRepository.update(opinion.id, Opinion(opinion.id, opinion.providerKey, opinion.product_id, opinion.opinion_txt)).map { _ =>
          Redirect("/opinion/all")
        }
      }
    )
  }

  def deleteOpinion(id: Int): Action[AnyContent] = Action {
    opinionRepository.delete(id)
    Redirect("/opinions")
  }

  def allOpinions: Action[AnyContent] = Action.async { implicit request =>
    opinionRepository.list().map(opinions => Ok(views.html.opinion.allOpinions(opinions)))
  }

  def getById(id: Int) = Action.async { implicit request =>
    val opinion = opinionRepository.getById(id)
    opinion.map(opinion => opinion match {
      case Some(p) => Ok(views.html.opinion.opinionById(p))
      case None => Redirect(routes.OpinionFormController.allOpinions)
    })
  }

  def createOpinion() = Action { implicit request =>
    val users = Await.result(userRepository.getAll(), Duration.Inf)
    val products = Await.result(productRepository.list(), Duration.Inf)
    Ok(views.html.opinion.createOpinion(opinionForm, users, products))
  }

  def createOpinionHandle(): Action[AnyContent] = Action.async { implicit request =>
    opinionForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.opinion.createOpinion(errorForm, userList, productList))
        )
      },
      opinion => {
        opinionRepository.create(opinion.providerKey, opinion.product_id, opinion.opinion_txt).map { _ =>
          Redirect("/opinions/all")
        }
      }
    )
  }

}

case class CreateOpinionForm(providerKey: String, product_id: Int, opinion_txt: String)

case class UpdateOpinionForm(id: Int = 0, providerKey: String, product_id: Int, opinion_txt: String)
