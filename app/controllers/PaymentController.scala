package controllers

import play.api.mvc._
import models.{Payment, PaymentRepository, OrderRepository}
import play.api.libs.json.{JsValue, Json}
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class PaymentController @Inject()(paymentRepository: PaymentRepository,
                                  orderRepository: OrderRepository,
                                  cc: ControllerComponents)
                                 (implicit ec: ExecutionContext)
  extends AbstractController(cc){

  def createPayment: Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Payment].map {
        payment =>
          paymentRepository.create(payment.order_id, payment.date, payment.amount).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }

  def getPayments: Action[AnyContent] = Action.async {
    val payments = paymentRepository.list()
    payments.map{
      payment => Ok(Json.toJson(payment))
    }
  }

  def getPayment(id: Int): Action[AnyContent] = Action.async {
    val payment = paymentRepository.getById(id)
    payment.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def deletePayment(id: Int): Action[AnyContent] = Action.async {
    paymentRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getPaymentByOrder(order_id: Int): Action[AnyContent] = Action.async{
    val payments = paymentRepository.getByOrder(order_id)
    payments.map {
      products => Ok(Json.toJson(products))
    }
  }

}


