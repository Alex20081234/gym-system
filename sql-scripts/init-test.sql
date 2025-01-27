-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: gymsystemtest
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

DROP DATABASE IF EXISTS `gymsystemdatabase`;
CREATE DATABASE `gymsystemdatabase`;
USE gymsystemdatabase;

GRANT ALL PRIVILEGES ON gymsystemtest.* TO 'dev'@'%' IDENTIFIED BY 'DevPassword';
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trainee`
--

DROP TABLE IF EXISTS `trainee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainee` (
                           `DateOfBirth` date NOT NULL,
                           `id` int NOT NULL,
                           `Address` varchar(255) NOT NULL,
                           PRIMARY KEY (`id`),
                           CONSTRAINT `FK1b1rs0qdwriiqxlqflj5njnho` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
-- Dumping events for database 'gymsystemtest'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-22 10:13:43
