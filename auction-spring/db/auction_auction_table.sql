-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: auction
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `auction_table`
--

DROP TABLE IF EXISTS `auction_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auction_table` (
  `id` int NOT NULL AUTO_INCREMENT,
  `end_date` datetime NOT NULL,
  `explanation` text NOT NULL,
  `minimum_increase` int NOT NULL,
  `sell_now_price` int NOT NULL,
  `start_date` datetime NOT NULL,
  `start_price` int NOT NULL,
  `title` varchar(255) NOT NULL,
  `buyer_id` bigint DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `creator_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc4r5mcxvp6px6qnr2its61hga` (`buyer_id`),
  KEY `FKl088mxkyvtxtkpte11ksbbj5i` (`category_id`),
  KEY `FKq2ro50en48y3f9p7ftfyqgqwv` (`creator_id`),
  CONSTRAINT `FKc4r5mcxvp6px6qnr2its61hga` FOREIGN KEY (`buyer_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKl088mxkyvtxtkpte11ksbbj5i` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FKq2ro50en48y3f9p7ftfyqgqwv` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auction_table`
--

LOCK TABLES `auction_table` WRITE;
/*!40000 ALTER TABLE `auction_table` DISABLE KEYS */;
INSERT INTO `auction_table` VALUES (4,'2022-06-23 04:10:00','2019 model with no problems at 20,000 miles.',100,15000,'2022-06-16 04:11:55',1000,'Opel Corsa',NULL,1,2),(5,'2022-06-19 04:13:00','Very little used Passat. Navy blue, very clean. 5000 miles',100,25000,'2022-06-16 04:13:48',5000,'Volswagen Passat',NULL,1,2),(6,'2022-06-21 04:14:00','Yellow 2021 Volkswagen Polo. There is no problem. No damage record.',25,25000,'2022-06-16 04:15:29',7000,'Volkswagen Polo',NULL,1,2),(7,'2022-06-25 04:17:00','Fiat egea. SUV vehicle. Perfect for the family. Like new. ',50,15000,'2022-06-16 04:17:58',5000,'Fiat Egean',NULL,1,2),(8,'2022-06-16 10:18:23','A very well preserved, special handmade antique item of 100 years.',10,5000,'2022-06-16 04:21:04',5000,'Antique cabinet',2,3,3),(9,'2022-06-29 04:22:00','Antique Desk. Dating from the 1800\'s. Gold plated, handcrafted. UK origin.',20,10000,'2022-06-16 04:23:02',2070,'Study desk',NULL,3,3),(10,'2022-06-20 04:24:00','A 200-year-old historical artifact from the Ottoman period. Copper plated. It\'s working.',5,2500,'2022-06-16 04:24:32',200,'Gramophone',NULL,3,3),(11,'2022-06-22 05:16:00','duplex house. 5+2 250 square meters. It has a garden. Private.',1000,1000000,'2022-06-16 05:16:54',200000,'Villa',NULL,2,1),(12,'2022-06-16 05:30:00','4+1, 250 square meters. It has a garden. Very close to the city.',1000,500000,'2022-06-16 05:20:25',100000,'House 4+1',NULL,2,1),(13,'2022-06-16 05:49:00','Helicopter with a capacity of 6 people. Suitable for civil flight.',1000,250000,'2022-06-16 05:43:23',150000,'Helicopter',2,5,3);
/*!40000 ALTER TABLE `auction_table` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-18 17:03:18
