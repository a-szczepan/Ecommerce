package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShippingRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ShippingTable(tag: Tag) extends Table[Shipping](tag, "shipping") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def providerKey = column[String]("providerKey")

    def user_fk = foreignKey("providerKey", providerKey, usr)(_.providerKey)

    def street_name = column[String]("street_name")

    def building_number = column[String]("building_number")


    def postal_code = column[String]("postal_code")

    def city = column[String]("city")

    def * = (id, providerKey, street_name, building_number, postal_code, city) <> ((Shipping.apply _).tupled, Shipping.unapply)
  }

  import userRepository.UserTable
  val usr = TableQuery[UserTable]
  val shipping = TableQuery[ShippingTable]

  def create(providerKey: String, street_name: String, building_number: String,  postal_code: String, city: String): Future[Shipping] = db.run {
    (shipping.map(s => (s.providerKey, s.street_name, s.building_number, s.postal_code, s.city))
      returning shipping.map(_.id)
      into { case ((providerKey, street_name, building_number,postal_code, city), id) => Shipping(id,providerKey, street_name, building_number, postal_code, city) }
      ) += (providerKey, street_name, building_number,  postal_code, city)
  }

  def list(): Future[Seq[Shipping]] = db.run {
    shipping.result
  }

  def update(id: Int, new_shipping: Shipping): Future[Int] = {
    val shippingToUpdate: Shipping = new_shipping.copy(id)
    db.run(shipping.filter(_.id === id).update(shippingToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(shipping.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Shipping]] = db.run {
    shipping.filter(_.id === id).result.headOption
  }

  def getByKey(providerKey: String): Future[Option[Shipping]] = db.run {
    shipping.filter(_.providerKey === providerKey).result.headOption
  }

}
