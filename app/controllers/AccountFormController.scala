package controllers
import models.{Account,AccountRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AccountFormController @Inject()(accountRepository: AccountRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){
  val accountForm: Form[CreateAccountForm] = Form {
    mapping(
      "first_name" -> nonEmptyText,
      "last_name" -> nonEmptyText,
    )(CreateAccountForm.apply)(CreateAccountForm.unapply)
  }

  val updateAccountForm: Form[UpdateAccountForm] = Form {
    mapping(
      "id" -> number,
      "first_name" -> nonEmptyText,
      "last_name" -> nonEmptyText,
    )(UpdateAccountForm.apply)(UpdateAccountForm.unapply)
  }

  def updateAccount(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val account = accountRepository.getById(id)
    account.map(account => {
      val accountForm = updateAccountForm.fill(UpdateAccountForm(account.get.id, account.get.first_name, account.get.last_name))
      Ok(views.html.account.updateAccount(accountForm))
    })
  }

  def updateAccountHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateAccountForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.account.updateAccount(errorForm))
        )
      },
      account => {
        accountRepository.update(account.id, Account(account.id, account.first_name, account.last_name)).map { _ =>
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

  def createAccount(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val accounts = accountRepository.list()
    accounts.map(_ => Ok(views.html.account.createAccount(accountForm)))
  }

  def createAccountHandle(): Action[AnyContent] = Action.async { implicit request =>
    accountForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.account.createAccount(errorForm))
        )
      },
      account => {
        accountRepository.create(account.first_name, account.last_name).map { _ =>
          Redirect("/accounts/all")
        }
      }
    )
  }
}
case class CreateAccountForm(first_name: String, last_name: String)
case class UpdateAccountForm(id: Int, first_name: String, last_name: String)