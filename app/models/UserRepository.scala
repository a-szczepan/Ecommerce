package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val accountRepository: AccountRepository, val orderRepository: OrderRepository, val wishlistRepository: WishlistRepository, val cartRepository: CartRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "appuser") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def account_id = column[Int]("account_id")

    def account_fk = foreignKey("account_id", account_id, account)(_.id)

    def order_id = column[Int]("order_id")

    def order_fk = foreignKey("order_id", order_id, order)(_.id)

    def wishlist_id = column[Int]("wishlist_id")

    def wishlist_fk = foreignKey("wishlist_id", wishlist_id, wishlist)(_.id)

    def cart_id = column[Int]("cart_id")

    def cart_fk = foreignKey("cart_id", cart_id, cart)(_.id)

    def email = column[String]("email")

    def password = column[String]("password")

    def * = (id, account_id, order_id, wishlist_id, cart_id, email, password) <> ((User.apply _).tupled, User.unapply)
  }

  import accountRepository.AccountTable
  import orderRepository.OrderTable
  import wishlistRepository.WishlistTable
  import cartRepository.CartTable

  val account = TableQuery[AccountTable]
  val order = TableQuery[OrderTable]
  val wishlist = TableQuery[WishlistTable]
  val cart = TableQuery[CartTable]
  val usr = TableQuery[UserTable]

  def create(account_id: Int, order_id: Int, wishlist_id: Int, cart_id: Int, email: String, password: String): Future[User] = db.run {
    (usr.map(u => (u.account_id, u.order_id, u.wishlist_id, u.cart_id, u.email, u.password))
      returning usr.map(_.id)
      into { case ((account_id, order_id, wishlist_id, cart_id, email, password), id) => User(id, account_id, order_id, wishlist_id, cart_id, email, password) }
      ) += (account_id, order_id, wishlist_id, cart_id, email, password)
  }

  def list(): Future[Seq[User]] = db.run {
    usr.result
  }

  def update(id: Int, new_user: User): Future[Unit] = {
    val userToUpdate: User = new_user.copy(id)
    db.run(usr.filter(_.id === id).update(userToUpdate)).map(_ => ())
  }

  def delete(id: Int): Future[Int] = db.run(usr.filter(_.id === id).delete)

}
