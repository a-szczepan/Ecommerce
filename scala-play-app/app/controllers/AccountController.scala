package controllers
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import models.{Account, AccountRepository, UserRepository}
import play.api.libs.json.{JsValue, Json}

@Singleton
class AccountController @Inject()(accountRepository: AccountRepository,userRepository: UserRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def createAccount: Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Account].map {
      account =>
        accountRepository.create(account.providerKey, account.firstName, account.lastName).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def getAccounts: Action[AnyContent] = Action.async {
    val accounts = accountRepository.list()
    accounts.map { accounts =>
      Ok(Json.toJson(accounts))
    }
  }

  def getAccount(id: Int): Action[AnyContent] = Action async {
    val account = accountRepository.getById(id)
    account.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def getAccountByUserKey(providerKey: String): Action[AnyContent] = Action async {
    val account = accountRepository.getByKey(providerKey)
    account.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updateAccount(id: Int):  Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Account].map {
      account =>
        accountRepository.update(account.id, account).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteAccount(id: Int): Action[AnyContent] = Action.async {
    accountRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

}


