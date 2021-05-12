package controllers
import models.{Shipping, ShippingRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class ShippingFormController @Inject()(shippingRepository: ShippingRepository,
                                      cc: MessagesControllerComponents)
                                     (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {


}
case class CreateShippingForm(street_name: String, building_number: Int, apartment_number: Int, postal_code: String, city: String)

case class UpdateShippingForm(id: Int = 0, street_name: String, building_number: Int, apartment_number: Int, postal_code: String, city: String)