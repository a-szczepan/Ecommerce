CREATE TABLE "appuser"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "account_id"  INTEGER NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account (id),
    "order_id"    INTEGER NOT NULL,
    FOREIGN KEY (order_id) REFERENCES apporder (id),
    "wishlist_id" INTEGER NOT NULL,
    FOREIGN KEY (wishlist_id) REFERENCES wishlist (id),
    "cart_id"     INTEGER NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES cart (id),
    "email"       VARCHAR NOT NULL,
    "password"    VARCHAR NOT NULL
);

CREATE TABLE "account"
(
    "id"         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "user_id"    INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES appuser (id),
    "first_name" VARCHAR NOT NULL,
    "last_name"  VARCHAR NOT NULL
);

CREATE TABLE "apporder"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "cart_id"     INTEGER NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES cart (id),
    "shipping_id" INTEGER NOT NULL,
    FOREIGN KEY (shipping_id) REFERENCES shipping (id)
);

CREATE TABLE "wishlist"
(
    "id"         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "user_id"    INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES appuser (id),
    "product_id" INTEGER NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE "cart"
(
    "id"         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "user_id"    INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES appuser (id),
    "product_id" INTEGER NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE "shipping"
(
    "id"               INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "street_name"      VARCHAR NOT NULL,
    "building_number"  INTEGER NOT NULL,
    "apartment_number" INTEGER NOT NULL,
    "postal_code"      VARCHAR NOT NULL,
    "city"             VARCHAR NOT NULL
);

CREATE TABLE "product"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "category_id" INTEGER NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category (id),
    "name"        VARCHAR NOT NULL
);

CREATE TABLE "category"
(
    "id"   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name" VARCHAR NOT NULL
);

CREATE TABLE "opinion"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "user_id"     INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES appuser (id),
    "product_id"  INTEGER NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id),
    "opinion_txt" VARCHAR NOT NULL
);

CREATE TABLE "payment"
(
    "id"       INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "order_id" INTEGER NOT NULL,
    FOREIGN KEY (order_id) REFERENCES apporder (id),
    "date"     VARCHAR NOT NULL,
    "amount"   FLOAT   NOT NULL
);

DROP TABLE "appuser";
DROP TABLE "apporder";
DROP TABLE "account";
DROP TABLE "wishlist";
DROP TABLE "cart";
DROP TABLE "shipping";
DROP TABLE "product";
DROP TABLE "category";
DROP TABLE "opinion";
DROP TABLE "payment";

