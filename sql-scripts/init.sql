-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: gymsystemdatabase
-- ------------------------------------------------------
-- Server version	8.0.39

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

CREATE DATABASE IF NOT EXISTS gymsystemdatabase;
USE gymsystemdatabase;

GRANT ALL PRIVILEGES ON gymsystemdatabase.* TO 'dev'@'%' IDENTIFIED BY 'DevPassword';
FLUSH PRIVILEGES;

--
-- Table structure for table `blacklist`
--

DROP TABLE IF EXISTS `blacklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blacklist` (
                             `id` varchar(255) NOT NULL,
                             `ExpireDate` date NOT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trainee`
--

DROP TABLE IF EXISTS `trainee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainee` (
                           `DateOfBirth` date DEFAULT NULL,
                           `id` int NOT NULL,
                           `Address` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           CONSTRAINT `FK1b1rs0qdwriiqxlqflj5njnho` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trainee_trainer`
--

DROP TABLE IF EXISTS `trainee_trainer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainee_trainer` (
                                   `trainee_id` int NOT NULL,
                                   `trainer_id` int NOT NULL,
                                   PRIMARY KEY (`trainee_id`,`trainer_id`),
                                   KEY `FKe6n92jfm3hp9nuxk2u3ipshei` (`trainer_id`),
                                   CONSTRAINT `FK30hetj4tutcpj1q7qhgk96785` FOREIGN KEY (`trainee_id`) REFERENCES `trainee` (`id`),
                                   CONSTRAINT `FKe6n92jfm3hp9nuxk2u3ipshei` FOREIGN KEY (`trainer_id`) REFERENCES `trainer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trainer`
--

DROP TABLE IF EXISTS `trainer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainer` (
                           `Specialization` int NOT NULL,
                           `id` int NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKg1xt0c1nfosc7dpv4tehwwvie` (`Specialization`),
                           CONSTRAINT `FKg1xt0c1nfosc7dpv4tehwwvie` FOREIGN KEY (`Specialization`) REFERENCES `trainingtype` (`id`),
                           CONSTRAINT `FKm4ck87wwdx2p9ttytj6drmhng` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `training`
--

DROP TABLE IF EXISTS `training`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training` (
                            `TraineeId` int NOT NULL,
                            `TrainerId` int NOT NULL,
                            `TrainingDate` date NOT NULL,
                            `TrainingDuration` int NOT NULL,
                            `TrainingTypeId` int NOT NULL,
                            `id` int NOT NULL AUTO_INCREMENT,
                            `TrainingName` varchar(255) NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FKlimd0kk0dah62b0206cadrib4` (`TraineeId`),
                            KEY `FKs9pox8k10cws4p6jdba9vsdtq` (`TrainerId`),
                            KEY `FK9cah2yg5yv04itvdxppl2bjn5` (`TrainingTypeId`),
                            CONSTRAINT `FK9cah2yg5yv04itvdxppl2bjn5` FOREIGN KEY (`TrainingTypeId`) REFERENCES `trainingtype` (`id`),
                            CONSTRAINT `FKlimd0kk0dah62b0206cadrib4` FOREIGN KEY (`TraineeId`) REFERENCES `trainee` (`id`),
                            CONSTRAINT `FKs9pox8k10cws4p6jdba9vsdtq` FOREIGN KEY (`TrainerId`) REFERENCES `trainer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=268 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trainingtype`
--

DROP TABLE IF EXISTS `trainingtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainingtype` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `TrainingTypeName` varchar(255) NOT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
                        `IsActive` bit(1) NOT NULL,
                        `id` int NOT NULL AUTO_INCREMENT,
                        `FirstName` varchar(255) NOT NULL,
                        `LastName` varchar(255) NOT NULL,
                        `Password` varchar(255) NOT NULL,
                        `Username` varchar(255) NOT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `UKseh7nteifndaopocsq9f1w8ia` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=867 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usertries`
--

DROP TABLE IF EXISTS `usertries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usertries` (
                             `id` varchar(255) NOT NULL,
                             `Attempts` int NOT NULL,
                             `BlockTime` bigint DEFAULT NULL,
                             `ExpireDate` date DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'gymsystemdatabase'
--
/*!50106 SET @save_time_zone= @@TIME_ZONE */ ;
/*!50106 DROP EVENT IF EXISTS `delete_expired_blacklist_entries` */;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = cp852 */ ;;
/*!50003 SET character_set_results = cp852 */ ;;
/*!50003 SET collation_connection  = cp852_general_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`localhost`*/ /*!50106 EVENT `delete_expired_blacklist_entries` ON SCHEDULE EVERY 1 DAY STARTS '2024-11-04 12:49:20' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    DELETE FROM blacklist
    WHERE ExpireDate < NOW();
END */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
/*!50106 DROP EVENT IF EXISTS `delete_expired_user_tries` */;;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = cp852 */ ;;
/*!50003 SET character_set_results = cp852 */ ;;
/*!50003 SET collation_connection  = cp852_general_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`localhost`*/ /*!50106 EVENT `delete_expired_user_tries` ON SCHEDULE EVERY 1 DAY STARTS '2024-11-04 12:56:01' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    DELETE FROM usertries
    WHERE ExpireDate < NOW();
END */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
DELIMITER ;
/*!50106 SET TIME_ZONE= @save_time_zone */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-22 10:11:20
