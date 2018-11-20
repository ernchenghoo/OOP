-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: oop
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `branch`
--

DROP TABLE IF EXISTS `branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `branch` (
  `branchid` int(10) NOT NULL,
  `location` varchar(100) NOT NULL,
  PRIMARY KEY (`branchid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch`
--

LOCK TABLES `branch` WRITE;
/*!40000 ALTER TABLE `branch` DISABLE KEYS */;
INSERT INTO `branch` VALUES (1,'Sunway'),(2,'Puchong'),(3,'Subang Jaya'),(4,'Putrajaya');
/*!40000 ALTER TABLE `branch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `inventory` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (10001,'ern the cheng',1),(10002,'ern cheng',2),(10003,'ern',3);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `item` (
  `itemid` int(10) NOT NULL,
  `itemname` varchar(100) NOT NULL,
  `itemdesc` varchar(500) NOT NULL,
  `price` decimal(12,2) NOT NULL,
  PRIMARY KEY (`itemid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (1,'Keyboard','Keyboard desc',50.00),(2,'Bottle','A Bottle',10.50),(3,'Apple','fruit',0.11),(4,'Monitor','A LED Moniter with 120 Hertz',500.00),(5,'Banana','Freshly pickled bananas',5.99),(6,'SmartPhone','SmartPhone that use AMOLED screen with snapdragon processor\n',1199.99),(7,'A4 Paper','A Bundle of A4 Paper that have 400 sheet\nA4 Paper',50.00),(8,'Speaker','A Speaker by Sony with high bass and produce high quality sounds',699.00),(9,'Candy','Pepermint flavour candy ',9.50),(10,'Isotonic Drink','A good choice to drink for athelete',5.00),(11,'Bread','A White Bread filled with vanila flavoured cream',0.99),(12,'Television','A Television that support 4k resolution',1999.00),(13,'Soap','A 500 gram soaps with strawberry smelt',15.50),(14,'Office Chair','Office Chair that equiped with roller',699.00),(15,'Laptop','A Laptop by MSI',2999.00);
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itemsold`
--

DROP TABLE IF EXISTS `itemsold`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `itemsold` (
  `salesid` int(10) NOT NULL,
  `itemid` int(10) NOT NULL,
  `itemname` varchar(200) NOT NULL,
  `quantity` int(10) NOT NULL,
  `price` decimal(12,2) NOT NULL,
  PRIMARY KEY (`salesid`,`itemid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itemsold`
--

LOCK TABLES `itemsold` WRITE;
/*!40000 ALTER TABLE `itemsold` DISABLE KEYS */;
INSERT INTO `itemsold` VALUES (8,1,'Keyboard',2,50.00),(8,2,'Bottle',5,10.50),(9,1,'Keyboard',1,50.00),(9,2,'Bottle',5,10.50),(10,1,'Keyboard',17,50.00),(10,2,'Bottle',10,10.50),(11,1,'Keyboard',1,0.11),(12,3,'Apple',2,0.11);
/*!40000 ALTER TABLE `itemsold` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itemstock`
--

DROP TABLE IF EXISTS `itemstock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `itemstock` (
  `itemid` int(10) NOT NULL,
  `branchid` int(10) NOT NULL,
  `numofstock` bigint(20) NOT NULL,
  PRIMARY KEY (`itemid`,`branchid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itemstock`
--

LOCK TABLES `itemstock` WRITE;
/*!40000 ALTER TABLE `itemstock` DISABLE KEYS */;
INSERT INTO `itemstock` VALUES (1,1,36),(1,2,0),(1,3,0),(1,4,0),(2,1,0),(2,2,0),(2,3,0),(2,4,0),(3,1,0),(3,2,0),(3,3,0),(3,4,0),(4,1,0),(4,2,0),(4,3,0),(4,4,0),(5,1,0),(5,2,0),(5,3,0),(5,4,0),(6,1,0),(6,2,0),(6,3,0),(6,4,0),(7,1,0),(7,2,0),(7,3,0),(7,4,0),(8,1,0),(8,2,0),(8,3,0),(8,4,0),(9,1,0),(9,2,0),(9,3,0),(9,4,0),(10,1,0),(10,2,0),(10,3,0),(10,4,0),(11,1,0),(11,2,0),(11,3,0),(11,4,0),(12,1,0),(12,2,0),(12,3,0),(12,4,0),(13,1,0),(13,2,0),(13,3,0),(13,4,0),(14,1,0),(14,2,0),(14,3,0),(14,4,0),(15,1,0),(15,2,0),(15,3,0),(15,4,0);
/*!40000 ALTER TABLE `itemstock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `returnitem`
--

DROP TABLE IF EXISTS `returnitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `returnitem` (
  `returnitemid` int(10) NOT NULL,
  `date` timestamp NOT NULL,
  `salesid` varchar(255) DEFAULT NULL,
  `itemid` int(10) NOT NULL,
  `itemname` varchar(100) NOT NULL,
  `branchid` int(10) NOT NULL,
  `branchlocation` varchar(100) NOT NULL,
  `amount` bigint(20) NOT NULL,
  `description` varchar(500) NOT NULL,
  PRIMARY KEY (`returnitemid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `returnitem`
--

LOCK TABLES `returnitem` WRITE;
/*!40000 ALTER TABLE `returnitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `returnitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sales` (
  `salesid` int(10) NOT NULL,
  `branchid` int(10) NOT NULL,
  `date` timestamp NOT NULL,
  `totalsalesamount` decimal(12,2) NOT NULL,
  PRIMARY KEY (`salesid`,`branchid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,1,'2018-11-12 07:41:52',500.00),(2,1,'2018-11-12 08:13:06',50.00),(3,1,'2018-11-12 08:13:27',300.00),(4,1,'2018-11-14 04:17:33',484.00),(5,1,'2018-11-14 04:19:39',50.00),(6,1,'2018-11-14 04:26:42',50.00),(7,1,'2018-11-14 04:31:31',363.00),(8,1,'2018-11-14 04:32:25',352.50),(9,1,'2018-11-14 04:45:06',202.50),(10,1,'2018-11-14 22:40:09',1105.00),(11,1,'2018-11-14 23:42:53',0.11),(12,1,'2018-11-15 00:08:52',0.22);
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stockedithistory`
--

DROP TABLE IF EXISTS `stockedithistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `stockedithistory` (
  `stockeditid` int(10) NOT NULL,
  `date` timestamp NOT NULL,
  `itemid` int(10) NOT NULL,
  `itemname` varchar(100) NOT NULL,
  `branchid` int(10) NOT NULL,
  `branchlocation` varchar(100) NOT NULL,
  `amount` bigint(20) NOT NULL,
  `description` varchar(500) NOT NULL,
  PRIMARY KEY (`stockeditid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stockedithistory`
--

LOCK TABLES `stockedithistory` WRITE;
/*!40000 ALTER TABLE `stockedithistory` DISABLE KEYS */;
INSERT INTO `stockedithistory` VALUES (1,'2018-11-11 23:57:58',1,'Keyboard',1,'Sunway',1,'123');
/*!40000 ALTER TABLE `stockedithistory` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-20 20:19:34
