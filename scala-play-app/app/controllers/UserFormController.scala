package controllers
import models.{User, UserRepository, Account, AccountRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class UserFormController @Inject()(userRepository: UserRepository,
                                      accountRepository: AccountRepository,
                                      cc: MessagesControllerComponents)
                                     (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  var accountList: Seq[Account] = Seq[Account]()

  accountRepository.list().onComplete {
    case Success(account) => accountList = account
    case Failure(e) => print("", e)
  }

  val userForm: Form[CreateUserForm] = Form {
    mapping(
      "account_id" -> number,
      "email" -> nonEmptyText,
      "password" -> nonEmptyText,
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }

  val updateUserForm: Form[UpdateUserForm] = Form {
    mapping(
      "id" -> number,
      "account_id" -> number,
      "email" -> nonEmptyText,
      "password" -> nonEmptyText,
    )(UpdateUserForm.apply)(UpdateUserForm.unapply)
  }

  /* -------------------------------------------- */

  def updateUser(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val user = userRepository.getById(id)
    user.map(user => {
      val userForm = updateUserForm.fill(UpdateUserForm(user.get.id, user.get.account_id, user.get.email, user.get.password))
      Ok(views.html.user.updateUser(userForm, accountList))
    })
  }

  def updateUserHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateUserForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.user.updateUser(errorForm, accountList))
        )
      },
      user => {
        userRepository.update(user.id, User(user.id, user.account_id, user.email, user.password)).map { _ =>
          Redirect("/users/all")
        }
      }
    )
  }

  def deleteUser(id: Int): Action[AnyContent] = Action {
    userRepository.delete(id)
    Redirect("/users")
  }

  def allUsers: Action[AnyContent] = Action.async { implicit request =>
    userRepository.list().map(users => Ok(views.html.user.allUsers(users)))
  }

  def getById(id: Int) = Action.async { implicit request =>
    val user = userRepository.getById(id)
    user.map(user => user match {
      case Some(p) => Ok(views.html.user.userById(p))
      case None => Redirect(routes.UserFormController.allUsers)
    })
  }

  def createUser() = Action { implicit request =>
    val accounts = Await.result(accountRepository.list(), Duration.Inf)
    Ok(views.html.user.createUser(userForm, accounts))
  }

  def createUserHandle(): Action[AnyContent] = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.user.createUser(errorForm, accountList))
        )
      },
      user => {
        userRepository.create(user.account_id, user.email, user.password).map { _ =>
          Redirect("/users/all")
        }
      }
    )
  }

}
case class CreateUserForm(account_id: Int, email: String, password: String)

case class UpdateUserForm(id: Int = 0, account_id: Int, email: String, password: String)