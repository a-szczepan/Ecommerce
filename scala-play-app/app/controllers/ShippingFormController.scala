package controllers

import models.{Shipping, ShippingRepository, User, UserRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class ShippingFormController @Inject()(shippingRepository: ShippingRepository,userRepository: UserRepository,
                                       cc: MessagesControllerComponents)
                                      (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  var userList: Seq[User] = Seq[User]()

  userRepository.getAll().onComplete {
    case Success(user) => userList = user
    case Failure(e) => print("", e)
  }

  val shippingForm: Form[CreateShippingForm] = Form {
    mapping(
      "providerKey" -> nonEmptyText,
      "street_name" -> nonEmptyText,
      "building_number" -> nonEmptyText,
      "postal_code" -> nonEmptyText,
      "city" -> nonEmptyText,
    )(CreateShippingForm.apply)(CreateShippingForm.unapply)
  }

  val updateShippingForm: Form[UpdateShippingForm] = Form {
    mapping(
      "id" -> number,
      "providerKey" -> nonEmptyText,
      "street_name" -> nonEmptyText,
      "building_number" -> nonEmptyText,
      "postal_code" -> nonEmptyText,
      "city" -> nonEmptyText,
    )(UpdateShippingForm.apply)(UpdateShippingForm.unapply)
  }

  def updateShipping(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val shipping = shippingRepository.getById(id)
    shipping.map(shipping => {
      val shippingForm = updateShippingForm.fill(UpdateShippingForm(shipping.get.id,shipping.get.providerKey, shipping.get.street_name, shipping.get.building_number, shipping.get.postal_code, shipping.get.city))
      Ok(views.html.shipping.updateShipping(shippingForm))
    })
  }

  def updateShippingHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateShippingForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.shipping.updateShipping(errorForm))
        )
      },
      shipping => {
        shippingRepository.update(shipping.id, Shipping(shipping.id, shipping.providerKey, shipping.street_name, shipping.building_number, shipping.postal_code, shipping.city)).map { _ =>
          Redirect("/shipping")
        }
      }
    )
  }

  def deleteShipping(id: Int): Action[AnyContent] = Action {
    shippingRepository.delete(id)
    Redirect("/shipping")
  }

  def allShippings: Action[AnyContent] = Action.async { implicit request =>
    shippingRepository.list().map(shippings => Ok(views.html.shipping.allShippings(shippings)))
  }

  def getById(id: Int) = Action.async { implicit request =>
    val shipping = shippingRepository.getById(id)
    shipping.map(shipping => shipping match {
      case Some(p) => Ok(views.html.shipping.shippingById(p))
      case None => Redirect(routes.ShippingFormController.allShippings)
    })
  }

  def createShipping(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val shippings = shippingRepository.list()
    shippings.map(_ => Ok(views.html.shipping.createShipping(shippingForm)))
  }

  def createShippingHandle(): Action[AnyContent] = Action.async { implicit request =>
    shippingForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.shipping.createShipping(errorForm))
        )
      },
      shipping => {
        shippingRepository.create(shipping.providerKey, shipping.street_name, shipping.building_number, shipping.postal_code, shipping.city).map { _ =>
          Redirect("/shipping/all")
        }
      }
    )
  }
}

case class CreateShippingForm(providerKey: String, street_name: String, building_number: String, postal_code: String, city: String)

case class UpdateShippingForm(id: Int = 0, providerKey: String, street_name: String, building_number: String, postal_code: String, city: String)