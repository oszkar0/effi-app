DROP SCHEMA IF EXISTS `effi_app`;
CREATE SCHEMA `effi_app`;
USE `effi_app`;
SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `users_roles`;
DROP TABLE IF EXISTS `company`;
DROP TABLE IF EXISTS `tasks`;

-- table to store companies using app
CREATE TABLE `company` (
	`id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- app users table
CREATE TABLE `users` (
	`id` int NOT NULL AUTO_INCREMENT,
    `first_name` varchar(50) NOT NULL,
	`last_name` varchar(50) NOT NULL,
	`email` varchar(50) NOT NULL,
    `password` varchar(60) NOT NULL,
	`enabled` tinyint NOT NULL,
    `company_id` int NOT NULL,
	
    PRIMARY KEY (`id`),
    
	CONSTRAINT `FK_USER_COMPANY` FOREIGN KEY (`company_id`)
    REFERENCES `company` (`id`)
	ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- available roles
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- table to describe users having roles
CREATE TABLE `users_roles` (
	`user_id` int NOT NULL,
	`role_id` int NOT NULL,
  
	PRIMARY KEY (`user_id`,`role_id`),
  
	CONSTRAINT `FK_USER` FOREIGN KEY (`user_id`) 
	REFERENCES `users` (`id`) 
	ON DELETE NO ACTION ON UPDATE NO ACTION,
  
	CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`) 
	REFERENCES `role` (`id`) 
	ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

    
-- users tasks 
CREATE TABLE `tasks` (
	`id` int NOT NULL AUTO_INCREMENT,
    `title` varchar(150) NOT NULL,
    `description` varchar(500) NOT NULL,
    `user_id` int NOT NULL,
    `status` int NOT NULL,
    `deadline` date NOT NULL,
	
    PRIMARY KEY(`id`),
    
	CONSTRAINT `FK_USER_TASK` FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
	ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


-- insert roles
INSERT INTO `role` (name)
VALUES 
('ROLE_EMPLOYEE'),('ROLE_MANAGER'),('ROLE_ADMIN');



