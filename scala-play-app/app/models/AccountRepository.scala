package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AccountRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class AccountTable(tag: Tag) extends Table[Account](tag, "account") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def first_name: Rep[String] = column[String]("first_name")

    def last_name: Rep[String] = column[String]("last_name")

    def * = (id, first_name, last_name) <> ((Account.apply _).tupled, Account.unapply)
  }


  val account = TableQuery[AccountTable]

  def create(first_name: String, last_name: String): Future[Account] = db.run {
    (account.map(a => ( a.first_name, a.last_name))
      returning account.map(_.id)
      into { case ((first_name, last_name), id) => Account(id,first_name,last_name) }
      ) += (first_name, last_name)
  }

  def list(): Future[Seq[Account]] = db.run {
    account.result
  }

  def update(id: Int, new_account: Account): Future[Int] = {
    val accountToUpdate: Account = new_account.copy(id)
    db.run(account.filter(_.id === id).update(accountToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(account.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Account]] = db.run {
    account.filter(_.id === id).result.headOption
  }

}
