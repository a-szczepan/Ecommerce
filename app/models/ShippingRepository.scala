package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShippingRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ShippingTable(tag: Tag) extends Table[Shipping](tag, "shipping") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def street_name = column[String]("street_name")

    def building_number = column[Int]("building_number")

    def apartment_number = column[Int]("apartment_number")

    def postal_code = column[String]("postal_code")

    def city = column[String]("city")

    def * = (id, street_name, building_number, apartment_number, postal_code, city) <> ((Shipping.apply _).tupled, Shipping.unapply)
  }

  val shipping = TableQuery[ShippingTable]

  def create(street_name: String, building_number: Int, apartment_number: Int, postal_code: String, city: String): Future[Shipping] = db.run {
    (shipping.map(s => (s.street_name, s.building_number, s.apartment_number, s.postal_code, s.city))
      returning shipping.map(_.id)
      into { case ((street_name, building_number, apartment_number, postal_code, city), id) => Shipping(id, street_name, building_number, apartment_number, postal_code, city) }
      ) += (street_name, building_number, apartment_number, postal_code, city)
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

}
