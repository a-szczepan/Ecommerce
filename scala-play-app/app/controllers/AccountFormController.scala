package controllers
import models.{Account,AccountRepository, User, UserRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration.Duration

@Singleton
class AccountFormController @Inject()(accountRepository: AccountRepository, userRepository: UserRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){
  var userList: Seq[User] = Seq[User]()

  userRepository.getAll().onComplete {
    case Success(user) => userList = user
    case Failure(e) => print("", e)
  }


  val accountForm: Form[CreateAccountForm] = Form {
    mapping(
      "providerKey" -> nonEmptyText,
      "first_name" -> nonEmptyText,
      "last_name" -> nonEmptyText,
    )(CreateAccountForm.apply)(CreateAccountForm.unapply)
  }

  val updateAccountForm: Form[UpdateAccountForm] = Form {
    mapping(
      "id" -> number,
      "providerKey" -> nonEmptyText,
      "first_name" -> nonEmptyText,
      "last_name" -> nonEmptyText,
    )(UpdateAccountForm.apply)(UpdateAccountForm.unapply)
  }

  def updateAccount(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val account = accountRepository.getById(id)
    account.map(account => {
      val accountForm = updateAccountForm.fill(UpdateAccountForm(account.get.id, account.get.providerKey, account.get.first_name, account.get.last_name))
      Ok(views.html.account.updateAccount(accountForm, userList))
    })
  }

  def updateAccountHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateAccountForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.account.updateAccount(errorForm, userList))
        )
      },
      account => {
        accountRepository.update(account.id, Account(account.id, account.providerKey, account.first_name, account.last_name)).map { _ =>
          Redirect("/accounts/all")
        }
      }
    )
  }

  def deleteAccount(id: Int): Action[AnyContent] = Action {
    accountRepository.delete(id)
    Redirect("/accounts/all")
  }

  def allAccounts: Action[AnyContent] = Action.async { implicit request =>
    accountRepository.list().map(accounts => Ok(views.html.account.allAccounts(accounts)))
  }

  def getById(id: Int) = Action.async { implicit request =>
    val account = accountRepository.getById(id)
    account.map(account => account match {
      case Some(p) => Ok(views.html.account.accountById(p))
      case None => Redirect(routes.AccountFormController.allAccounts)
    })
  }

  def createAccount(): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    val users = Await.result(userRepository.getAll(), Duration.Inf)
    Ok(views.html.account.createAccount(accountForm, users))
  }

  def createAccountHandle(): Action[AnyContent] = Action.async { implicit request =>
    accountForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.account.createAccount(errorForm, userList))
        )
      },
      account => {
        accountRepository.create(account.providerKey, account.first_name, account.last_name).map { _ =>
          Redirect("/accounts/all")
        }
      }
    )
  }
}
case class CreateAccountForm(providerKey: String, first_name: String, last_name: String)
case class UpdateAccountForm(id: Int, providerKey: String, first_name: String, last_name: String)