# ************************************************************
# Sequel Pro SQL dump
# Version 4499
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.19)
# Database: shop
# Generation Time: 2020-08-08 04:56:25 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table hibernate_sequence
# ------------------------------------------------------------

DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;

INSERT INTO `hibernate_sequence` (`next_val`)
VALUES
	(1);

/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_admin
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_admin`;

CREATE TABLE `t_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_admin` WRITE;
/*!40000 ALTER TABLE `t_admin` DISABLE KEYS */;

INSERT INTO `t_admin` (`id`, `password`, `username`)
VALUES
	(1,'xiaod','小D课堂'),
	(2,'daniel','daniel');

/*!40000 ALTER TABLE `t_admin` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_announcement
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_announcement`;

CREATE TABLE `t_announcement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `createAdmin_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKslwp3gcgudths92bk0kbak06t` (`createAdmin_id`),
  CONSTRAINT `FKslwp3gcgudths92bk0kbak06t` FOREIGN KEY (`createAdmin_id`) REFERENCES `t_admin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table t_coupon
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_coupon`;

CREATE TABLE `t_coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '优惠券码',
  `pic_url` varchar(255) NOT NULL DEFAULT '' COMMENT '优惠券图',
  `reduce_amount` int(11) NOT NULL DEFAULT '0' COMMENT '所减金额',
  `achieve_amount` int(11) NOT NULL DEFAULT '0' COMMENT '达到满减资格金额',
  `stock` bigint(20) NOT NULL DEFAULT '0' COMMENT '库存，当库存为0不可领取',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '优惠券名称',
  `create_time` datetime DEFAULT NULL,
  `status` int(1) unsigned NOT NULL COMMENT '状态为0表示可用，1为不可用',
  `start_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '生效开始时间',
  `end_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '生效结束时间',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `inx_code` (`code`),
  KEY `inx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_coupon` WRITE;
/*!40000 ALTER TABLE `t_coupon` DISABLE KEYS */;

INSERT INTO `t_coupon` (`id`, `code`, `pic_url`, `reduce_amount`, `achieve_amount`, `stock`, `title`, `create_time`, `status`, `start_time`, `end_time`, `createTime`)
VALUES
	(1,'10086','/images/l_cou01.png',50,500,1000,'满500减50代金券','2019-06-07 12:25:05',0,'2019-01-07 12:25:05','2019-10-07 12:25:05',NULL),
	(2,'9527','/images/l_cou02.png',300,4000,1000,'满4000减300代金券','2019-06-07 12:25:05',0,'2019-07-21 01:25:40','2019-10-07 12:25:05',NULL),
	(4,'9528','/images/l_cou02.png',300,4000,1000,'满4000减300代金券','2019-06-07 12:25:05',0,'2019-07-21 01:25:39','2019-10-07 12:25:05',NULL),
	(9,'1222c75c-1f8c-4c83-94e5-aba934ef59e3','/images/l_cou02.png',20,500,100,'测试coupon','2019-07-08 21:18:00',0,'2019-07-26 00:11:35','2019-07-26 00:11:35',NULL),
	(200010,'1222c75c-1f8c-4c83-94e-aba934ef59e3','/images/l_cou03.png',20,500,100,'测试coupon','2019-07-08 21:18:00',0,'2019-07-26 00:11:35','2019-07-26 00:11:35',NULL),
	(200011,'1222c75c-1f8c-4c83-4e-aba934ef59e3','/images/l_cou04.png',20,500,100,'测试coupon','2019-07-08 21:18:00',0,'2019-07-26 00:11:35','2019-07-26 00:11:35',NULL),
	(200012,'1222c75c-f8c-4c83-4e-aba934ef59e3','/images/l_cou05.png',20,500,100,'测试coupon','2019-07-08 21:18:00',0,'2019-07-26 00:11:35','2019-07-26 00:11:35',NULL),
	(200013,'222c75c-f8c-4c83-4e-aba934ef59e3','/images/l_cou06.png',20,500,100,'测试coupon','2019-07-08 21:18:00',0,'2019-07-26 00:11:35','2019-07-26 00:11:35',NULL),
	(200014,'222c75c-f8c-4c83-4e-aba934ef59e','/images/l_cou07.png',20,500,100,'测试coupon','2019-07-08 21:18:00',0,'2019-07-26 00:11:35','2019-07-26 00:11:35',NULL),
	(200015,'222c75c-f8c-4c83-4e-aba934ef5','/images/l_cou08.png',20,500,100,'测试coupon','2019-07-08 21:18:00',0,'2019-07-26 00:11:35','2019-07-26 00:11:35',NULL),
	(200017,'222c75c-f8c-4c83-4e-aba93ef5','/images/l_cou09.png',20,500,100,'测试coupon','2019-07-08 21:18:00',0,'2019-07-26 00:11:35','2019-07-26 00:11:35',NULL),
	(200018,'22c75c-f8c-4c83-4e-aba93ef5','/images/l_cou09.png',20,500,100,'测试coupon','2019-07-08 21:18:00',0,'2019-07-26 00:11:35','2019-07-26 00:11:35',NULL),
	(200019,'22c75-f8c-4c83-4e-aba93ef5','/images/l_cou09.png',20,500,100,'测试coupon','2019-07-08 21:18:00',0,'2019-07-26 00:11:35','2019-07-26 00:11:35',NULL),
	(200020,'75-f8c-4c83-4e-aba93ef5','/images/l_cou09.png',20,500,100,'测试coupon','2019-07-08 21:18:00',0,'2019-07-26 00:11:35','2019-07-26 00:11:35',NULL),
	(200021,'75-f8c-4c83-4e-aba9ef5','/images/l_cou09.png',20,500,100,'测试coupon','2019-07-08 21:18:00',0,'2019-07-26 00:11:35','2019-07-26 00:11:35',NULL);

/*!40000 ALTER TABLE `t_coupon` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_coupon1
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_coupon1`;

CREATE TABLE `t_coupon1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(128) NOT NULL DEFAULT '' COMMENT '优惠券码',
  `pic_url` varchar(255) NOT NULL DEFAULT '' COMMENT '优惠券图',
  `achieve_amount` int(11) NOT NULL DEFAULT '0' COMMENT '达到满减资格金额',
  `reduce_amount` int(11) NOT NULL DEFAULT '0' COMMENT '所减金额',
  `stock` int(11) NOT NULL DEFAULT '0' COMMENT '库存，当库存为0不可领取',
  `title` varchar(64) NOT NULL DEFAULT '' COMMENT '优惠券名称',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态为0表示可用，1为不可用',
  `create_time` datetime DEFAULT NULL,
  `start_time` datetime DEFAULT NULL COMMENT '生效开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '生效结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券定义表';

LOCK TABLES `t_coupon1` WRITE;
/*!40000 ALTER TABLE `t_coupon1` DISABLE KEYS */;

INSERT INTO `t_coupon1` (`id`, `code`, `pic_url`, `achieve_amount`, `reduce_amount`, `stock`, `title`, `status`, `create_time`, `start_time`, `end_time`)
VALUES
	(1,'10086','/images/l_cou01.png',500,50,1000,'?500?50???',0,'2019-06-07 12:25:05','2019-01-07 12:25:05','2029-06-07 12:25:05'),
	(2,'9527','/images/l_cou02.png',4000,300,1000,'?4000?300???',0,'2019-06-07 12:25:05','2019-02-07 12:25:05','2029-06-07 12:25:05'),
	(4,'9528','/images/l_cou02.png',4000,300,1000,'?4000?300???',0,'2019-06-07 12:25:05','2029-06-07 12:25:05','2030-06-07 12:25:05');

/*!40000 ALTER TABLE `t_coupon1` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_news
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_news`;

CREATE TABLE `t_news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `inputUser_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKspe5cxeewkudeg10usq127fji` (`inputUser_id`),
  CONSTRAINT `FKspe5cxeewkudeg10usq127fji` FOREIGN KEY (`inputUser_id`) REFERENCES `t_admin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_news` WRITE;
/*!40000 ALTER TABLE `t_news` DISABLE KEYS */;

INSERT INTO `t_news` (`id`, `content`, `title`, `create_time`, `inputUser_id`)
VALUES
	(1,'?????5?','?????????????','2019-06-07 08:00:00',2);

/*!40000 ALTER TABLE `t_news` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_order
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_order`;

CREATE TABLE `t_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `order_number` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `user_address` int(11) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `confirmTime` datetime DEFAULT NULL,
  `consignee` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `finalPrice` double DEFAULT NULL,
  `orderNumber` varchar(255) DEFAULT NULL,
  `payTime` datetime DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `shipTime` datetime DEFAULT NULL,
  `totalPrice` double DEFAULT NULL,
  `zipcode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_75ffb6s55q5aibdhqs2unu49q` (`user_id`),
  KEY `FK_9a6eb7iu2gbn1628cud5w836w` (`user_address`),
  CONSTRAINT `FK_9a6eb7iu2gbn1628cud5w836w` FOREIGN KEY (`user_address`) REFERENCES `t_useraddress` (`id`),
  CONSTRAINT `FKho2r4qgj3txpy8964fnla95ub` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_order` WRITE;
/*!40000 ALTER TABLE `t_order` DISABLE KEYS */;

INSERT INTO `t_order` (`id`, `create_time`, `order_number`, `status`, `user_id`, `user_address`, `address`, `confirmTime`, `consignee`, `createTime`, `finalPrice`, `orderNumber`, `payTime`, `phone`, `shipTime`, `totalPrice`, `zipcode`)
VALUES
	(1,NULL,NULL,1,2,NULL,'',NULL,'','2019-06-07 10:37:33',369,'20190607103789','2019-06-08 21:11:43','',NULL,369,''),
	(2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2019-06-07 12:25:05',NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(3,NULL,NULL,1,2,NULL,'',NULL,'','2019-06-08 21:11:37',200,'20190608211190','2019-06-28 22:31:40','',NULL,200,''),
	(4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2019-06-12 00:04:17',NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(5,NULL,NULL,0,2,NULL,'',NULL,'','2019-06-28 21:50:39',123,'20190628215036',NULL,'',NULL,123,''),
	(6,NULL,NULL,0,2,NULL,'??',NULL,'??','2019-06-28 22:26:45',3456,'20190628222651',NULL,'15820681234',NULL,3456,'10000'),
	(7,NULL,NULL,0,2,NULL,'???',NULL,'???','2019-06-29 15:50:02',6666,'20190629155007',NULL,'10086100861',NULL,6666,'20000'),
	(8,NULL,NULL,0,2,NULL,'??',NULL,'??,','2019-06-29 15:59:19',0,'20190629155974',NULL,'15820681234',NULL,0,'10000'),
	(9,NULL,NULL,1,2,NULL,'???',NULL,'???,','2019-06-29 16:00:22',6666,'20190629160049','2019-06-29 23:44:49','10086100861',NULL,6666,'20000'),
	(10,NULL,NULL,1,2,NULL,'???',NULL,'???,','2019-06-29 16:02:06',0,'20190629160255','2019-06-29 23:44:47','10086100861',NULL,0,'20000'),
	(11,NULL,NULL,3,2,NULL,'???',NULL,'???,','2019-06-29 16:02:56',0,'20190629160299',NULL,'10086100861',NULL,0,'20000'),
	(12,NULL,NULL,3,2,NULL,'???',NULL,'???,','2019-06-29 16:03:08',0,'20190629160311',NULL,'10086100861',NULL,0,'20000'),
	(13,NULL,NULL,0,2,NULL,'??',NULL,'??','2019-06-29 16:42:03',3283,'20190629164209',NULL,'15820681234',NULL,3333,'10000'),
	(14,NULL,NULL,0,2,NULL,'??',NULL,'??','2019-06-29 16:49:52',3283,'20190629164920',NULL,'15820681234',NULL,3333,'10000'),
	(15,NULL,NULL,0,2,NULL,'??',NULL,'??','2019-06-29 16:53:04',3283,'20190629165356',NULL,'10086100861',NULL,3333,'20000'),
	(16,NULL,NULL,0,2,NULL,'??',NULL,'??','2019-06-29 17:30:45',3406,'20190629173042',NULL,'15820681234',NULL,3456,'10000'),
	(17,NULL,NULL,0,2,NULL,'??',NULL,'??','2019-06-29 23:44:32',1196,'20190629234491',NULL,'15820681234',NULL,1246,'10000'),
	(18,NULL,NULL,0,2,NULL,'',NULL,'','2019-06-29 23:46:49',3406,'20190629234678',NULL,'',NULL,3456,''),
	(19,NULL,NULL,0,2,NULL,'??',NULL,'??','2019-06-29 23:57:57',3406,'20190629235738',NULL,'15820681234',NULL,3456,'10000'),
	(21,NULL,NULL,3,1,NULL,'',NULL,'','2019-07-26 00:13:59',3406,'20190726001328','2019-07-26 08:51:38','',NULL,3456,''),
	(22,NULL,NULL,1,1,NULL,'',NULL,'','2019-07-26 08:51:26',20549,'20190726085196','2019-07-26 22:28:43','',NULL,20599,''),
	(23,NULL,NULL,1,1,NULL,'',NULL,'','2019-07-26 22:28:04',66613,'20190726222803','2019-07-26 22:28:47','',NULL,66663,''),
	(24,NULL,NULL,1,1,NULL,'',NULL,'','2019-07-26 22:29:21',3283,'20190726222995','2019-07-26 22:29:24','',NULL,3333,''),
	(25,NULL,NULL,1,1,NULL,'',NULL,'','2019-08-03 15:44:39',3406,'20190803154449','2019-08-03 15:44:51','',NULL,3456,'');

/*!40000 ALTER TABLE `t_order` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_orderitem
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_orderitem`;

CREATE TABLE `t_orderitem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quantity` int(11) NOT NULL DEFAULT '0',
  `order_id` int(11) DEFAULT NULL,
  `product` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_n7j2urgoicw0qa2b5s2pidind` (`order_id`),
  KEY `FK_cxxgytqnjjrpm4x7k2grbm6iu` (`product`),
  KEY `FK2yx4lqm9mh15mysa9kppvf77r` (`product_id`),
  CONSTRAINT `FK2yx4lqm9mh15mysa9kppvf77r` FOREIGN KEY (`product_id`) REFERENCES `t_product` (`id`),
  CONSTRAINT `FK_n7j2urgoicw0qa2b5s2pidin1` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`),
  CONSTRAINT `FKj435mnwwxw5wci0t6xi15ddxk` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_orderitem` WRITE;
/*!40000 ALTER TABLE `t_orderitem` DISABLE KEYS */;

INSERT INTO `t_orderitem` (`id`, `quantity`, `order_id`, `product`, `product_id`)
VALUES
	(1,3,1,NULL,1),
	(2,2,2,NULL,1),
	(3,2,2,NULL,2),
	(4,2,2,NULL,3),
	(5,2,2,NULL,4),
	(6,2,2,NULL,5),
	(7,1,3,NULL,5),
	(8,2,4,NULL,1),
	(9,2,4,NULL,2),
	(10,2,4,NULL,3),
	(11,2,4,NULL,4),
	(12,2,4,NULL,5),
	(15,1,5,NULL,1),
	(16,1,6,NULL,1),
	(17,1,6,NULL,2),
	(18,2,7,NULL,2),
	(19,2,9,NULL,2),
	(20,1,13,NULL,2),
	(21,1,14,NULL,2),
	(22,1,15,NULL,2),
	(23,1,16,NULL,1),
	(24,1,16,NULL,2),
	(25,2,17,NULL,1),
	(26,1,17,NULL,3),
	(27,1,18,NULL,1),
	(28,1,18,NULL,2),
	(29,1,19,NULL,1),
	(30,1,19,NULL,2),
	(31,1,21,NULL,1),
	(32,1,21,NULL,2),
	(33,1,22,NULL,4),
	(34,1,22,NULL,25),
	(35,1,22,NULL,26),
	(36,1,22,NULL,27),
	(37,3,23,NULL,17),
	(38,2,23,NULL,2),
	(39,1,24,NULL,2),
	(40,1,25,NULL,1),
	(41,1,25,NULL,2);

/*!40000 ALTER TABLE `t_orderitem` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_payment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_payment`;

CREATE TABLE `t_payment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table t_picture
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_picture`;

CREATE TABLE `t_picture` (
  `id` int(11) NOT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `updateAdmin_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf820vwoskrv05yxj80mlbj226` (`updateAdmin_id`),
  CONSTRAINT `FKf820vwoskrv05yxj80mlbj226` FOREIGN KEY (`updateAdmin_id`) REFERENCES `t_admin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table t_product
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_product`;

CREATE TABLE `t_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `pic_url` varchar(255) DEFAULT NULL,
  `point` int(11) DEFAULT NULL,
  `stock` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `masterPic_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKknr9ckik7tms7787w9nkpis8g` (`masterPic_id`),
  CONSTRAINT `FKknr9ckik7tms7787w9nkpis8g` FOREIGN KEY (`masterPic_id`) REFERENCES `t_picture` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_product` WRITE;
/*!40000 ALTER TABLE `t_product` DISABLE KEYS */;

INSERT INTO `t_product` (`id`, `code`, `model`, `note`, `pic_url`, `point`, `stock`, `title`, `createTime`, `masterPic_id`)
VALUES
	(1,'2343434','2a-dd-1s','???????????????????????????????????????????????????????','/images/l_pro01.png',123,3,'????????','2019-06-07 12:25:05',NULL),
	(2,'sdfsdf','df','???','/images/l_pro02.png',3333,123,'????','2019-06-07 12:25:05',NULL),
	(3,'sdfdsf','fddf','????','/images/l_pro03.png',1000,222,'??????','2019-06-07 12:25:05',NULL),
	(4,'BMC-SLR01-54','54',NULL,'/images/l_pro04.png',19999,200,'????1','2019-06-07 12:25:05',NULL),
	(5,'KDF-SD1-200','400CM',NULL,'/images/l_pro05.png',200,200,'????2','2019-06-07 12:25:05',NULL),
	(6,'KDF-SD1-200','400CM',NULL,'/images/l_pro06.png',200,200,'????1','2019-06-07 12:25:05',NULL),
	(7,'KDF-SD1-200','400CM',NULL,'/images/l_pro07.png',200,200,'????2','2019-06-07 12:25:05',NULL),
	(8,'KDF-SD1-200','400CM',NULL,'/images/l_pro08.png',200,200,'????3','2019-06-07 12:25:05',NULL),
	(9,'KDF-SD1-200','400CM',NULL,'/images/l_pro09.png',200,200,'????4','2019-06-07 12:25:05',NULL),
	(10,'KDF-SD1-200','400CM',NULL,'/images/l_pro10.png',200,200,'????5','2019-06-07 12:25:05',NULL),
	(11,'KDF-SD1-200','400CM',NULL,'/images/l_pro11.png',200,200,'????6','2019-06-07 12:25:05',NULL),
	(12,'KDF-SD1-200','400CM',NULL,'/images/l_pro12.jpg',200,200,'????2','2019-06-07 12:25:05',NULL),
	(13,'KDF-SD1-200','400CM',NULL,'/images/l_pro15.jpg',200,200,'????2','2019-06-07 12:25:05',NULL),
	(14,'KDF-SD1-200','400CM',NULL,'/images/l_pro16.jpg',200,200,'????1','2019-06-07 12:25:05',NULL),
	(15,'BMC-SLR01-54','54',NULL,'/images/l_pro14.jpg',19999,200,'????1','2019-06-07 12:25:05',NULL),
	(16,'KDF-SD1-200','400CM',NULL,'/images/l_pro15.jpg',200,200,'????2','2019-06-07 12:25:05',NULL),
	(17,'BMC-SLR01-54','54',NULL,'/images/l_pro13.jpg',19999,200,'????1','2019-06-07 12:25:05',NULL),
	(18,'KDF-SD1-200','400CM',NULL,'/images/l_pro15.jpg',200,200,'????2','2019-06-07 12:25:05',NULL),
	(19,'KDF-SD1-200','400CM',NULL,'/images/l_pro17.jpg',200,200,'????1','2019-06-07 12:25:05',NULL),
	(20,'KDF-SD1-200','400CM',NULL,'/images/l_pro07.png',200,200,'????2','2019-06-07 12:25:05',NULL),
	(21,'KDF-SD1-200','400CM',NULL,'/images/l_pro18.jpg',200,200,'????3','2019-06-07 12:25:05',NULL),
	(22,'KDF-SD1-200','400CM',NULL,'/images/l_pro19.jpg',200,200,'????4','2019-06-07 12:25:05',NULL),
	(23,'KDF-SD1-200','400CM',NULL,'/images/l_pro110.jpg',200,200,'????5','2019-06-07 12:25:05',NULL),
	(24,'KDF-SD1-200','400CM',NULL,'/images/l_pro111.jpg',200,200,'????6','2019-06-07 12:25:05',NULL),
	(25,'KDF-SD1-200','400CM',NULL,'/images/l_pro112.jpg',200,200,'????2','2019-06-07 12:25:05',NULL),
	(26,'KDF-SD1-200','400CM',NULL,'/images/l_pro113.jpg',200,200,'????2','2019-06-07 12:25:05',NULL),
	(27,'KDF-SD1-200','400CM',NULL,'/images/l_pro114.jpg',200,200,'????2','2019-06-07 12:25:05',NULL),
	(28,'KDF-SD1-200','400CM',NULL,'/images/l_pro115.jpg',200,200,'????2','2019-06-07 12:25:05',NULL),
	(29,'KDF-SD1-200','400CM',NULL,'/images/l_pro116.jpg',200,200,'????2','2019-06-07 12:25:05',NULL),
	(30,'KDF-SD1-200','400CM',NULL,'/images/l_pro117.jpg',200,200,'????2','2019-06-07 12:25:05',NULL),
	(31,'KDF-SD1-200','400CM',NULL,'/images/l_pro118.jpg',200,200,'????2','2019-06-07 12:25:05',NULL),
	(32,'KDF-SD1-200','400CM',NULL,'/images/l_pro119.jpg',200,200,'????2','2019-06-07 12:25:05',NULL),
	(33,'KDF-SD1-200','400CM',NULL,'/images/l_pro120.jpg',200,200,'????2','2019-06-07 12:25:05',NULL);

/*!40000 ALTER TABLE `t_product` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_product_t_picture
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_product_t_picture`;

CREATE TABLE `t_product_t_picture` (
  `Product_id` int(11) NOT NULL,
  `slavePic_id` int(11) NOT NULL,
  KEY `FKln4qvwbfxoiypk9mgwyedmtl1` (`slavePic_id`),
  KEY `FKmixcjvbg3qcrp0reaqha7ujk` (`Product_id`),
  CONSTRAINT `FKln4qvwbfxoiypk9mgwyedmtl1` FOREIGN KEY (`slavePic_id`) REFERENCES `t_picture` (`id`),
  CONSTRAINT `FKmixcjvbg3qcrp0reaqha7ujk` FOREIGN KEY (`Product_id`) REFERENCES `t_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table t_producttype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_producttype`;

CREATE TABLE `t_producttype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_producttype` WRITE;
/*!40000 ALTER TABLE `t_producttype` DISABLE KEYS */;

INSERT INTO `t_producttype` (`id`, `name`)
VALUES
	(1,'????'),
	(2,'????'),
	(3,'????'),
	(4,'????'),
	(5,'????'),
	(6,'????');

/*!40000 ALTER TABLE `t_producttype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_remember
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_remember`;

CREATE TABLE `t_remember` (
  `id` varchar(255) NOT NULL,
  `addTime` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6uc4b3e4xdabyyiamthsb2bqa` (`user_id`),
  CONSTRAINT `FK6uc4b3e4xdabyyiamthsb2bqa` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`),
  CONSTRAINT `t_remember_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `t_remember` WRITE;
/*!40000 ALTER TABLE `t_remember` DISABLE KEYS */;

INSERT INTO `t_remember` (`id`, `addTime`, `user_id`)
VALUES
	('6f18fd80-1d92-4021-8a5c-6a28e21ba66a','2019-06-28 21:44:08',2),
	('7d49a0c9-50c1-496c-9cd9-1cab40306cba','2019-07-20 11:11:51',1),
	('9702147a-b3d4-4a9e-be06-63868e4171f8','2019-06-08 12:41:14',2),
	('98e2f67d-c322-40dc-96aa-662624d13a7e','2019-06-11 08:58:51',2),
	('9be1447c-1e28-44b0-9b96-3df07b2bb8a0','2019-07-26 00:11:49',1),
	('b329619d-1b2c-4c25-a4fe-8f5079eddb1a','2019-07-05 08:50:45',2),
	('b333547d-3aae-4c9b-b920-087f52f27b20','2019-06-07 10:37:05',2),
	('b62e3361-1df2-4366-9436-d70e54d290d5','2019-08-04 20:23:30',1),
	('d06ddf6b-132b-457d-97f4-f069ea4a1fb5','2019-06-29 23:42:56',2);

/*!40000 ALTER TABLE `t_remember` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL DEFAULT '' COMMENT '??',
  `address` varchar(50) NOT NULL DEFAULT '' COMMENT '??',
  `password` varchar(64) NOT NULL DEFAULT '' COMMENT '??',
  `phone` char(11) DEFAULT NULL COMMENT '????',
  `point` int(11) DEFAULT NULL COMMENT '???',
  `remark` varchar(50) NOT NULL DEFAULT '',
  `tel_phone` char(11) NOT NULL DEFAULT '' COMMENT '????',
  `username` varchar(15) NOT NULL DEFAULT '' COMMENT '????',
  `zip_code` varchar(6) NOT NULL DEFAULT '' COMMENT '????',
  `balance` bigint(20) DEFAULT NULL COMMENT '????',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='???????';

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;

INSERT INTO `t_user` (`id`, `account`, `address`, `password`, `phone`, `point`, `remark`, `tel_phone`, `username`, `zip_code`, `balance`)
VALUES
	(1,'daniel','????','daniel','12580125800',1000,'admin','95279527','daniel','10000',10000),
	(2,'root','????','root','12580125800',1000,'admin','95279527','root','10000',10000);

/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_user_coupon
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_user_coupon`;

CREATE TABLE `t_user_coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_coupon_code` varchar(255) DEFAULT NULL,
  `pic_url` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `coupon_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `status` int(1) unsigned DEFAULT '0' COMMENT '?????0??????1????????0',
  `order_id` int(11) DEFAULT NULL COMMENT '??id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_user_coupon` WRITE;
/*!40000 ALTER TABLE `t_user_coupon` DISABLE KEYS */;

INSERT INTO `t_user_coupon` (`id`, `user_coupon_code`, `pic_url`, `createTime`, `coupon_id`, `user_id`, `status`, `order_id`, `create_time`)
VALUES
	(1,'214522434','/images/l_cou01.png','2019-06-07 12:25:05',1,1,1,25,NULL),
	(4,'722632ddb06147fb800cea742ee3c8ea','/images/l_cou01.png','2019-06-11 08:44:42',1,1,1,23,NULL),
	(5,'b247c3a2aaaa4d6fb23e076a7ab424c2','/images/l_cou01.png','2019-06-11 09:01:12',1,1,0,NULL,NULL),
	(6,'d0c9fc4fa2c4442c84c0b3726fd32e0e','/images/l_cou01.png','2019-06-11 09:03:42',1,1,0,NULL,NULL),
	(22,'20052fab342043eba6035449b0dd958e','/images/l_cou02.png','2019-06-28 21:47:27',2,1,0,NULL,NULL),
	(23,'20052fab342043eba6035449b0dd958e','/images/l_cou02.png','2019-06-28 21:47:27',2,1,0,NULL,NULL),
	(24,'20052fab342043eba6035449b0dd958e','/images/l_cou02.png','2019-06-28 21:47:27',2,1,0,NULL,NULL),
	(26,'facc47aaba6c41bfbc1f15a55b757ce9','/images/l_cou02.png','2019-06-29 15:16:18',2,1,0,NULL,NULL),
	(27,'a19f93640dfb45dd9fad54d25d733e54','/images/l_cou02.png','2019-06-29 15:17:19',2,1,0,NULL,NULL),
	(28,'b8b307f1fc204365b0b700f642883206','/images/l_cou01.png','2019-06-29 15:22:47',1,2,1,19,NULL),
	(29,'c982c3a2fa2e495697f32246efeeb1af','/images/l_cou01.png','2019-06-29 15:24:15',1,2,1,16,NULL),
	(30,'8e74d5c9639b4f8ab922a934ac6c5b21','/images/l_cou01.png','2019-06-29 15:44:31',1,2,0,NULL,NULL),
	(31,'cfc842d324064a8ab592f599e1798f7e','/images/l_cou02.png','2019-06-29 16:13:27',2,2,0,NULL,NULL),
	(32,'fe3cee8858664575a503bf8a5cea5298','/images/l_cou01.png','2019-06-29 17:08:28',1,2,NULL,NULL,NULL),
	(33,'99a7d9814d9844ccb8e14689cd3c72fc','/images/l_cou02.png','2019-06-29 17:08:30',2,2,NULL,NULL,NULL),
	(34,'58ab96fa50d24b3bbd2dc9f8030d8c4b','/images/l_cou01.png','2019-06-29 23:43:55',1,2,NULL,NULL,NULL),
	(35,'191cbdefcb2846129d29f1a4208e32c6','/images/l_cou02.png','2019-06-29 23:43:57',2,2,NULL,NULL,NULL),
	(36,'c50663afb7754227be18f372ddaba7b2','/images/l_cou01.png','2019-06-29 23:47:32',1,2,NULL,NULL,NULL),
	(37,'b43e7c8d65a144fdb6c8db8de2e723b6','/images/l_cou01.png','2019-06-29 23:58:13',1,2,NULL,NULL,NULL),
	(38,'f6058984600f4dcc95a44e4504db222f','/images/l_cou02.png','2019-06-29 23:58:16',2,2,NULL,NULL,NULL),
	(39,'0a58bc8d748842e5abb2e5ae0d36fe0e','/images/l_cou02.png','2019-07-05 08:51:42',2,2,NULL,NULL,NULL),
	(40,'46dc605eca394836ac8457671e12b47e','/images/l_cou01.png',NULL,1,2,NULL,NULL,'2019-07-13 10:35:01'),
	(41,'f6ad5789e30e48dbb6d7383f0accafb7','/images/l_cou01.png',NULL,1,1,NULL,NULL,'2019-07-26 00:11:53'),
	(42,'be072e162e7d412bad4173b308a9c233','/images/l_cou01.png',NULL,1,1,NULL,NULL,'2019-07-26 08:51:49'),
	(43,'9c6704e09c8d4a62906b16b9cdbc6395','/images/l_cou01.png',NULL,1,1,NULL,NULL,'2019-07-26 22:28:54'),
	(44,'ce0293b35e0a46d6937bd5d5b53cdf06','/images/l_cou02.png',NULL,2,1,NULL,NULL,'2019-07-26 22:28:56'),
	(45,'607260439864147968','/images/l_cou01.png',NULL,1,1234,0,10086,'2019-08-03 17:16:11'),
	(46,'607333165165445120','/images/l_cou01.png',NULL,1,1,0,NULL,'2019-08-03 22:05:10'),
	(47,'607352774849462272','/images/l_cou01.png',NULL,1,1,0,NULL,'2019-08-03 23:23:06'),
	(48,'607353215242993664','/images/l_cou02.png',NULL,2,1,0,NULL,'2019-08-03 23:24:51');

/*!40000 ALTER TABLE `t_user_coupon` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_user_coupon1
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_user_coupon1`;

CREATE TABLE `t_user_coupon1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_coupon_code` varchar(128) NOT NULL DEFAULT '' COMMENT '??????',
  `pic_url` varchar(255) NOT NULL DEFAULT '' COMMENT '????',
  `coupon_id` int(11) NOT NULL DEFAULT '0' COMMENT 't_coupon???ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '?????id',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '???0??????1????',
  `order_id` int(11) NOT NULL DEFAULT '0' COMMENT '??t_order???',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='?????';

LOCK TABLES `t_user_coupon1` WRITE;
/*!40000 ALTER TABLE `t_user_coupon1` DISABLE KEYS */;

INSERT INTO `t_user_coupon1` (`id`, `user_coupon_code`, `pic_url`, `coupon_id`, `user_id`, `status`, `order_id`, `create_time`)
VALUES
	(1,'','',0,0,0,0,NULL),
	(2,'','',0,0,0,0,NULL),
	(3,'','',0,0,0,0,NULL),
	(4,'','',0,0,0,0,NULL);

/*!40000 ALTER TABLE `t_user_coupon1` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_user1
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_user1`;

CREATE TABLE `t_user1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `password` varchar(14) NOT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `point` int(11) DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL,
  `tel_phone` varchar(11) DEFAULT NULL,
  `username` varchar(15) DEFAULT NULL,
  `zip_code` varchar(6) DEFAULT NULL,
  `balance` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_user1` WRITE;
/*!40000 ALTER TABLE `t_user1` DISABLE KEYS */;

INSERT INTO `t_user1` (`id`, `account`, `address`, `password`, `phone`, `point`, `remark`, `tel_phone`, `username`, `zip_code`, `balance`)
VALUES
	(2,'daniel','daniel','daniel','1',1,'1','1','daniel','1',1);

/*!40000 ALTER TABLE `t_user1` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_useraddress
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_useraddress`;

CREATE TABLE `t_useraddress` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `consignee` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `zipcode` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `inx_user` (`user_id`),
  CONSTRAINT `FKivjwmwb9xngrc6ic856ryrb57` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_useraddress` WRITE;
/*!40000 ALTER TABLE `t_useraddress` DISABLE KEYS */;

INSERT INTO `t_useraddress` (`id`, `address`, `consignee`, `phone`, `zipcode`, `user_id`)
VALUES
	(1,'??','??','15820681234','10000',2),
	(2,'??','??','10086100861','20000',2),
	(11,'','','','',2),
	(14,'','','','',1),
	(15,'','','','',1),
	(16,'','','','',1),
	(17,'','','','',1),
	(18,'','','','',1);

/*!40000 ALTER TABLE `t_useraddress` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
