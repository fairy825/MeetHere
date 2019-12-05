﻿# Host: 127.0.0.1  (Version: 5.5.15)
# Date: 2019-12-03 18:03:39
# Generator: MySQL-Front 5.3  (Build 4.269)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "admin"
#

DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Data for table "admin"
#

INSERT INTO `admin` VALUES (1,'root','admin'),(2,'meethere','meethere'),(3,'a','a');

#
# Structure for table "district"
#

DROP TABLE IF EXISTS `district`;
CREATE TABLE `district` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

#
# Data for table "district"
#

INSERT INTO `district` VALUES (1,'t'),(2,'桃'),(3,'桃浦'),(4,'a'),(16,'桃园');

#
# Structure for table "news"
#

DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `content` varchar(4000) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

#
# Data for table "news"
#

INSERT INTO `news` VALUES (1,'test','第一条新闻','2019-10-27 10:55:53'),(2,'test','2222','2019-10-27 10:55:53'),(3,'test3','第3条新闻','2019-10-27 11:47:19'),(4,'sg','asdgfg','2019-10-27 10:55:53');

#
# Structure for table "user"
#

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(21) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `faceImage` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

#
# Data for table "user"
#

INSERT INTO `user` VALUES (1,'a','a','12345678','111111@mh.com','first',NULL),(2,'aaa','b',NULL,NULL,'aaa',NULL),(3,'c','c',NULL,NULL,'c',NULL),(4,'d','d',NULL,NULL,'d',NULL),(25,'aa','QSS8CpM1wn8IbyS6IHpJEg==','11','111','aa','img/faceImage/微信图片_20190906012034.jpg');

#
# Structure for table "venue"
#

DROP TABLE IF EXISTS `venue`;
CREATE TABLE `venue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `introduction` varchar(255) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `phoneNumber` varchar(21) DEFAULT NULL,
  `totalSeat` int(11) DEFAULT NULL,
  `did` int(11) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `startTime` int(6) DEFAULT NULL,
  `endTime` int(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_venue_district` (`did`),
  CONSTRAINT `fk_venue_district` FOREIGN KEY (`did`) REFERENCES `district` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

#
# Data for table "venue"
#

INSERT INTO `venue` VALUES (3,'test3',NULL,9,'66667777',200,3,NULL,8,18),(4,'test4',NULL,10,'66667777',200,3,NULL,8,18),(5,'test','。。',7,'66667777888',200,3,'这里是地址..',9,10),(6,'t',NULL,20,NULL,200,3,NULL,7,18),(11,'gym',NULL,5,NULL,50,4,NULL,9,17);

#
# Structure for table "timeslot"
#

DROP TABLE IF EXISTS `timeslot`;
CREATE TABLE `timeslot` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `beginTime` int(6) DEFAULT NULL,
  `endTime` int(6) DEFAULT NULL,
  `bookingDate` date DEFAULT NULL,
  `seat` int(11) DEFAULT NULL,
  `vid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_timeslot_venue` (`vid`),
  CONSTRAINT `fk_timeslot_venue` FOREIGN KEY (`vid`) REFERENCES `venue` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1417 DEFAULT CHARSET=utf8;

#
# Data for table "timeslot"
#

INSERT INTO `timeslot` VALUES (2,8,9,'2019-11-08',99,3),(3,10,11,'2019-11-08',9,3),(4,9,10,'2019-11-08',10,3),(5,11,12,'2019-11-07',11,4),(6,9,10,'2019-11-07',5,11),(7,10,11,'2019-11-07',50,11),(8,11,12,'2019-11-07',50,11),(9,12,13,'2019-11-07',50,11),(10,13,14,'2019-11-07',50,11),(11,14,15,'2019-11-07',50,11),(12,15,16,'2019-11-07',50,11),(13,16,17,'2019-11-07',50,11),(14,9,10,'2019-11-08',50,11),(15,10,11,'2019-11-08',50,11),(16,11,12,'2019-11-08',50,11),(17,12,13,'2019-11-08',50,11),(18,13,14,'2019-11-08',50,11),(19,14,15,'2019-11-08',50,11),(20,15,16,'2019-11-08',50,11),(21,16,17,'2019-11-08',50,11),(40,8,9,'2019-11-09',200,3),(41,9,10,'2019-11-09',200,3),(42,10,11,'2019-11-09',200,3),(43,11,12,'2019-11-09',200,3),(44,12,13,'2019-11-09',200,3),(45,13,14,'2019-11-09',200,3),(46,14,15,'2019-11-09',200,3),(47,15,16,'2019-11-09',200,3),(48,16,17,'2019-11-09',200,3),(49,17,18,'2019-11-09',200,3),(50,8,9,'2019-11-09',200,4),(51,9,10,'2019-11-09',200,4),(52,10,11,'2019-11-09',200,4),(53,11,12,'2019-11-09',200,4),(54,12,13,'2019-11-09',200,4),(55,13,14,'2019-11-09',200,4),(56,14,15,'2019-11-09',200,4),(57,15,16,'2019-11-09',200,4),(58,16,17,'2019-11-09',200,4),(59,17,18,'2019-11-09',200,4),(60,9,10,'2019-11-09',200,5),(61,7,8,'2019-11-09',200,6),(62,8,9,'2019-11-09',200,6),(63,9,10,'2019-11-09',200,6),(64,10,11,'2019-11-09',200,6),(65,11,12,'2019-11-09',200,6),(66,12,13,'2019-11-09',200,6),(67,13,14,'2019-11-09',200,6),(68,14,15,'2019-11-09',200,6),(69,15,16,'2019-11-09',200,6),(70,16,17,'2019-11-09',200,6),(71,17,18,'2019-11-09',200,6),(72,9,10,'2019-11-09',50,11),(73,10,11,'2019-11-09',50,11),(74,11,12,'2019-11-09',50,11),(75,12,13,'2019-11-09',50,11),(76,13,14,'2019-11-09',50,11),(77,14,15,'2019-11-09',50,11),(78,15,16,'2019-11-09',50,11),(79,16,17,'2019-11-09',50,11),(80,8,9,'2019-11-10',200,3),(81,9,10,'2019-11-10',200,3),(82,10,11,'2019-11-10',200,3),(83,11,12,'2019-11-10',200,3),(84,12,13,'2019-11-10',200,3),(85,13,14,'2019-11-10',200,3),(86,14,15,'2019-11-10',200,3),(87,15,16,'2019-11-10',200,3),(88,16,17,'2019-11-10',200,3),(89,17,18,'2019-11-10',200,3),(90,8,9,'2019-11-10',200,4),(91,9,10,'2019-11-10',200,4),(92,10,11,'2019-11-10',200,4),(93,11,12,'2019-11-10',200,4),(94,12,13,'2019-11-10',200,4),(95,13,14,'2019-11-10',200,4),(96,14,15,'2019-11-10',200,4),(97,15,16,'2019-11-10',200,4),(98,16,17,'2019-11-10',200,4),(99,17,18,'2019-11-10',200,4),(100,9,10,'2019-11-10',200,5),(101,7,8,'2019-11-10',200,6),(102,8,9,'2019-11-10',200,6),(103,9,10,'2019-11-10',200,6),(104,10,11,'2019-11-10',200,6),(105,11,12,'2019-11-10',200,6),(106,12,13,'2019-11-10',200,6),(107,13,14,'2019-11-10',200,6),(108,14,15,'2019-11-10',200,6),(109,15,16,'2019-11-10',200,6),(110,16,17,'2019-11-10',200,6),(111,17,18,'2019-11-10',200,6),(112,9,10,'2019-11-10',50,11),(113,10,11,'2019-11-10',50,11),(114,11,12,'2019-11-10',50,11),(115,12,13,'2019-11-10',50,11),(116,13,14,'2019-11-10',50,11),(117,14,15,'2019-11-10',50,11),(118,15,16,'2019-11-10',50,11),(119,16,17,'2019-11-10',50,11),(120,8,9,'2019-11-18',0,3),(121,9,10,'2019-11-18',200,3),(122,10,11,'2019-11-18',200,3),(123,11,12,'2019-11-18',200,3),(124,12,13,'2019-11-18',200,3),(125,13,14,'2019-11-18',200,3),(126,14,15,'2019-11-18',200,3),(127,15,16,'2019-11-18',200,3),(128,16,17,'2019-11-18',200,3),(129,17,18,'2019-11-18',200,3),(130,8,9,'2019-11-18',200,4),(131,9,10,'2019-11-18',200,4),(132,10,11,'2019-11-18',200,4),(133,11,12,'2019-11-18',200,4),(134,12,13,'2019-11-18',200,4),(135,13,14,'2019-11-18',200,4),(136,14,15,'2019-11-18',200,4),(137,15,16,'2019-11-18',200,4),(138,16,17,'2019-11-18',200,4),(139,17,18,'2019-11-18',200,4),(140,9,10,'2019-11-18',200,5),(141,7,8,'2019-11-18',200,6),(142,8,9,'2019-11-18',200,6),(143,9,10,'2019-11-18',200,6),(144,10,11,'2019-11-18',200,6),(145,11,12,'2019-11-18',200,6),(146,12,13,'2019-11-18',200,6),(147,13,14,'2019-11-18',200,6),(148,14,15,'2019-11-18',200,6),(149,15,16,'2019-11-18',200,6),(150,16,17,'2019-11-18',200,6),(151,17,18,'2019-11-18',200,6),(152,9,10,'2019-11-18',50,11),(153,10,11,'2019-11-18',50,11),(154,11,12,'2019-11-18',50,11),(155,12,13,'2019-11-18',50,11),(156,13,14,'2019-11-18',50,11),(157,14,15,'2019-11-18',50,11),(158,15,16,'2019-11-18',50,11),(159,16,17,'2019-11-18',50,11),(160,8,9,'2019-11-19',200,3),(161,9,10,'2019-11-19',99,3),(162,10,11,'2019-11-19',200,3),(163,11,12,'2019-11-19',200,3),(164,12,13,'2019-11-19',200,3),(165,13,14,'2019-11-19',200,3),(166,14,15,'2019-11-19',200,3),(167,15,16,'2019-11-19',200,3),(168,16,17,'2019-11-19',200,3),(169,17,18,'2019-11-19',200,3),(170,8,9,'2019-11-19',200,4),(171,9,10,'2019-11-19',200,4),(172,10,11,'2019-11-19',200,4),(173,11,12,'2019-11-19',200,4),(174,12,13,'2019-11-19',200,4),(175,13,14,'2019-11-19',200,4),(176,14,15,'2019-11-19',200,4),(177,15,16,'2019-11-19',200,4),(178,16,17,'2019-11-19',200,4),(179,17,18,'2019-11-19',200,4),(180,9,10,'2019-11-19',200,5),(181,7,8,'2019-11-19',200,6),(182,8,9,'2019-11-19',200,6),(183,9,10,'2019-11-19',200,6),(184,10,11,'2019-11-19',200,6),(185,11,12,'2019-11-19',200,6),(186,12,13,'2019-11-19',200,6),(187,13,14,'2019-11-19',200,6),(188,14,15,'2019-11-19',200,6),(189,15,16,'2019-11-19',200,6),(190,16,17,'2019-11-19',200,6),(191,17,18,'2019-11-19',200,6),(192,9,10,'2019-11-19',50,11),(193,10,11,'2019-11-19',50,11),(194,11,12,'2019-11-19',50,11),(195,12,13,'2019-11-19',50,11),(196,13,14,'2019-11-19',50,11),(197,14,15,'2019-11-19',50,11),(198,15,16,'2019-11-19',50,11),(199,16,17,'2019-11-19',50,11),(200,8,9,'2019-11-20',200,3),(201,9,10,'2019-11-20',200,3),(202,10,11,'2019-11-20',200,3),(203,11,12,'2019-11-20',200,3),(204,12,13,'2019-11-20',200,3),(205,13,14,'2019-11-20',200,3),(206,14,15,'2019-11-20',200,3),(207,15,16,'2019-11-20',200,3),(208,16,17,'2019-11-20',200,3),(209,17,18,'2019-11-20',0,3),(210,8,9,'2019-11-20',200,4),(211,9,10,'2019-11-20',200,4),(212,10,11,'2019-11-20',200,4),(213,11,12,'2019-11-20',200,4),(214,12,13,'2019-11-20',200,4),(215,13,14,'2019-11-20',200,4),(216,14,15,'2019-11-20',200,4),(217,15,16,'2019-11-20',200,4),(218,16,17,'2019-11-20',200,4),(219,17,18,'2019-11-20',200,4),(220,9,10,'2019-11-20',200,5),(221,7,8,'2019-11-20',200,6),(222,8,9,'2019-11-20',200,6),(223,9,10,'2019-11-20',200,6),(224,10,11,'2019-11-20',200,6),(225,11,12,'2019-11-20',200,6),(226,12,13,'2019-11-20',200,6),(227,13,14,'2019-11-20',200,6),(228,14,15,'2019-11-20',200,6),(229,15,16,'2019-11-20',200,6),(230,16,17,'2019-11-20',200,6),(231,17,18,'2019-11-20',200,6),(232,9,10,'2019-11-20',50,11),(233,10,11,'2019-11-20',50,11),(234,11,12,'2019-11-20',50,11),(235,12,13,'2019-11-20',50,11),(236,13,14,'2019-11-20',50,11),(237,14,15,'2019-11-20',50,11),(238,15,16,'2019-11-20',50,11),(239,16,17,'2019-11-20',50,11),(1217,8,9,'2019-12-02',200,3),(1218,9,10,'2019-12-02',200,3),(1219,10,11,'2019-12-02',200,3),(1220,11,12,'2019-12-02',200,3),(1221,12,13,'2019-12-02',200,3),(1222,13,14,'2019-12-02',200,3),(1223,14,15,'2019-12-02',200,3),(1224,15,16,'2019-12-02',200,3),(1225,16,17,'2019-12-02',200,3),(1226,17,18,'2019-12-02',200,3),(1227,8,9,'2019-12-02',200,4),(1228,9,10,'2019-12-02',200,4),(1229,10,11,'2019-12-02',200,4),(1230,11,12,'2019-12-02',200,4),(1231,12,13,'2019-12-02',200,4),(1232,13,14,'2019-12-02',200,4),(1233,14,15,'2019-12-02',200,4),(1234,15,16,'2019-12-02',200,4),(1235,16,17,'2019-12-02',200,4),(1236,17,18,'2019-12-02',200,4),(1237,9,10,'2019-12-02',200,5),(1238,7,8,'2019-12-02',200,6),(1239,8,9,'2019-12-02',200,6),(1240,9,10,'2019-12-02',200,6),(1241,10,11,'2019-12-02',200,6),(1242,11,12,'2019-12-02',200,6),(1243,12,13,'2019-12-02',200,6),(1244,13,14,'2019-12-02',200,6),(1245,14,15,'2019-12-02',200,6),(1246,15,16,'2019-12-02',200,6),(1247,16,17,'2019-12-02',200,6),(1248,17,18,'2019-12-02',200,6),(1249,9,10,'2019-12-02',50,11),(1250,10,11,'2019-12-02',50,11),(1251,11,12,'2019-12-02',50,11),(1252,12,13,'2019-12-02',50,11),(1253,13,14,'2019-12-02',50,11),(1254,14,15,'2019-12-02',50,11),(1255,15,16,'2019-12-02',50,11),(1256,16,17,'2019-12-02',50,11),(1257,8,9,'2019-12-03',200,3),(1258,9,10,'2019-12-03',200,3),(1259,10,11,'2019-12-03',200,3),(1260,11,12,'2019-12-03',200,3),(1261,12,13,'2019-12-03',200,3),(1262,13,14,'2019-12-03',200,3),(1263,14,15,'2019-12-03',200,3),(1264,15,16,'2019-12-03',200,3),(1265,16,17,'2019-12-03',200,3),(1266,17,18,'2019-12-03',200,3),(1267,8,9,'2019-12-03',200,4),(1268,9,10,'2019-12-03',200,4),(1269,10,11,'2019-12-03',200,4),(1270,11,12,'2019-12-03',200,4),(1271,12,13,'2019-12-03',200,4),(1272,13,14,'2019-12-03',200,4),(1273,14,15,'2019-12-03',200,4),(1274,15,16,'2019-12-03',200,4),(1275,16,17,'2019-12-03',200,4),(1276,17,18,'2019-12-03',200,4),(1277,9,10,'2019-12-03',200,5),(1278,7,8,'2019-12-03',200,6),(1279,8,9,'2019-12-03',200,6),(1280,9,10,'2019-12-03',200,6),(1281,10,11,'2019-12-03',200,6),(1282,11,12,'2019-12-03',200,6),(1283,12,13,'2019-12-03',200,6),(1284,13,14,'2019-12-03',200,6),(1285,14,15,'2019-12-03',200,6),(1286,15,16,'2019-12-03',200,6),(1287,16,17,'2019-12-03',200,6),(1288,17,18,'2019-12-03',200,6),(1289,9,10,'2019-12-03',50,11),(1290,10,11,'2019-12-03',50,11),(1291,11,12,'2019-12-03',50,11),(1292,12,13,'2019-12-03',50,11),(1293,13,14,'2019-12-03',50,11),(1294,14,15,'2019-12-03',50,11),(1295,15,16,'2019-12-03',50,11),(1296,16,17,'2019-12-03',50,11),(1297,8,9,'2019-12-01',200,3),(1298,9,10,'2019-12-01',200,3),(1299,10,11,'2019-12-01',200,3),(1300,11,12,'2019-12-01',200,3),(1301,12,13,'2019-12-01',200,3),(1302,13,14,'2019-12-01',200,3),(1303,14,15,'2019-12-01',200,3),(1304,15,16,'2019-12-01',200,3),(1305,16,17,'2019-12-01',200,3),(1306,22,18,'2019-12-01',199,3),(1307,8,9,'2019-12-01',200,4),(1308,9,10,'2019-12-01',200,4),(1309,10,11,'2019-12-01',200,4),(1310,11,12,'2019-12-01',200,4),(1311,12,13,'2019-12-01',200,4),(1312,13,14,'2019-12-01',200,4),(1313,14,15,'2019-12-01',200,4),(1314,15,16,'2019-12-01',200,4),(1315,16,17,'2019-12-01',200,4),(1316,17,18,'2019-12-01',200,4),(1317,9,10,'2019-12-01',1,5),(1318,7,8,'2019-12-01',200,6),(1319,8,9,'2019-12-01',200,6),(1320,9,10,'2019-12-01',200,6),(1321,10,11,'2019-12-01',200,6),(1322,11,12,'2019-12-01',200,6),(1323,12,13,'2019-12-01',200,6),(1324,13,14,'2019-12-01',200,6),(1325,14,15,'2019-12-01',200,6),(1326,15,16,'2019-12-01',200,6),(1327,16,17,'2019-12-01',200,6),(1328,17,18,'2019-12-01',200,6),(1329,9,10,'2019-12-01',50,11),(1330,10,11,'2019-12-01',50,11),(1331,11,12,'2019-12-01',50,11),(1332,12,13,'2019-12-01',50,11),(1333,13,14,'2019-12-01',50,11),(1334,14,15,'2019-12-01',50,11),(1335,15,16,'2019-12-01',50,11),(1336,16,17,'2019-12-01',50,11),(1337,8,9,'2019-12-04',200,3),(1338,9,10,'2019-12-04',200,3),(1339,10,11,'2019-12-04',200,3),(1340,11,12,'2019-12-04',200,3),(1341,12,13,'2019-12-04',200,3),(1342,13,14,'2019-12-04',200,3),(1343,14,15,'2019-12-04',200,3),(1344,15,16,'2019-12-04',200,3),(1345,16,17,'2019-12-04',200,3),(1346,17,18,'2019-12-04',200,3),(1347,8,9,'2019-12-04',200,4),(1348,9,10,'2019-12-04',200,4),(1349,10,11,'2019-12-04',200,4),(1350,11,12,'2019-12-04',200,4),(1351,12,13,'2019-12-04',200,4),(1352,13,14,'2019-12-04',200,4),(1353,14,15,'2019-12-04',200,4),(1354,15,16,'2019-12-04',200,4),(1355,16,17,'2019-12-04',200,4),(1356,17,18,'2019-12-04',200,4),(1357,9,10,'2019-12-04',200,5),(1358,7,8,'2019-12-04',200,6),(1359,8,9,'2019-12-04',200,6),(1360,9,10,'2019-12-04',200,6),(1361,10,11,'2019-12-04',200,6),(1362,11,12,'2019-12-04',200,6),(1363,12,13,'2019-12-04',200,6),(1364,13,14,'2019-12-04',200,6),(1365,14,15,'2019-12-04',200,6),(1366,15,16,'2019-12-04',200,6),(1367,16,17,'2019-12-04',200,6),(1368,17,18,'2019-12-04',200,6),(1369,9,10,'2019-12-04',50,11),(1370,10,11,'2019-12-04',50,11),(1371,11,12,'2019-12-04',50,11),(1372,12,13,'2019-12-04',50,11),(1373,13,14,'2019-12-04',50,11),(1374,14,15,'2019-12-04',50,11),(1375,15,16,'2019-12-04',50,11),(1376,16,17,'2019-12-04',50,11),(1377,8,9,'2019-12-05',200,3),(1378,9,10,'2019-12-05',200,3),(1379,10,11,'2019-12-05',200,3),(1380,11,12,'2019-12-05',200,3),(1381,12,13,'2019-12-05',200,3),(1382,13,14,'2019-12-05',200,3),(1383,14,15,'2019-12-05',200,3),(1384,15,16,'2019-12-05',200,3),(1385,16,17,'2019-12-05',200,3),(1386,17,18,'2019-12-05',200,3),(1387,8,9,'2019-12-05',200,4),(1388,9,10,'2019-12-05',200,4),(1389,10,11,'2019-12-05',200,4),(1390,11,12,'2019-12-05',200,4),(1391,12,13,'2019-12-05',200,4),(1392,13,14,'2019-12-05',200,4),(1393,14,15,'2019-12-05',200,4),(1394,15,16,'2019-12-05',200,4),(1395,16,17,'2019-12-05',200,4),(1396,17,18,'2019-12-05',200,4),(1397,9,10,'2019-12-05',200,5),(1398,7,8,'2019-12-05',200,6),(1399,8,9,'2019-12-05',200,6),(1400,9,10,'2019-12-05',200,6),(1401,10,11,'2019-12-05',200,6),(1402,11,12,'2019-12-05',200,6),(1403,12,13,'2019-12-05',200,6),(1404,13,14,'2019-12-05',200,6),(1405,14,15,'2019-12-05',200,6),(1406,15,16,'2019-12-05',200,6),(1407,16,17,'2019-12-05',200,6),(1408,17,18,'2019-12-05',200,6),(1409,9,10,'2019-12-05',50,11),(1410,10,11,'2019-12-05',50,11),(1411,11,12,'2019-12-05',50,11),(1412,12,13,'2019-12-05',50,11),(1413,13,14,'2019-12-05',50,11),(1414,14,15,'2019-12-05',50,11),(1415,15,16,'2019-12-05',50,11),(1416,16,17,'2019-12-05',50,11);

#
# Structure for table "message"
#

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(4000) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `vid` int(11) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `bid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_message_venue` (`vid`),
  KEY `fk_message_user` (`uid`),
  CONSTRAINT `fk_message_user` FOREIGN KEY (`uid`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_message_venue` FOREIGN KEY (`vid`) REFERENCES `venue` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

#
# Data for table "message"
#

INSERT INTO `message` VALUES (14,'dgf',25,4,'2019-12-02 23:54:49','waitApprove','5');

#
# Structure for table "booking"
#

DROP TABLE IF EXISTS `booking`;
CREATE TABLE `booking` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `vid` int(11) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `state` varchar(40) DEFAULT NULL,
  `payDate` datetime DEFAULT NULL,
  `arriveDate` datetime DEFAULT NULL,
  `tid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_booking_venue` (`vid`),
  KEY `fk_booking_timeslot` (`tid`),
  KEY `fk_booking_user` (`uid`),
  CONSTRAINT `fk_booking_timeslot` FOREIGN KEY (`tid`) REFERENCES `timeslot` (`id`),
  CONSTRAINT `fk_booking_user` FOREIGN KEY (`uid`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_booking_venue` FOREIGN KEY (`vid`) REFERENCES `venue` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

#
# Data for table "booking"
#

INSERT INTO `booking` VALUES (1,25,3,'2019-10-27 10:55:53','refused','2019-10-27 10:55:53',NULL,3),(2,1,4,NULL,'refused',NULL,NULL,5),(3,25,3,'2019-10-27 10:55:53','waitReview',NULL,NULL,4),(4,2,3,NULL,'waitPay',NULL,NULL,4),(5,25,4,'2019-12-01 21:12:48','waitTime',NULL,NULL,1307),(13,25,3,'2019-12-01 21:12:48','waitFinish',NULL,NULL,1306);

#
# Structure for table "venueimage"
#

DROP TABLE IF EXISTS `venueimage`;
CREATE TABLE `venueimage` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `vid` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_venue_venueimage` (`vid`),
  CONSTRAINT `fk_venue_venueimage` FOREIGN KEY (`vid`) REFERENCES `venue` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

#
# Data for table "venueimage"
#

INSERT INTO `venueimage` VALUES (12,5),(14,4),(15,4),(16,3),(17,3),(18,3);