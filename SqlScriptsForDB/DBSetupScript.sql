DROP SCHEMA IF EXISTS `effi_app`;
CREATE SCHEMA `effi_app`;
USE `effi_app`;
SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `users_roles`;

-- app users table
CREATE TABLE `users` (
	`id` int NOT NULL AUTO_INCREMENT,
    `first_name` varchar(50) NOT NULL,
	`last_name` varchar(50) NOT NULL,
	`email` varchar(50) NOT NULL,
    `password` varchar(68) NOT NULL,
	`enabled` tinyint NOT NULL,
	PRIMARY KEY (`id`)
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