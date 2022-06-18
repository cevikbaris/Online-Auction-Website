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
-- Table structure for table `file_attachment`
--

DROP TABLE IF EXISTS `file_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_attachment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `auction_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgw391ep7u9w04imw840q8obey` (`auction_id`),
  CONSTRAINT `FKgw391ep7u9w04imw840q8obey` FOREIGN KEY (`auction_id`) REFERENCES `auction_table` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_attachment`
--

LOCK TABLES `file_attachment` WRITE;
/*!40000 ALTER TABLE `file_attachment` DISABLE KEYS */;
INSERT INTO `file_attachment` VALUES (5,'2022-06-16 04:10:06','image/jpeg','695999765e984bb9a3e011ed75b79330',4),(6,'2022-06-16 04:12:10','image/jpeg','72e9414e00db4eb28c5365f66f1bdd4d',5),(7,'2022-06-16 04:14:01','image/jpeg','352bffc92dfe4e4d8c591822b061cad3',6),(8,'2022-06-16 04:17:06','image/jpeg','5408dd508e1f436090b05f6b3434eab5',7),(9,'2022-06-16 04:19:42','image/jpeg','6dff70489fd34086bdce78790f32131d',8),(10,'2022-06-16 04:21:57','image/jpeg','3f48d4361baf4c69b3330b6b9e2c3e8d',9),(11,'2022-06-16 04:23:14','image/jpeg','06e1e558e0fd4f54a6e684a04b03ec9d',10),(12,'2022-06-16 05:15:34','image/jpeg','fac62c958c4c422580a00398c9a0a4f9',11),(13,'2022-06-16 05:19:21','image/jpeg','cfdab7b93a6140e39a8442723fa5e197',12),(14,'2022-06-16 05:42:10','image/jpeg','16519a62b83345feaf8605d3792788c5',13);
/*!40000 ALTER TABLE `file_attachment` ENABLE KEYS */;
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
