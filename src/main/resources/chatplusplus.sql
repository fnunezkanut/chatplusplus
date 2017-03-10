/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : chatplusplus

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2017-03-04 14:24:57
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages` (
  `id`         INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `comment`    TEXT CHARACTER SET utf8,
  `user_id`    INT(10) UNSIGNED          DEFAULT '0',
  `dt_created` DATETIME                  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `s_user_id` (`user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- ----------------------------
-- Records of messages
-- ----------------------------

-- ----------------------------
-- Table structure for threads
-- ----------------------------
DROP TABLE IF EXISTS `threads`;
CREATE TABLE `threads` (
  `id`          INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title`       VARCHAR(255)
                CHARACTER SET utf8        DEFAULT NULL,
  `dt_created`  DATETIME                  DEFAULT NULL,
  `dt_updated`  DATETIME                  DEFAULT NULL,
  `customer_id` INT(10) UNSIGNED          DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = latin1;

-- ----------------------------
-- Records of threads
-- ----------------------------
INSERT INTO `threads` VALUES ('1', 'test', '2017-02-15 19:26:57', '2017-02-20 11:29:16', NULL, NULL);
INSERT INTO `threads` VALUES ('4', 'test3', '2017-02-20 10:55:18', '2017-02-20 11:28:36', NULL, NULL);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id`         INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(255)
               CHARACTER SET utf8        DEFAULT NULL,
  `email`      VARCHAR(255)              DEFAULT NULL,
  `passhash`   VARCHAR(32)               DEFAULT NULL,
  `dt_created` DATETIME                  DEFAULT NULL,
  `is_support` TINYINT(1) UNSIGNED       DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = latin1;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users`
VALUES ('1', 'Admin', 'admin@example.com', '5F4DCC3B5AA765D61D8327DEB882CF99', '2017-03-29 19:44:31', '1');
