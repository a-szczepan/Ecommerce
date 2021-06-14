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

    def categoryId = column[Int]("categoryId")

    def categoryFk = foreignKey("categoryId", categoryId, category)(_.id)

    def name: Rep[String] = column[String]("name")

    def description: Rep[String] = column[String]("description")

    def image: Rep[String] = column[String]("image")

    def price: Rep[String] = column[String]("price")

    def * = (id, categoryId, name, description, image, price) <> ((Product.apply _).tupled, Product.unapply)
  }

  import categoryRepository.CategoryTable

  val category = TableQuery[CategoryTable]
  val product = TableQuery[ProductTable]

  def create(categoryId: Int, name: String, description: String, image: String, price: String): Future[Product] = db.run {
    (product.map(p => (p.categoryId, p.name, p.description, p.image, p.price))
      returning product.map(_.id)
      into { case ((categoryId, name, description, image, price), id) => Product(id, categoryId, name, description, image, price) }
      ) += (categoryId, name, description, image, price)
  }

  def list(): Future[Seq[Product]] = db.run {
    product.result
  }

  def update(id: Int, newProduct: Product): Future[Int] = {
    val productToUpdate: Product = newProduct.copy(id)
    db.run(product.filter(_.id === id).update(productToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(product.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Product]] = db.run {
    product.filter(_.id === id).result.headOption
  }

  def getByCategoryId(categoryId: Int): Future[Seq[Product]] = db.run {
    product.filter(_.categoryId === categoryId).result
  }


}
