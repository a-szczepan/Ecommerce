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
    "providerKey"    VARCHAR     NOT NULL,
    "first_name" VARCHAR NOT NULL,
    "last_name"  VARCHAR NOT NULL,
    FOREIGN KEY (providerKey) REFERENCES appuser (providerKey)
);


CREATE TABLE "wishlist"
(
    "id"         INTEGER NOT NULL PRIMARY KEY UNIQUE,
    "providerKey"    VARCHAR NOT NULL,
    "product_id" INTEGER NOT NULL,
    UNIQUE (product_id)
        FOREIGN KEY (providerKey) REFERENCES appuser (providerKey),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE "cart"
(
    "id"         INTEGER NOT NULL PRIMARY KEY UNIQUE,
    "providerKey"    VARCHAR NOT NULL,
    "product_id" INTEGER NOT NULL,
    "quantity"   INTEGER NOT NULL,
    UNIQUE (product_id)
        FOREIGN KEY (providerKey) REFERENCES appuser (providerKey),
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
    "providerKey"     VARCHAR NOT NULL,
    "product_id"  INTEGER NOT NULL,
    "opinion_txt" VARCHAR NOT NULL,
    FOREIGN KEY (providerKey) REFERENCES appuser (providerKey),
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
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerId"  VARCHAR NOT NULL,
    "providerKey" VARCHAR NOT NULL,
    "email"       VARCHAR NOT NULL
);

CREATE TABLE "authToken"
(
    "id"     INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "userId" INT     NOT NULL,
    FOREIGN KEY (userId) references user (id)
);

CREATE TABLE "passwordInfo"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerId"  VARCHAR NOT NULL,
    "providerKey" VARCHAR NOT NULL,
    "hasher"      VARCHAR NOT NULL,
    "password"    VARCHAR NOT NULL,
    "salt"        VARCHAR
);

CREATE TABLE "oAuth2Info"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerId"  VARCHAR NOT NULL,
    "providerKey" VARCHAR NOT NULL,
    "accessToken" VARCHAR NOT NULL,
    "tokenType"   VARCHAR,
    "expiresIn"   INTEGER
);


# --- !Downs

DROP TABLE "payment";
DROP TABLE "apporder";
DROP TABLE "account";
DROP TABLE "wishlist";
DROP TABLE "cart";
DROP TABLE "shipping";
DROP TABLE "product";
DROP TABLE "category";
DROP TABLE "opinion";
DROP TABLE "appuser";
DROP TABLE "authToken";
DROP TABLE "passwordInfo";
DROP TABLE "oAuth2Info";


