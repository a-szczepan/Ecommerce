package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ProductTable(tag: Tag) extends Table[Product](tag, "product") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def category_id = column[Int]("category_id")

    def category_fk = foreignKey("category_id", category_id, category)(_.id)

    def name: Rep[String] = column[String]("name")

    def * = (id, category_id, name) <> ((Product.apply _).tupled, Product.unapply)
  }

  import categoryRepository.CategoryTable

  val category = TableQuery[CategoryTable]
  val product = TableQuery[ProductTable]

  def create(category_id: Int, name: String): Future[Product] = db.run {
    (product.map(p => (p.category_id, p.name))
      returning product.map(_.id)
      into { case ((category_id, name), id) => Product(id, category_id, name) }
      ) += (category_id, name)
  }

  def list(): Future[Seq[Product]] = db.run {
    product.result
  }

  def update(id: Int, new_product: Product): Future[Int] = {
    val productToUpdate: Product = new_product.copy(id)
    db.run(product.filter(_.id === id).update(productToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(product.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Product]] = db.run {
    product.filter(_.id === id).result.headOption
  }

  def getByCategoryId(category_id: Int): Future[Seq[Product]] = db.run {
    product.filter(_.category_id === category_id).result
  }


}
