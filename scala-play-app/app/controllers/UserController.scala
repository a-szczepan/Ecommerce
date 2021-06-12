package controllers
import models.{User,  UserRepository}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(val userRepository: UserRepository,
                               cc: ControllerComponents)
                              (implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def getUserByKey(key: String): Action[AnyContent] = Action.async {
    val user = userRepository.getByKey(key)
    user.map {
      user => Ok(Json.toJson(user))
    }
  }
}