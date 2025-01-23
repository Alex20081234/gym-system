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

CREATE DATABASE IF NOT EXISTS gymsystemtest;
USE gymsystemtest;

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
-- Dumping data for table `blacklist`
--

LOCK TABLES `blacklist` WRITE;
/*!40000 ALTER TABLE `blacklist` DISABLE KEYS */;
INSERT INTO `blacklist` VALUES ('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjE5OTI5MiwiZXhwIjoxNzM2MTk5ODkyfQ.Zm3l1g8im9BIoy5gnHbSq-lPVBGOF89SaUeZ7oJHRxU','2025-01-06'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjExMjA4NSwiZXhwIjoxNzM2MTEyNjg1fQ.a1rxSRv_fqLyXkIgIKygIC2xNWwAvy7Q3tCGRV4h-18','2025-01-05'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjExMjcyMCwiZXhwIjoxNzM2MTEzMzIwfQ.H0oG_h3gWhI5mcROK7nUBaTXBi5JnYCkjSuO7aVP4us','2025-01-05'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjExMjE5NywiZXhwIjoxNzM2MTEyNzk3fQ.vAOAmHJfZdrHwkIcVJh44mx4yBzNPkuHsYIuOi_URC0','2025-01-05'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjExMjU0MCwiZXhwIjoxNzM2MTEzMTQwfQ.uFU3LhRPC5mnFT5Ncby1qVs-IZCQp8XWmqhbpFFDowI','2025-01-05'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjExMTc0OSwiZXhwIjoxNzM2MTEyMzQ5fQ.iwG8R3cTAbjP01kyFcNq26bx8qTJE1cChdWgxGgyh3E','2025-01-05'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjExMTYwNiwiZXhwIjoxNzM2MTEyMjA2fQ.98u2tq-Qr3DwNLNSyiNxjtzteUrmUC0jdUvrUX4RQoM','2025-01-05'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjExMTYwNywiZXhwIjoxNzM2MTEyMjA3fQ.h-O71aN2WbllaXhmzskKcvoY3kVUcjTNuUMjfBo1pe0','2025-01-05'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjExMzExOCwiZXhwIjoxNzM2MTEzNzE4fQ._Jtws-O7G11ZPgT-eze4dGqBhtZNvQz5elxvQVlwcaA','2025-01-05'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjExMzgwMCwiZXhwIjoxNzM2MTE0NDAwfQ.d7XqPORKrBNcJOLHmSynShKNLKR_lBChY6sfvHR2J38','2025-01-05'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjExMzI3NCwiZXhwIjoxNzM2MTEzODc0fQ.R-u8IK7KlmhcOfwg0RsCC67bIsfSf0UyJ_-lfJtlFx8','2025-01-05'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjExMzkxMiwiZXhwIjoxNzM2MTE0NTEyfQ.q59DIpTwA5E7qV5pk9crf47iVcGtC4gjB2GXbhOZnxI','2025-01-05'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjExMzM1NCwiZXhwIjoxNzM2MTEzOTU0fQ.d1DKSkyDo5vpiFWkBRXkF9iarM20p5IiT57ug5IdAzA','2025-01-05'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0NjcxMywiZXhwIjoxNzM2MjQ3MzEzfQ.GwckaakzePJqysyVJOap3ndhud4NfBFR4zj0S48KuQI','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0Njg2NCwiZXhwIjoxNzM2MjQ3NDY0fQ.JEiGWkzJ24WFKtw_7CxCN9lTSZBUdDnyWrNZnRhhqy8','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0Njk3NiwiZXhwIjoxNzM2MjQ3NTc2fQ.g1aZxYyFIU6IRXW4f6FNbckKrlz1qUeA0W71PRUPwnc','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0NjkzMSwiZXhwIjoxNzM2MjQ3NTMxfQ.Zci7Yl9p3TiT6HOy1BG6Vbd_oFjFbt9wdnNY9fDh1-E','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0NjQyMSwiZXhwIjoxNzM2MjQ3MDIxfQ.XYwdnFNFpvG9P5JqzGC6NQIJ8Kt0IU0rbklY22n0dd4','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0NjU5NSwiZXhwIjoxNzM2MjQ3MTk1fQ.HkNu0A44AsTcfmbA76ZV4NSlBNtnK9KqDabbBsZM7Q8','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0NzA3NSwiZXhwIjoxNzM2MjQ3Njc1fQ.ShTHgQMyZKHuOPw-gXM7ohrtdrs-iMESw_SJVtlsydM','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0NzEwOCwiZXhwIjoxNzM2MjQ3NzA4fQ._mw0M7M6JbQab7SK7OxSLTDT8DUobGpd720NE8bgQws','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0NzI4MCwiZXhwIjoxNzM2MjQ3ODgwfQ.I0OHdLELBHXTwKlpC7iXKFDPwNPMbayZkE789uB0sYU','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0NzM0NSwiZXhwIjoxNzM2MjQ3OTQ1fQ.Wx90puyGD7H_8wy9MPHzilVdKHZQerPtGzg_TVR6X94','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0NzM2OCwiZXhwIjoxNzM2MjQ3OTY4fQ.Be8JHXC11MvE_aDz02yFgs4x8ox2HokPtAbWE8GoI1M','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0NzQwMiwiZXhwIjoxNzM2MjQ4MDAyfQ.Nqq4yk0AziFAHtLlrRnJcdf-Rk1bzl9v32OcKxtTFB8','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0OTc4OCwiZXhwIjoxNzM2MjUwMzg4fQ.7NcNniAKCmqEMV_tAuGF1dmLaEFlVzA5kVilnTuCzxo','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0OTM3NCwiZXhwIjoxNzM2MjQ5OTc0fQ.IaNzA3DMR_gziHRl7MYADB51auXsX92XsiufmDn8_64','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0OTQ2MywiZXhwIjoxNzM2MjUwMDYzfQ.EIXCsVGF_uQzw54z6oFD4hKPBcmneOA03OioShP_ZKM','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0OTQwOSwiZXhwIjoxNzM2MjUwMDA5fQ.ZINX9W1NJjsyRagWQ5L5SQS9as8H6GFVNnqgsxa02bE','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0OTU2OCwiZXhwIjoxNzM2MjUwMTY4fQ.qiwImhub86KpR1FS5XiALSZ2MfLJfNfWtbJSanQD-uY','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0OTUwNSwiZXhwIjoxNzM2MjUwMTA1fQ.3R6qr041N2TbtQE1jWbc9ASW0rUiRYzEnLUUTp63Keo','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI0OTY1MSwiZXhwIjoxNzM2MjUwMjUxfQ.hJdE57ZOhBQzDUatbPxfwbZIbNfW-okvJBeLeGs19W0','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1MDc3MiwiZXhwIjoxNzM2MjUxMzcyfQ.l1y4f1aqlRj8oq35je1lVw1U3Kj68ydRoHyxQGxQn90','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1MDIyMCwiZXhwIjoxNzM2MjUwODIwfQ.819CsZF_R4iCmTGSh3Mo7cKnJo35qibVvCRTZKnYLkk','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1MDk0NCwiZXhwIjoxNzM2MjUxNTQ0fQ.DjR9_KWu8qIecgMxQTXM4CBRPtrLLg_iCtQxRUWFs3w','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1MDQ1MSwiZXhwIjoxNzM2MjUxMDUxfQ.JyLXTOE-rZeRQqBO_OSeDJXYGo2G-Nf0zAxV9QZnFlI','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1MDU1OSwiZXhwIjoxNzM2MjUxMTU5fQ.pz94R17r_fe72Lvz86me4eIPp3S80mxCCE9CXKWrong','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1MTAzNSwiZXhwIjoxNzM2MjUxNjM1fQ.dOrMm4xxB2mz8LxcfKycZ2I-Nxsrgxwzk4IdgAgKzSw','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1MTY3NCwiZXhwIjoxNzM2MjUyMjc0fQ.bpPhTypgJFX96Zemb24noE_SZN6V0-VgryNBAR_w7Ao','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NDcxMCwiZXhwIjoxNzM2MjU1MzEwfQ.YhteghlO3HY33EmO-9E1n2CCd9NzmNbU-OwSFwrY1PA','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NDk0MywiZXhwIjoxNzM2MjU1NTQzfQ.Icjp_tMAnVLf8FYs2gZNJqd44EFdilQ0dEwt1f0R05g','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NDUwNiwiZXhwIjoxNzM2MjU1MTA2fQ.UtnKb_bnFCqSl28irlcR_EK-xoJbScPbFfaw-8vgwO8','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NjA1OCwiZXhwIjoxNzM2MjU2NjU4fQ.-tqi6l66Whxb8h39ZY1txuYa9ycSKV5EUjJ6M_6_Rlc','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1Njc1OSwiZXhwIjoxNzM2MjU3MzU5fQ.VQB4BTXJnM9r_rdCMRk6_aIjt1XkEPoWpk_WIgs0_oY','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NjE0OSwiZXhwIjoxNzM2MjU2NzQ5fQ.Z8fEFKaEl0k3-lUZki5MUQIHGH4Gii-gtQXaC278-XY','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1Njk2OCwiZXhwIjoxNzM2MjU3NTY4fQ.552Ms9jcVb7C2HecBQUMhD22VIEjJy51OqLRcImTfOM','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NjQ2MywiZXhwIjoxNzM2MjU3MDYzfQ.rEPaWKfSSgGln3pXI-rQCy8WPTFSr4XFJ3PACInK2XM','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NjQ5OCwiZXhwIjoxNzM2MjU3MDk4fQ._ulwpvGJa_pTlcuEp6HvWRai0d9OEztPHy6228cjgdo','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NjU0MiwiZXhwIjoxNzM2MjU3MTQyfQ.yoh5r9zA3obt9a7TZQtyBY8-Wu5fKYl-V3QLZNKS9C0','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NTAzNSwiZXhwIjoxNzM2MjU1NjM1fQ.pHBc2Owv9Gy-rAxCC6ijE4FlvgZAAFTCY2Y6u3Pq1XI','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NTExNSwiZXhwIjoxNzM2MjU1NzE1fQ.KSsuNUlJ1c-8_-vnkF3sKTbZKogPWhMTR53uTZ5lMUw','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NTgwOSwiZXhwIjoxNzM2MjU2NDA5fQ.eFrW9kyDV-dYygokr5yI-17wAxNXGgC6uiGd_HKpR78','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NTM3MywiZXhwIjoxNzM2MjU1OTczfQ.etrmGwWouyWOwxRR8PK4f1IfYb3gC7MtNI0rjEroGBU','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NTQ1MywiZXhwIjoxNzM2MjU2MDUzfQ.c6JcESkIziX-JBx0fSuTWqs-8x6bXg0JewiChRCuZc8','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NTUyMCwiZXhwIjoxNzM2MjU2MTIwfQ.56B6l2iyjVzbB5WqSeAChsf-l0xN5jB_Jl2eHc_dBt8','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1NzEzMiwiZXhwIjoxNzM2MjU3NzMyfQ.Q-Va5BjoOVK6RMzJLKH__h5_-e_EMCdHRfsZDKlmfSU','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1ODA3OSwiZXhwIjoxNzM2MjU4Njc5fQ.bjJvIKuEQ1_MtyhnPShqHwoiGZ4e_GqmfXWH-fBKvj0','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI1ODQ0MCwiZXhwIjoxNzM2MjU5MDQwfQ.nNRCFaqMlsj5z6aKQzz-tyVs3tyQS2Hyy3XHc2FlnFU','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI2MDQ1MywiZXhwIjoxNzM2MjYxMDUzfQ.FerMGYjium0I_eftuNfNiyNhZ02_geZ_MQtN049CE2A','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI3NDA2MSwiZXhwIjoxNzM2Mjc0NjYxfQ.f9oLIsHa6ZU6Xn8Gw5MbmAbT1F25iOG7OnL_D2ZITVs','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjI3Njg2OCwiZXhwIjoxNzM2Mjc3NDY4fQ.wFq9jkHhoNZjVdgSkPUIcmKhKZXx5gUb4Y89flxtSc4','2025-01-07'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjMyNTcxNCwiZXhwIjoxNzM2MzI2MzE0fQ.BfZ4mua2wyZWo_2br_D0qPTpbuwcKaA5F11pQ0hbyAo','2025-01-08'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjMyODcyNCwiZXhwIjoxNzM2MzI5MzI0fQ.uW-TuFJfly5mxjLT_PLOV5qK5IqFraBDF23uXceeSaY','2025-01-08'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjMyOTM1NywiZXhwIjoxNzM2MzI5OTU3fQ.q3Mtkmw9ilMJA_a6Rj-fxuJ_NLAMXQNwwSHrE9cFlaQ','2025-01-08'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjMyOTU0NywiZXhwIjoxNzM2MzMwMTQ3fQ.gwlbB_adhNeyqU4bylnnZQmnP4mc9hb1QHzkmHpsuRc','2025-01-08'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjU0NDI5MiwiZXhwIjoxNzM2NTQ0ODkyfQ.wcmdbVAok5ydApK5eF3XfdZSaGn6e_5IDXN_hbjSrcA','2025-01-10'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYxNDY4NiwiZXhwIjoxNzM2NjE1Mjg2fQ.948_rv0AfTwU16uAPS1DQGSPFDeV16LrPBduHgmGxHk','2025-01-11'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYxNTE2OCwiZXhwIjoxNzM2NjE1NzY4fQ.s2DuDb0VAMR7ejVl0DGPHlDXApBLiJgbfkF3WAeQyN8','2025-01-11'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYyMzc2NiwiZXhwIjoxNzM2NjI0MzY2fQ.NrNh2noEayMfclLkXwmfc_UFw1ET1wu7HBiNJMcJe_A','2025-01-11'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYyMzU0NSwiZXhwIjoxNzM2NjI0MTQ1fQ.cIeTj27HDDsSAOewHrhwlXdQKznAnmlIlwvayQXQWl0','2025-01-11'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYyMzY2NSwiZXhwIjoxNzM2NjI0MjY1fQ.A5P-UgxJwmdfl0cWfycicJWEMBUfKAkNT5kqXzWVNhg','2025-01-11'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYyNDI1MywiZXhwIjoxNzM2NjI0ODUzfQ.RT-x5LCMXBqpAWxBoLARywTPitvq3dcLiK_Wi_nlOAI','2025-01-11'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYyNDIwOSwiZXhwIjoxNzM2NjI0ODA5fQ.TtoVZ4hHThBP6kcAEHZGIRiaMI_naHZlcqXdAXTRNl0','2025-01-11'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYyNDM3MCwiZXhwIjoxNzM2NjI0OTcwfQ.1rufqE6xeX4M0FoZ85bWe1m49m792i8mlR-uy6f1Gjw','2025-01-11'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYyNDQ3MywiZXhwIjoxNzM2NjI1MDczfQ.tpzH1oO4y7SlsdS28Xn2qEhZ9ldJDU3AFqN6RgKoZVY','2025-01-11'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYyNDQyMywiZXhwIjoxNzM2NjI1MDIzfQ.tziVvOR0yICy4Slq8EcQMCV-djoGEDINyA7gu4KdkLo','2025-01-11'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYzMTA5OSwiZXhwIjoxNzM2NjMxNjk5fQ.pAE1rGgKHPAc4hxGxpo5aIwSAiNq3oJYM6FA26VWp3I','2025-01-11'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYzMTQ0MCwiZXhwIjoxNzM2NjMyMDQwfQ.qjwp5alJBfyStg7JPBlVyxJG_lnBdlQ-2zkaKHcjfCE','2025-01-11'),('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huLkRvZSIsImlhdCI6MTczNjYzMTU0OSwiZXhwIjoxNzM2NjMyMTQ5fQ.cQgOZhC0vcyBHP5PBH_NTLgMJFQCWj3bw0FkNACwaeE','2025-01-11');
/*!40000 ALTER TABLE `blacklist` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `trainee`
--

LOCK TABLES `trainee` WRITE;
/*!40000 ALTER TABLE `trainee` DISABLE KEYS */;
/*!40000 ALTER TABLE `trainee` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `trainee_trainer`
--

LOCK TABLES `trainee_trainer` WRITE;
/*!40000 ALTER TABLE `trainee_trainer` DISABLE KEYS */;
/*!40000 ALTER TABLE `trainee_trainer` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `trainer`
--

LOCK TABLES `trainer` WRITE;
/*!40000 ALTER TABLE `trainer` DISABLE KEYS */;
/*!40000 ALTER TABLE `trainer` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=1428 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training`
--

LOCK TABLES `training` WRITE;
/*!40000 ALTER TABLE `training` DISABLE KEYS */;
/*!40000 ALTER TABLE `training` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `trainingtype`
--

LOCK TABLES `trainingtype` WRITE;
/*!40000 ALTER TABLE `trainingtype` DISABLE KEYS */;
INSERT INTO `trainingtype` VALUES (1,'Yoga'),(2,'Pilates'),(3,'CrossFit'),(4,'Strength Training'),(5,'Cardio'),(6,'HIIT'),(7,'Aerobics'),(8,'Kickboxing'),(9,'Zumba'),(10,'Cycling');
/*!40000 ALTER TABLE `trainingtype` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=8656 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `usertries`
--

LOCK TABLES `usertries` WRITE;
/*!40000 ALTER TABLE `usertries` DISABLE KEYS */;
/*!40000 ALTER TABLE `usertries` ENABLE KEYS */;
UNLOCK TABLES;

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
