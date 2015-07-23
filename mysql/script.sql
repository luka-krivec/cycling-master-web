-- MySQL Script generated by MySQL Workbench
-- 01/30/15 22:17:34
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema tracker
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema tracker
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tracker` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `tracker` ;

-- -----------------------------------------------------
-- Table `tracker`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker`.`Users` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  `idFacebook` VARCHAR(128) NULL,
  `email` VARCHAR(128) NULL,
  `userName` VARCHAR(128) NOT NULL,
  `profilePicPath` VARCHAR(512) NULL,
  `friendsCount` INT NULL,
  `birthday` DATE NULL,
  `dateCreated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idUser`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `userName_UNIQUE` (`userName` ASC),
  UNIQUE INDEX `idFacebook_UNIQUE` (`idFacebook` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracker`.`Routes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker`.`Routes` (
  `idRoute` INT NOT NULL AUTO_INCREMENT,
  `routeName` VARCHAR(256) NULL,
  `distance` FLOAT NULL,
  `averageSpeed` FLOAT NULL,
  `startTime` TIMESTAMP NULL,
  `endTime` TIMESTAMP NULL,
  `dateCreated` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idRoute`, `idUser`),
  INDEX `fk_Routes_Users_idx` (`idUser` ASC),
  CONSTRAINT `fk_Routes_Users`
    FOREIGN KEY (`idUser`)
    REFERENCES `tracker`.`Users` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracker`.`Points`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker`.`Points` (
  `idPoint` INT NOT NULL AUTO_INCREMENT,
  `lat` FLOAT(10,6) NOT NULL,
  `lng` FLOAT(10,6) NOT NULL,
  `altitude` FLOAT NULL,
  `accuracy` FLOAT NULL,
  `speed` FLOAT NULL,
  `bearing` FLOAT NULL,
  `dateCreated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `idRoute` INT NOT NULL,
  PRIMARY KEY (`idPoint`, `idRoute`),
  INDEX `fk_Points_Routes1_idx` (`idRoute` ASC),
  CONSTRAINT `fk_Points_Routes1`
    FOREIGN KEY (`idRoute`)
    REFERENCES `tracker`.`Routes` (`idRoute`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracker`.`Plans`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker`.`Plans` (
  `idPlan` INT NOT NULL AUTO_INCREMENT,
  `planText` VARCHAR(512) NOT NULL,
  `dateCreated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idPlan`, `idUser`),
  INDEX `fk_Plans_Users1_idx` (`idUser` ASC),
  CONSTRAINT `fk_Plans_Users1`
    FOREIGN KEY (`idUser`)
    REFERENCES `tracker`.`Users` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracker`.`PlanLikes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker`.`PlanLikes` (
  `idPlan` INT NOT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idUser`, `idPlan`),
  INDEX `fk_PlanLikes_Plans1_idx` (`idPlan` ASC),
  INDEX `fk_PlanLikes_Users1_idx` (`idUser` ASC),
  CONSTRAINT `fk_PlanLikes_Plans1`
    FOREIGN KEY (`idPlan`)
    REFERENCES `tracker`.`Plans` (`idPlan`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PlanLikes_Users1`
    FOREIGN KEY (`idUser`)
    REFERENCES `tracker`.`Users` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracker`.`PlanComments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker`.`PlanComments` (
  `idPlanComment` INT NOT NULL AUTO_INCREMENT,
  `planComment` VARCHAR(512) NOT NULL,
  `dateCreated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `idPlan` INT NOT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idPlanComment`, `idPlan`, `idUser`),
  INDEX `fk_PlanComment_Plans1_idx` (`idPlan` ASC),
  INDEX `fk_PlanComments_Users1_idx` (`idUser` ASC),
  CONSTRAINT `fk_PlanComment_Plans1`
    FOREIGN KEY (`idPlan`)
    REFERENCES `tracker`.`Plans` (`idPlan`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PlanComments_Users1`
    FOREIGN KEY (`idUser`)
    REFERENCES `tracker`.`Users` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracker`.`RouteLikes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker`.`RouteLikes` (
  `idRoute` INT NOT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idUser`, `idRoute`),
  INDEX `fk_RouteLikes_Routes1_idx` (`idRoute` ASC),
  INDEX `fk_RouteLikes_Users1_idx` (`idUser` ASC),
  CONSTRAINT `fk_RouteLikes_Routes1`
    FOREIGN KEY (`idRoute`)
    REFERENCES `tracker`.`Routes` (`idRoute`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RouteLikes_Users1`
    FOREIGN KEY (`idUser`)
    REFERENCES `tracker`.`Users` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracker`.`RouteComments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker`.`RouteComments` (
  `idRouteComment` INT NOT NULL AUTO_INCREMENT,
  `routeComment` VARCHAR(512) NOT NULL,
  `dateCreated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `idRoute` INT NOT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idRouteComment`, `idRoute`, `idUser`),
  INDEX `fk_RouteComments_Routes1_idx` (`idRoute` ASC),
  INDEX `fk_RouteComments_Users1_idx` (`idUser` ASC),
  CONSTRAINT `fk_RouteComments_Routes1`
    FOREIGN KEY (`idRoute`)
    REFERENCES `tracker`.`Routes` (`idRoute`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RouteComments_Users1`
    FOREIGN KEY (`idUser`)
    REFERENCES `tracker`.`Users` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracker`.`Friends`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker`.`Friends` (
  `friendOne` INT NULL,
  `friendTwo` INT NULL,
  `dateCreated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` ENUM('0','1','2') NOT NULL DEFAULT '0',
  INDEX `fk_Friends_Users1_idx` (`friendOne` ASC),
  INDEX `fk_Friends_Users2_idx` (`friendTwo` ASC),
  PRIMARY KEY (`friendOne`, `friendTwo`),
  CONSTRAINT `fk_Friends_Users1`
    FOREIGN KEY (`friendOne`)
    REFERENCES `tracker`.`Users` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Friends_Users2`
    FOREIGN KEY (`friendTwo`)
    REFERENCES `tracker`.`Users` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracker`.`PlanCommentLikes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker`.`PlanCommentLikes` (
  `idPlanComment` INT NOT NULL,
  `idPlan` INT NOT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idUser`, `idPlan`, `idPlanComment`),
  INDEX `fk_PlanCommentLikes_PlanComments1_idx` (`idPlanComment` ASC, `idPlan` ASC, `idUser` ASC),
  CONSTRAINT `fk_PlanCommentLikes_PlanComments1`
    FOREIGN KEY (`idPlanComment` , `idPlan` , `idUser`)
    REFERENCES `tracker`.`PlanComments` (`idPlanComment` , `idPlan` , `idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tracker`.`RouteCommentLikes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tracker`.`RouteCommentLikes` (
  `idRouteComment` INT NOT NULL,
  `idRoute` INT NOT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idRouteComment`, `idRoute`, `idUser`),
  CONSTRAINT `fk_RouteCommentLikes_RouteComments1`
    FOREIGN KEY (`idRouteComment` , `idRoute` , `idUser`)
    REFERENCES `tracker`.`RouteComments` (`idRouteComment` , `idRoute` , `idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


ALTER TABLE  `Users` ADD  `online` TINYINT NOT NULL DEFAULT  '0';