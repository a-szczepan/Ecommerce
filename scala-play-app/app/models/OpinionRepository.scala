package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OpinionRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository, val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class OpinionTable(tag: Tag) extends Table[Opinion](tag, "opinion") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def providerKey = column[String]("providerKey")

    def user_fk = foreignKey("providerKey", providerKey, usr)(_.providerKey)

    def product_id = column[Int]("product_id")

    def product_fk = foreignKey("product_id", product_id, product)(_.id)

    def opinion_txt = column[String]("opinion_txt")

    def * = (id, providerKey, product_id, opinion_txt) <> ((Opinion.apply _).tupled, Opinion.unapply)
  }

  import userRepository.UserTable
  import productRepository.ProductTable

  val usr = TableQuery[UserTable]
  val product = TableQuery[ProductTable]
  val opinion = TableQuery[OpinionTable]

  def create(providerKey: String, product_id: Int, opinion_txt: String): Future[Opinion] = db.run {
    (opinion.map(o => (o.providerKey, o.product_id, o.opinion_txt))
      returning opinion.map(_.id)
      into { case ((providerKey, product_id, opinion_txt), id) => Opinion(id, providerKey, product_id, opinion_txt) }
      ) += (providerKey, product_id, opinion_txt)
  }

  def list(): Future[Seq[Opinion]] = db.run {
    opinion.result
  }

  def update(id: Int, new_opinion: Opinion): Future[Int] = {
    val opinionToUpdate: Opinion = new_opinion.copy(id)
    db.run(opinion.filter(_.id === id).update(opinionToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(opinion.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Opinion]] = db.run {
    opinion.filter(_.id === id).result.headOption
  }

  def getByUserKey(providerKey: String): Future[Seq[Opinion]] = db.run {
    opinion.filter(_.providerKey === providerKey).result
  }

  def getByProduct(product_id: Int): Future[Seq[Opinion]] = db.run {
    opinion.filter(_.product_id === product_id).result
  }
}
