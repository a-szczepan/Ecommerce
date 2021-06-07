# --- !Ups

CREATE TABLE "apporder"
(
    "id"          INTEGER NOT NULL PRIMARY KEY UNIQUE,
    "cart_id"     INT     NOT NULL,
    "shipping_id" INT     NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES cart (id),
    FOREIGN KEY (shipping_id) REFERENCES shipping (id)
);

CREATE TABLE "account"
(
    "id"         INTEGER NOT NULL PRIMARY KEY UNIQUE,
    "first_name" VARCHAR NOT NULL,
    "last_name"  VARCHAR NOT NULL
);


CREATE TABLE "wishlist"
(
    "id"         INTEGER NOT NULL PRIMARY KEY UNIQUE,
    "user_id"    INTEGER NOT NULL,
    "product_id" INTEGER NOT NULL,
    UNIQUE (product_id)
        FOREIGN KEY (user_id) REFERENCES appuser (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE "cart"
(
    "id"         INTEGER NOT NULL PRIMARY KEY UNIQUE,
    "user_id"    INTEGER NOT NULL,
    "product_id" INTEGER NOT NULL,
    "quantity"   INTEGER NOT NULL,
    UNIQUE (product_id)
    FOREIGN KEY (user_id) REFERENCES appuser (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE "shipping"
(
    "id"               INTEGER NOT NULL PRIMARY KEY UNIQUE,
    "street_name"      VARCHAR NOT NULL,
    "building_number"  INT     NOT NULL,
    "apartment_number" INT     NOT NULL,
    "postal_code"      VARCHAR NOT NULL,
    "city"             VARCHAR NOT NULL
);

CREATE TABLE "product"
(
    "id"          INTEGER NOT NULL PRIMARY KEY UNIQUE,
    "category_id" INTEGER NOT NULL,
    "name"        VARCHAR NOT NULL,
    "description" VARCHAR NOT NULL,
    "image"       VARCHAR NOT NULL,
    "price"       VARCHAR NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE "category"
(
    "id"   INTEGER NOT NULL PRIMARY KEY UNIQUE,
    "name" VARCHAR NOT NULL
);

CREATE TABLE "opinion"
(
    "id"          INTEGER NOT NULL PRIMARY KEY UNIQUE,
    "user_id"     INTEGER NOT NULL,
    "product_id"  INTEGER NOT NULL,
    "opinion_txt" VARCHAR NOT NULL,
    FOREIGN KEY (user_id) REFERENCES appuser (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE "payment"
(
    "id"       INTEGER NOT NULL PRIMARY KEY UNIQUE,
    "order_id" INTEGER NOT NULL,
    "date"     VARCHAR NOT NULL,
    "amount"   VARCHAR NOT NULL,
    FOREIGN KEY (order_id) REFERENCES apporder (id)
);

CREATE TABLE "appuser"
(
    "id"         INTEGER NOT NULL PRIMARY KEY UNIQUE,
    "account_id" INTEGER NOT NULL,
    "email"      VARCHAR NOT NULL,
    "password"   VARCHAR NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account (id)

);

# --- !Downs

DROP TABLE "payment";
DROP TABLE "apporder";
DROP TABLE "appuser";
DROP TABLE "account";
DROP TABLE "wishlist";
DROP TABLE "cart";
DROP TABLE "shipping";
DROP TABLE "product";
DROP TABLE "category";
DROP TABLE "opinion";


