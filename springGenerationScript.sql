-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema spring
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema spring
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `spring` DEFAULT CHARACTER SET utf8;
USE `spring`;

-- -----------------------------------------------------
-- Table `spring`.`inspectors`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spring`.`inspectors`;

CREATE TABLE IF NOT EXISTS `spring`.`inspectors`
(
    `id`         BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(50)  NOT NULL,
    `name`       VARCHAR(50)  NOT NULL,
    `password`   VARCHAR(70)  NOT NULL,
    `patronymic` VARCHAR(50)  NOT NULL,
    `role`       VARCHAR(255) NULL DEFAULT NULL,
    `surname`    VARCHAR(50)  NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `spring`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spring`.`users`;

CREATE TABLE IF NOT EXISTS `spring`.`users`
(
    `id`           BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `email`        VARCHAR(50)  NOT NULL,
    `name`         VARCHAR(50)  NOT NULL,
    `password`     VARCHAR(70)  NOT NULL,
    `patronymic`   VARCHAR(50)  NOT NULL,
    `role`         VARCHAR(255) NULL DEFAULT NULL,
    `surname`      VARCHAR(50)  NOT NULL,
    `inn_code`     INT(11)      NOT NULL,
    `inspector_id` BIGINT(20)   NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FKnojt432w6ydj99hq552pgipjk` (`inspector_id` ASC) VISIBLE,
    CONSTRAINT `FKnojt432w6ydj99hq552pgipjk`
        FOREIGN KEY (`inspector_id`)
            REFERENCES `spring`.`inspectors` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `spring`.`reports`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spring`.`reports`;

CREATE TABLE IF NOT EXISTS `spring`.`reports`
(
    `id`        BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `date`      TIMESTAMP    NOT NULL,
    `file_link` VARCHAR(500) NOT NULL,
    `status`    VARCHAR(255) NULL DEFAULT NULL,
    `user_id`   BIGINT(20)   NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `FK2o32rer9hfweeylg7x8ut8rj2` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FK2o32rer9hfweeylg7x8ut8rj2`
        FOREIGN KEY (`user_id`)
            REFERENCES `spring`.`users` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `spring`.`actions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spring`.`actions`;

CREATE TABLE IF NOT EXISTS `spring`.`actions`
(
    `id`           BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `action`       VARCHAR(255) NULL DEFAULT NULL,
    `date`         TIMESTAMP    NOT NULL,
    `name`         VARCHAR(50)  NOT NULL,
    `inspector_id` BIGINT(20)   NOT NULL,
    `report_id`    BIGINT(20)   NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `FK360sesx60rkeq9jjkpmoel0ob` (`inspector_id` ASC) VISIBLE,
    INDEX `FKkwe09xjmkxmq2ojfh6kvx5bn7` (`report_id` ASC) VISIBLE,
    CONSTRAINT `FK360sesx60rkeq9jjkpmoel0ob`
        FOREIGN KEY (`inspector_id`)
            REFERENCES `spring`.`inspectors` (`id`),
    CONSTRAINT `FKkwe09xjmkxmq2ojfh6kvx5bn7`
        FOREIGN KEY (`report_id`)
            REFERENCES `spring`.`reports` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `spring`.`request`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spring`.`request`;

CREATE TABLE IF NOT EXISTS `spring`.`request`
(
    `id`      BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `message` VARCHAR(500) NOT NULL,
    `user_id` BIGINT(20)   NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `FKg03bldv86pfuboqfefx48p6u3` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FKg03bldv86pfuboqfefx48p6u3`
        FOREIGN KEY (`user_id`)
            REFERENCES `spring`.`users` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
