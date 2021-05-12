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


}
case class CreateUserForm(account_id: Int, email: String, password: String)

case class UpdateUserForm(id: Int = 0, account_id: Int, email: String, password: String)