package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val accountRepository: AccountRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "appuser") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def account_id = column[Int]("account_id")

    def account_fk = foreignKey("account_id", account_id, account)(_.id)

    def email = column[String]("email")

    def password = column[String]("password")

    def * = (id, account_id, email, password) <> ((User.apply _).tupled, User.unapply)
  }

  import accountRepository.AccountTable

  val account = TableQuery[AccountTable]
  val usr = TableQuery[UserTable]

  def create(account_id: Int, email: String, password: String): Future[User] = db.run {
    (usr.map(u => (u.account_id, u.email, u.password))
      returning usr.map(_.id)
      into { case ((account_id, email, password), id) => User(id, account_id, email, password) }
      ) += (account_id, email, password)
  }

  def list(): Future[Seq[User]] = db.run {
    usr.result
  }

  def update(id: Int, new_user: User): Future[Int] = {
    val userToUpdate: User = new_user.copy(id)
    db.run(usr.filter(_.id === id).update(userToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(usr.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[User]] = db.run {
    usr.filter(_.id === id).result.headOption
  }

  def getByAccount(account_id: Int): Future[Seq[User]] = db.run {
    usr.filter(_.account_id === account_id).result
  }

}
