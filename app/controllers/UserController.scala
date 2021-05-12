package controllers
import models.{User, AccountRepository, UserRepository}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(val userRepository: UserRepository,
                                  val accountRepository: AccountRepository,
                                  cc: ControllerComponents)
                                 (implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def createUser: Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[User].map {
        user =>
          userRepository.create(user.account_id, user.email, user.password).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }

  def getUsers: Action[AnyContent] = Action.async {
    val users = userRepository.list()
    users.map{
      users => Ok(Json.toJson(users))
    }
  }

  def getUser(id: Int): Action[AnyContent] = Action.async {
    val user = userRepository.getById(id)
    user.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def getUserByAccount(account_id: Int): Action[AnyContent] = Action.async{
    val users = userRepository.getByAccount(account_id)
    users.map {
      users => Ok(Json.toJson(users))
    }
  }

  def updateUser(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[User].map {
      user =>
        userRepository.update(user.id, user).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteUser(id: Int): Action[AnyContent] = Action.async {
    userRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }
}


