/*
  MySQL SQL query script for Bookstore database
  Designed for MariaDB RDBMS
  
  Author:
    Pekka Helenius <fincer89 [at] hotmail [dot] com>
    Fjordtek, 2020
*/

-- -----------------------------------------------------
-- Used database is defined in resources/application-prod.properties

-- USE <DATABASE_NAME>;

-- -----------------------------------------------------
-- Table AUTHOR
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS AUTHOR (
  id        INT           NOT NULL AUTO_INCREMENT,
  firstname NVARCHAR(100) NULL,
  lastname  NVARCHAR(100) NULL,
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table CATEGORY
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS CATEGORY (
  id   INT          NOT NULL AUTO_INCREMENT,
  name NVARCHAR(50) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table BOOK
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS BOOK (
  id          INT           NOT NULL AUTO_INCREMENT,
  isbn        VARCHAR(11)   NOT NULL UNIQUE,
  price       DECIMAL(5,2)  NOT NULL,
  title       NVARCHAR(100) NOT NULL,
  year        INT           NOT NULL,
  author_id   INT           NULL,
  category_id INT           NULL,
  publish     BIT           NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (author_id)
    REFERENCES AUTHOR (id),
  FOREIGN KEY (category_id)
    REFERENCES CATEGORY (id)
);

-- -----------------------------------------------------
-- Table BOOK_HASH
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS BOOK_HASH (
  book_id INT      NOT NULL,
  hash_id CHAR(32) NOT NULL UNIQUE,
  PRIMARY KEY (book_id),
  FOREIGN KEY (book_id)
    REFERENCES BOOK (id)
);

-- -----------------------------------------------------
-- Table USER
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS USER (
  id       INT            NOT NULL AUTO_INCREMENT,
  username NVARCHAR(100)  NOT NULL UNIQUE,
  password NVARCHAR(1024) NOT NULL,
  email    NVARCHAR(100)  NOT NULL,
  PRIMARY KEY (id)
);


-- -----------------------------------------------------
-- Table ROLE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ROLE (
  id INT           NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table USER_ROLE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS USER_ROLE (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id)
    REFERENCES USER (id),
  FOREIGN KEY (role_id)
    REFERENCES ROLE (id)
);

/*

Sequenced drop table rules:

DROP table USER_ROLE;
DROP table ROLE;
DROP table USER;

DROP table BOOK_HASH;
DROP table BOOK;
DROP table CATEGORY;
DROP table AUTHOR;

*/
