package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AccountRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class AccountTable(tag: Tag) extends Table[Account](tag, "account") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def providerKey = column[String]("providerKey")

    def userFk = foreignKey("providerKey", providerKey, usr)(_.providerKey)

    def firstName: Rep[String] = column[String]("firstName")

    def lastName: Rep[String] = column[String]("lastName")

    def * = (id,providerKey, firstName, lastName) <> ((Account.apply _).tupled, Account.unapply)
  }

  import userRepository.UserTable
  val account = TableQuery[AccountTable]
  val usr = TableQuery[UserTable]

  def create(providerKey: String, firstName: String, lastName: String): Future[Account] = db.run {
    (account.map(a => (a.providerKey, a.firstName, a.lastName))
      returning account.map(_.id)
      into { case ((providerKey, firstName, lastName), id) => Account(id, providerKey, firstName,lastName) }
      ) += (providerKey, firstName, lastName)
  }

  def list(): Future[Seq[Account]] = db.run {
    account.result
  }

  def update(id: Int, newAccount: Account): Future[Int] = {
    val accountToUpdate: Account = newAccount.copy(id)
    db.run(account.filter(_.id === id).update(accountToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(account.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Account]] = db.run {
    account.filter(_.id === id).result.headOption
  }

  def getByKey(providerKey: String): Future[Option[Account]] = db.run {
    account.filter(_.providerKey === providerKey).result.headOption
  }

}
