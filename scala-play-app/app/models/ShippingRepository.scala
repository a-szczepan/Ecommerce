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

    def userFk = foreignKey("providerKey", providerKey, usr)(_.providerKey)

    def streetName = column[String]("streetName")

    def buildingNumber = column[String]("buildingNumber")


    def postalCode = column[String]("postalCode")

    def city = column[String]("city")

    def * = (id, providerKey, streetName, buildingNumber, postalCode, city) <> ((Shipping.apply _).tupled, Shipping.unapply)
  }

  import userRepository.UserTable
  val usr = TableQuery[UserTable]
  val shipping = TableQuery[ShippingTable]

  def create(providerKey: String, streetName: String, buildingNumber: String,  postalCode: String, city: String): Future[Shipping] = db.run {
    (shipping.map(s => (s.providerKey, s.streetName, s.buildingNumber, s.postalCode, s.city))
      returning shipping.map(_.id)
      into { case ((providerKey, streetName, buildingNumber,postalCode, city), id) => Shipping(id,providerKey, streetName, buildingNumber, postalCode, city) }
      ) += (providerKey, streetName, buildingNumber,  postalCode, city)
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
