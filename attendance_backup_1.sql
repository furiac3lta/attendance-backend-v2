-- MySQL dump 10.13  Distrib 9.5.0, for macos15.4 (arm64)
--
-- Host: localhost    Database: attendance_db
-- ------------------------------------------------------
-- Server version	8.4.6

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attendances`
--

DROP TABLE IF EXISTS `attendances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendances` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `attended` bit(1) NOT NULL,
  `taken_at` datetime(6) NOT NULL,
  `class_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `organization_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `taken_by_user_id` bigint NOT NULL,
  `has_debt` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8t063ywcvx9n4rt5rnsw1m1ws` (`student_id`,`class_id`),
  KEY `FKo5b3npj8o5o4mx6wfqdm2bhbx` (`class_id`),
  KEY `FKg8lphmp6k3q3l6gj7vl359tss` (`course_id`),
  KEY `FKgr77he5o3yi502h78024eebvs` (`organization_id`),
  KEY `FK6yswex6n0q72eqodg8tpqhiu5` (`taken_by_user_id`),
  CONSTRAINT `FK6yswex6n0q72eqodg8tpqhiu5` FOREIGN KEY (`taken_by_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK8rw9g2dvy0k41jwi5750bt1y5` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKg8lphmp6k3q3l6gj7vl359tss` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `FKgr77he5o3yi502h78024eebvs` FOREIGN KEY (`organization_id`) REFERENCES `organizations` (`id`),
  CONSTRAINT `FKo5b3npj8o5o4mx6wfqdm2bhbx` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendances`
--

LOCK TABLES `attendances` WRITE;
/*!40000 ALTER TABLE `attendances` DISABLE KEYS */;
INSERT INTO `attendances` VALUES (1,_binary '','2025-12-15 21:57:15.046639',30,1,1,27,81,_binary '\0'),(2,_binary '','2025-12-15 21:57:15.046666',30,1,1,39,81,_binary '\0'),(3,_binary '','2025-12-15 21:57:15.046672',30,1,1,51,81,_binary '\0'),(4,_binary '','2025-12-15 21:57:15.046680',30,1,1,63,81,_binary '\0'),(5,_binary '','2025-12-15 21:57:15.046686',30,1,1,75,81,_binary '\0'),(6,_binary '','2025-12-15 21:57:15.046693',30,1,1,76,81,_binary '\0'),(7,_binary '\0','2025-12-22 22:09:29.551773',31,2,1,2,2,_binary ''),(8,_binary '\0','2025-12-22 22:09:29.585653',31,2,1,26,2,_binary ''),(9,_binary '\0','2025-12-22 22:09:29.592762',31,2,1,38,2,_binary ''),(10,_binary '\0','2025-12-22 22:09:29.601915',31,2,1,50,2,_binary ''),(11,_binary '\0','2025-12-22 22:09:29.607990',31,2,1,62,2,_binary ''),(12,_binary '\0','2025-12-22 22:09:29.613711',31,2,1,74,2,_binary ''),(13,_binary '\0','2025-12-22 22:09:29.626507',31,2,1,77,2,_binary ''),(14,_binary '\0','2025-12-22 22:09:29.632769',31,2,1,78,2,_binary ''),(15,_binary '\0','2025-12-22 22:09:29.639869',31,2,1,79,2,_binary ''),(16,_binary '','2025-12-22 22:09:29.645973',31,2,1,80,2,_binary ''),(17,_binary '','2025-12-14 14:23:26.812863',33,1,1,27,81,_binary '\0'),(18,_binary '','2025-12-14 14:23:26.817791',33,1,1,39,81,_binary '\0'),(19,_binary '','2025-12-14 14:23:26.821515',33,1,1,51,81,_binary '\0'),(20,_binary '','2025-12-14 14:23:26.823444',33,1,1,63,81,_binary '\0'),(21,_binary '','2025-12-14 14:23:26.825242',33,1,1,75,81,_binary '\0'),(22,_binary '','2025-12-14 14:23:26.827084',33,1,1,76,81,_binary '\0'),(23,_binary '','2025-12-15 16:23:16.665727',32,3,1,25,81,_binary '\0'),(24,_binary '','2025-12-15 16:23:16.665748',32,3,1,37,81,_binary '\0'),(25,_binary '','2025-12-15 16:23:16.665755',32,3,1,49,81,_binary '\0'),(26,_binary '','2025-12-15 16:23:16.665761',32,3,1,61,81,_binary '\0'),(27,_binary '','2025-12-15 16:23:16.665769',32,3,1,73,81,_binary '\0'),(28,_binary '','2025-12-15 16:23:16.665776',32,3,1,77,81,_binary '\0'),(29,_binary '','2025-12-15 16:23:16.665785',32,3,1,81,81,_binary '\0'),(30,_binary '','2025-12-15 16:23:16.665794',32,3,1,83,81,_binary '\0'),(31,_binary '\0','2025-12-15 16:08:37.717277',34,3,1,25,81,_binary '\0'),(32,_binary '\0','2025-12-15 16:08:37.729761',34,3,1,37,81,_binary '\0'),(33,_binary '\0','2025-12-15 16:08:37.737417',34,3,1,49,81,_binary '\0'),(34,_binary '\0','2025-12-15 16:08:37.742715',34,3,1,61,81,_binary '\0'),(35,_binary '\0','2025-12-15 16:08:37.746422',34,3,1,73,81,_binary '\0'),(36,_binary '\0','2025-12-15 16:08:37.750202',34,3,1,77,81,_binary '\0'),(37,_binary '','2025-12-15 16:08:37.751850',34,3,1,81,81,_binary '\0'),(38,_binary '','2025-12-15 16:08:37.753955',34,3,1,83,81,_binary '\0'),(39,_binary '','2025-12-15 16:08:37.755999',34,3,1,84,81,_binary '\0'),(40,_binary '','2025-12-22 01:47:04.492087',36,1,1,27,2,_binary '\0'),(41,_binary '','2025-12-22 01:47:04.500474',36,1,1,39,2,_binary ''),(42,_binary '','2025-12-22 01:47:04.506913',36,1,1,51,2,_binary ''),(43,_binary '\0','2025-12-22 01:47:04.512791',36,1,1,63,2,_binary '\0'),(44,_binary '\0','2025-12-22 01:47:04.522580',36,1,1,75,2,_binary '\0'),(45,_binary '','2025-12-22 01:47:04.527180',36,1,1,76,2,_binary '\0'),(46,_binary '','2025-12-17 17:11:57.397492',37,1,1,27,81,_binary ''),(47,_binary '','2025-12-17 17:11:57.405760',37,1,1,39,81,_binary ''),(48,_binary '','2025-12-17 17:11:57.412375',37,1,1,51,81,_binary ''),(49,_binary '','2025-12-17 17:11:57.417616',37,1,1,63,81,_binary ''),(50,_binary '','2025-12-17 17:11:57.421893',37,1,1,75,81,_binary '\0'),(51,_binary '','2025-12-17 17:11:57.433224',37,1,1,76,81,_binary '\0'),(52,_binary '','2025-12-17 17:11:57.438291',37,1,1,85,81,_binary '\0'),(53,_binary '','2025-12-21 20:16:37.923632',40,1,1,27,2,_binary '\0'),(54,_binary '','2025-12-21 20:16:37.939507',40,1,1,39,2,_binary ''),(55,_binary '','2025-12-21 20:16:37.944455',40,1,1,51,2,_binary ''),(56,_binary '','2025-12-21 20:16:37.948728',40,1,1,63,2,_binary ''),(57,_binary '','2025-12-21 20:16:37.953281',40,1,1,75,2,_binary '\0'),(58,_binary '','2025-12-21 20:16:37.957175',40,1,1,76,2,_binary '\0'),(59,_binary '','2025-12-21 20:16:37.961294',40,1,1,85,2,_binary '\0'),(60,_binary '','2025-12-21 20:16:50.136900',39,1,1,27,2,_binary '\0'),(61,_binary '','2025-12-21 20:16:50.145342',39,1,1,39,2,_binary ''),(62,_binary '','2025-12-21 20:16:50.151989',39,1,1,51,2,_binary ''),(63,_binary '','2025-12-21 20:16:50.159702',39,1,1,63,2,_binary ''),(64,_binary '','2025-12-21 20:16:50.167893',39,1,1,75,2,_binary '\0'),(65,_binary '','2025-12-21 20:16:50.178569',39,1,1,76,2,_binary '\0'),(66,_binary '','2025-12-21 20:16:50.197951',39,1,1,85,2,_binary '\0'),(69,_binary '','2025-12-22 01:26:41.080311',41,1,1,27,2,_binary '\0'),(70,_binary '','2025-12-22 01:26:41.088011',41,1,1,39,2,_binary ''),(71,_binary '','2025-12-22 01:26:41.096252',41,1,1,51,2,_binary ''),(72,_binary '','2025-12-22 01:26:41.108194',41,1,1,63,2,_binary '\0'),(73,_binary '','2025-12-22 01:26:41.114530',41,1,1,75,2,_binary '\0'),(74,_binary '','2025-12-22 01:26:41.118807',41,1,1,76,2,_binary '\0'),(75,_binary '','2025-12-22 01:26:41.124531',41,1,1,85,2,_binary '\0'),(78,_binary '','2026-01-07 12:29:42.553549',42,12,5,16,1,_binary ''),(79,_binary '','2026-01-07 12:29:42.561217',42,12,5,28,1,_binary ''),(80,_binary '','2026-01-07 12:29:42.568113',42,12,5,40,1,_binary ''),(81,_binary '','2026-01-07 12:29:42.574707',42,12,5,52,1,_binary ''),(82,_binary '','2026-01-07 12:29:42.583321',42,12,5,64,1,_binary ''),(83,_binary '','2026-01-10 11:42:53.638305',43,1,1,27,1,_binary '\0'),(84,_binary '','2026-01-10 11:42:53.646440',43,1,1,39,1,_binary ''),(85,_binary '','2026-01-10 11:42:53.650093',43,1,1,51,1,_binary ''),(86,_binary '','2026-01-10 11:42:53.654521',43,1,1,63,1,_binary '\0'),(87,_binary '','2026-01-10 11:42:53.660236',43,1,1,75,1,_binary '\0'),(88,_binary '','2026-01-10 11:42:53.664765',43,1,1,76,1,_binary '\0'),(89,_binary '','2026-01-10 11:42:53.670747',43,1,1,85,1,_binary '\0'),(90,_binary '\0','2026-01-10 11:43:04.623638',29,1,1,27,1,_binary '\0'),(91,_binary '\0','2026-01-10 11:43:04.629139',29,1,1,39,1,_binary ''),(92,_binary '\0','2026-01-10 11:43:04.634605',29,1,1,51,1,_binary ''),(93,_binary '\0','2026-01-10 11:43:04.640051',29,1,1,63,1,_binary '\0'),(94,_binary '\0','2026-01-10 11:43:04.645130',29,1,1,75,1,_binary '\0'),(95,_binary '\0','2026-01-10 11:43:04.652492',29,1,1,76,1,_binary '\0'),(96,_binary '\0','2026-01-10 11:43:04.658706',29,1,1,85,1,_binary '\0'),(97,_binary '\0','2026-01-10 11:43:13.004843',35,1,1,27,1,_binary '\0'),(98,_binary '\0','2026-01-10 11:43:13.019451',35,1,1,39,1,_binary ''),(99,_binary '\0','2026-01-10 11:43:13.026373',35,1,1,51,1,_binary ''),(100,_binary '\0','2026-01-10 11:43:13.031203',35,1,1,63,1,_binary '\0'),(101,_binary '\0','2026-01-10 11:43:13.037312',35,1,1,75,1,_binary '\0'),(102,_binary '\0','2026-01-10 11:43:13.043086',35,1,1,76,1,_binary '\0'),(103,_binary '\0','2026-01-10 11:43:13.049094',35,1,1,85,1,_binary '\0'),(104,_binary '','2026-01-14 02:42:51.308228',44,1,1,27,1,_binary '\0'),(105,_binary '','2026-01-14 02:42:51.316923',44,1,1,39,1,_binary ''),(106,_binary '','2026-01-14 02:42:51.329897',44,1,1,51,1,_binary ''),(107,_binary '','2026-01-14 02:42:51.338895',44,1,1,63,1,_binary ''),(108,_binary '','2026-01-14 02:42:51.345478',44,1,1,75,1,_binary ''),(109,_binary '','2026-01-14 02:42:51.354484',44,1,1,76,1,_binary ''),(110,_binary '','2026-01-14 03:25:23.722781',45,1,1,27,1,_binary '\0'),(111,_binary '','2026-01-14 03:25:23.738362',45,1,1,39,1,_binary ''),(112,_binary '\0','2026-01-14 03:25:23.745657',45,1,1,51,1,_binary ''),(113,_binary '\0','2026-01-14 03:25:23.751436',45,1,1,63,1,_binary ''),(114,_binary '\0','2026-01-14 03:25:23.757912',45,1,1,75,1,_binary ''),(115,_binary '\0','2026-01-14 03:25:23.762773',45,1,1,76,1,_binary ''),(116,_binary '','2026-01-14 03:26:06.432649',46,1,1,27,1,_binary '\0'),(117,_binary '','2026-01-14 03:26:06.439464',46,1,1,39,1,_binary ''),(118,_binary '','2026-01-14 03:26:06.443986',46,1,1,51,1,_binary ''),(119,_binary '\0','2026-01-14 03:26:06.447985',46,1,1,63,1,_binary ''),(120,_binary '\0','2026-01-14 03:26:06.456786',46,1,1,75,1,_binary ''),(121,_binary '\0','2026-01-14 03:26:06.460896',46,1,1,76,1,_binary ''),(122,_binary '','2026-01-14 08:39:29.979423',49,1,1,27,1,_binary '\0'),(123,_binary '','2026-01-14 08:39:29.988213',49,1,1,39,1,_binary ''),(124,_binary '','2026-01-14 08:39:29.995841',49,1,1,51,1,_binary ''),(125,_binary '','2026-01-14 08:39:30.002603',49,1,1,63,1,_binary ''),(126,_binary '','2026-01-14 08:39:30.008928',49,1,1,75,1,_binary ''),(127,_binary '','2026-01-14 08:39:30.016463',49,1,1,76,1,_binary '');
/*!40000 ALTER TABLE `attendances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classes`
--

DROP TABLE IF EXISTS `classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `classes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `instructor_id` bigint DEFAULT NULL,
  `organization_id` bigint DEFAULT NULL,
  `observations` text,
  PRIMARY KEY (`id`),
  KEY `FK9v6ijeybapa0ontdtd4o4rycs` (`course_id`),
  KEY `FKlcfr0teple6ibcs3on84yubkh` (`instructor_id`),
  KEY `FKnxp2q3haifva19xpmw88efi7j` (`organization_id`),
  CONSTRAINT `FK9v6ijeybapa0ontdtd4o4rycs` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `FKlcfr0teple6ibcs3on84yubkh` FOREIGN KEY (`instructor_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKnxp2q3haifva19xpmw88efi7j` FOREIGN KEY (`organization_id`) REFERENCES `organizations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classes`
--

LOCK TABLES `classes` WRITE;
/*!40000 ALTER TABLE `classes` DISABLE KEYS */;
INSERT INTO `classes` VALUES (29,'2025-12-13','Clase del 13/12/2025',1,81,1,NULL),(30,'2025-12-13','Clase del 13/12/2025',1,81,1,NULL),(31,'2025-12-14','Clase del 13/12/2025',2,81,1,NULL),(32,'2025-12-14','Clase nueva',3,81,1,NULL),(33,'2025-12-14','Clase nueva',1,81,1,NULL),(34,'2025-12-14','Clase nueva',3,81,1,NULL),(35,'2025-12-15','Clase nueva',1,81,1,NULL),(36,'2025-12-15','Clase nueva',1,81,1,NULL),(37,'2025-12-17','Clase nueva',1,81,1,NULL),(38,'2025-12-17','Clase nueva',1,81,1,NULL),(39,'2025-12-19','Clase nueva',1,81,1,NULL),(40,'2025-12-21','Clase nueva',1,81,1,NULL),(41,'2025-12-22','Clase nueva',1,81,1,NULL),(42,'2026-01-07','Clase nueva',12,15,5,NULL),(43,'2026-01-10','Clase nueva',1,81,1,NULL),(44,'2026-01-14','Clase nueva',1,81,1,NULL),(45,'2026-01-14','Clase nueva',1,81,1,NULL),(46,'2026-01-14','Clase nueva',1,81,1,NULL),(47,'2026-01-14','Clase nueva',1,81,1,NULL),(48,'2026-01-14','Clase nueva',1,81,1,NULL),(49,'2026-01-14','Clase nueva',1,81,1,'clase. especial cumple de marce');
/*!40000 ALTER TABLE `classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `university_program` varchar(50) DEFAULT NULL,
  `instructor_id` bigint NOT NULL,
  `organization_id` bigint NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `FKcyfum8goa6q5u13uog0563gyp` (`instructor_id`),
  KEY `FK43ek3wmpt0a6qrhw5s8ajbl4h` (`organization_id`),
  CONSTRAINT `FK43ek3wmpt0a6qrhw5s8ajbl4h` FOREIGN KEY (`organization_id`) REFERENCES `organizations` (`id`),
  CONSTRAINT `FKcyfum8goa6q5u13uog0563gyp` FOREIGN KEY (`instructor_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,'Lunes & Miércoles 18:30–19:30','BJJ Infantil PR','Misiones',81,1,1),(2,'Lunes & Miércoles 18:00–19:30','BJJ Adultos','misiones',7,1,1),(3,'Lunes & Miércoles 18:00–19:30','BJJ Competidores',NULL,81,1,1),(4,'Lunes & Miércoles 20:00–21:30','BJJ Adultos',NULL,9,2,1),(5,'Lunes & Miércoles 20:00–21:30','BJJ Femenino Defensa Personal',NULL,10,2,1),(6,'Lunes & Miércoles 20:00–21:30','BJJ Competidores Junior',NULL,10,2,1),(7,'Martes & Jueves 18:00–19:00','Taekwondo Niños',NULL,12,3,1),(8,'Martes & Jueves 18:00–19:00','Taekwondo Adultos',NULL,11,3,1),(9,'Martes & Jueves 20:00–21:00','Taekwondo Niños',NULL,14,4,1),(10,'Martes & Jueves 20:00–21:00','Taekwondo Adultos',NULL,13,4,1),(11,'Control de asistencia Lun–Vie 08:00–18:00','Producción',NULL,15,5,1),(12,'Control de asistencia Lun–Vie 08:00–18:00','Administración',NULL,15,5,1);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollments`
--

DROP TABLE IF EXISTS `enrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `start_date` date NOT NULL,
  `course_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKg1muiskd02x66lpy6fqcj6b9q` (`user_id`,`course_id`),
  KEY `FKho8mcicp4196ebpltdn9wl6co` (`course_id`),
  CONSTRAINT `FK3hjx6rcnbmfw368sxigrpfpx0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKho8mcicp4196ebpltdn9wl6co` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollments`
--

LOCK TABLES `enrollments` WRITE;
/*!40000 ALTER TABLE `enrollments` DISABLE KEYS */;
INSERT INTO `enrollments` VALUES (1,_binary '','2025-12-15',1,75),(2,_binary '','2025-12-20',1,27),(3,_binary '','2025-12-22',1,63),(4,_binary '','2026-01-13',3,61),(5,_binary '','2026-01-13',2,26);
/*!40000 ALTER TABLE `enrollments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organizations`
--

DROP TABLE IF EXISTS `organizations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organizations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `logo_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `admin_id` bigint DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `FK1fx15mcfjewvjc2eh0j7n4ycm` (`admin_id`),
  CONSTRAINT `FK1fx15mcfjewvjc2eh0j7n4ycm` FOREIGN KEY (`admin_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organizations`
--

LOCK TABLES `organizations` WRITE;
/*!40000 ALTER TABLE `organizations` DISABLE KEYS */;
INSERT INTO `organizations` VALUES (1,'Puerto Rico, Misiones','2025-11-09 22:43:55.000000',NULL,'Irmãos Club Puerto Rico','3743-000111','BJJ','2025-12-15 12:10:31.961631',2,1),(2,'Jardín América, Misiones','2025-11-09 22:43:55.000000',NULL,'Irmãos Club Jardín América','3743-000222','BJJ','2025-11-10 01:48:21.160563',3,1),(3,'Puerto Rico, Misiones','2025-11-09 22:43:55.000000',NULL,'Dojang Puerto Rico','3743-000333','Taekwondo','2025-11-10 01:48:28.409368',4,1),(4,'Jardín América, Misiones','2025-11-09 22:43:55.000000',NULL,'Dojang Jardín América','3743-000444','Taekwondo','2025-11-10 01:48:34.964744',6,1),(5,'Puerto Rico, Misiones','2025-11-09 22:43:55.000000',NULL,'Innova S.A','3743-000555','EMPRESA','2025-11-10 01:48:39.440720',5,1),(6,'Los alerces 162','2026-01-07 12:28:20.238829','','Irmaos Garhuape','3743-453423','Academia','2026-01-07 12:28:20.238865',NULL,1);
/*!40000 ALTER TABLE `organizations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) DEFAULT NULL,
  `method` enum('CASH','MERCADOPAGO','OTHER','TRANSFER') DEFAULT NULL,
  `month` int NOT NULL,
  `paid_at` datetime(6) DEFAULT NULL,
  `status` enum('PAID','UNPAID') DEFAULT NULL,
  `year` int NOT NULL,
  `course_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7mrrk0kt6xn9m3ywhixwvhfe4` (`student_id`,`month`,`year`),
  KEY `FK8nlm4urshp5drsk0nlkprig36` (`course_id`),
  CONSTRAINT `FK8nlm4urshp5drsk0nlkprig36` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `FKdn7tvyxt0pb47kudo6f97jauk` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,25000.00,'CASH',12,'2025-12-14 18:12:22.408563','PAID',2025,3,25),(2,25000.00,'CASH',12,'2025-12-15 14:07:42.375773','PAID',2025,3,84),(3,25000.00,'CASH',12,'2025-12-15 16:46:14.245506','PAID',2025,1,85),(4,25000.00,'CASH',12,'2025-12-15 19:40:06.116741','PAID',2025,1,76),(5,25000.00,'CASH',12,'2025-12-15 19:45:41.968827','PAID',2025,1,75),(6,25000.00,'CASH',12,'2025-12-20 22:40:26.820470','PAID',2025,1,27),(7,25000.00,'CASH',12,'2025-12-22 01:23:12.535736','PAID',2025,1,63),(8,30000.00,'CASH',1,'2026-01-10 17:44:16.621722','PAID',2026,1,27),(9,30000.00,'CASH',1,'2026-01-13 13:52:24.039682','PAID',2026,3,61),(10,30000.00,'CASH',1,'2026-01-13 13:52:48.683322','PAID',2026,2,26);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_courses`
--

DROP TABLE IF EXISTS `user_courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_courses` (
  `user_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`course_id`),
  KEY `FKb84hga2qpwc4vv44lmyb8mwux` (`course_id`),
  CONSTRAINT `FK5i2mwg17kvpk92fy6cdii93da` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKb84hga2qpwc4vv44lmyb8mwux` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_courses`
--

LOCK TABLES `user_courses` WRITE;
/*!40000 ALTER TABLE `user_courses` DISABLE KEYS */;
INSERT INTO `user_courses` VALUES (27,1),(39,1),(51,1),(63,1),(75,1),(76,1),(85,1),(26,2),(38,2),(50,2),(62,2),(74,2),(77,2),(79,2),(110,2),(37,3),(49,3),(61,3),(73,3),(77,3),(83,3),(84,3),(24,4),(36,4),(48,4),(60,4),(72,4),(23,5),(35,5),(47,5),(59,5),(71,5),(22,6),(34,6),(46,6),(58,6),(70,6),(21,7),(33,7),(45,7),(57,7),(69,7),(106,7),(107,7),(108,7),(109,7),(18,8),(20,8),(32,8),(44,8),(56,8),(68,8),(18,9),(19,9),(31,9),(43,9),(55,9),(67,9),(18,10),(30,10),(42,10),(54,10),(66,10),(17,11),(29,11),(41,11),(53,11),(65,11),(16,12),(28,12),(40,12),(52,12),(64,12);
/*!40000 ALTER TABLE `user_courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','INSTRUCTOR','SUPER_ADMIN','USER') NOT NULL,
  `organization_id` bigint DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `observations` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `FKqpugllwvyv37klq7ft9m8aqxk` (`organization_id`),
  CONSTRAINT `FKqpugllwvyv37klq7ft9m8aqxk` FOREIGN KEY (`organization_id`) REFERENCES `organizations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'superadmin@irmaos.com','Super Admin','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','SUPER_ADMIN',1,1,NULL),(2,'admin.bjj.pr@dojo.com','admin bjj puerto rico','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','ADMIN',1,1,NULL),(3,'admin.bjj.ja@dojo.com','admin bjj jardin america','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','ADMIN',1,1,NULL),(4,'admin.tkd.pr@dojo.com','admin tkd puerto rico','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','ADMIN',3,1,NULL),(5,'admin.innova@empresa.com','admin innova','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','ADMIN',5,1,NULL),(6,'admin.tkd.ja@dojo.com','admin tkd jardin america','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','ADMIN',4,1,NULL),(7,'marcos.oliveira@dojo.com','Marcos Oliveira','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','INSTRUCTOR',1,1,NULL),(8,'javier.martinez@dojo.com','Javier Martínez','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','INSTRUCTOR',1,1,NULL),(9,'luis.ferreira@dojo.com','Luis Ferreira','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','INSTRUCTOR',2,1,NULL),(10,'andres.rodriguez@dojo.com','Andrés Rodríguez','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','INSTRUCTOR',2,1,NULL),(11,'carlos.gomez@dojo.com','Carlos Gómez','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','INSTRUCTOR',3,1,NULL),(12,'matias.herrera@dojo.com','Matías Herrera','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','INSTRUCTOR',3,1,NULL),(13,'diego.lopez@dojo.com','Diego López','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','INSTRUCTOR',4,1,NULL),(14,'martin.ruiz@dojo.com','Martín Ruiz','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','INSTRUCTOR',4,1,NULL),(15,'laura.gonzalez@innova.com','Laura González','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','INSTRUCTOR',5,1,NULL),(16,'alumno60.c12@dojo.com','Alumno60 Curso12','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',5,1,NULL),(17,'alumno55.c11@dojo.com','Alumno55 Curso11','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',5,1,NULL),(18,'alumno50.c10@dojo.com','Alumno50 Curso10','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',4,1,NULL),(19,'alumno42.c9@dojo.com','Alumno42 Curso9','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',4,1,NULL),(20,'alumno37.c8@dojo.com','Alumno37 Curso8','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',3,1,NULL),(21,'alumno35.c7@dojo.com','Alumno35 Curso7','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',3,1,NULL),(22,'alumno27.c6@dojo.com','Alumno27 Curso6','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(23,'alumno22.c5@dojo.com','Alumno22 Curso5','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(24,'alumno16.c4@dojo.com','Alumno16 Curso4','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(25,'alumno12.c3@dojo.com','Alumno12 Curso3','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,0,NULL),(26,'alumno6.c2@dojo.com','Alumno6 Curso2','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(27,'alumno5.c1@dojo.com','Alumno5 Curso1','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(28,'alumno56.c12@dojo.com','Alumno56 Curso12','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',5,1,NULL),(29,'alumno51.c11@dojo.com','Alumno51 Curso11','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',5,1,NULL),(30,'alumno48.c10@dojo.com','Alumno48 Curso10','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',4,1,NULL),(31,'alumno43.c9@dojo.com','Alumno43 Curso9','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',4,1,NULL),(32,'alumno38.c8@dojo.com','Alumno38 Curso8','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',3,1,NULL),(33,'alumno32.c7@dojo.com','Alumno32 Curso7','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',3,1,NULL),(34,'alumno28.c6@dojo.com','Alumno28 Curso6','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(35,'alumno24.c5@dojo.com','Alumno24 Curso5','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(36,'alumno19.c4@dojo.com','Alumno19 Curso4','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(37,'alumno14.c3@dojo.com','Alumno14 Curso3','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(38,'alumno8.c2@dojo.com','Alumno8 Curso2','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(39,'alumno1.c1@dojo.com','Alumno1 Curso1','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(40,'alumno59.c12@dojo.com','Alumno59 Curso12','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',5,1,NULL),(41,'alumno54.c11@dojo.com','Alumno54 Curso11','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',5,1,NULL),(42,'alumno46.c10@dojo.com','Alumno46 Curso10','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',4,1,NULL),(43,'alumno41.c9@dojo.com','Alumno41 Curso9','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',4,1,NULL),(44,'alumno36.c8@dojo.com','Alumno36 Curso8','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',3,1,NULL),(45,'alumno31.c7@dojo.com','Alumno31 Curso7','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',3,1,NULL),(46,'alumno26.c6@dojo.com','Alumno26 Curso6','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(47,'alumno25.c5@dojo.com','Alumno25 Curso5','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(48,'alumno18.c4@dojo.com','Alumno18 Curso4','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(49,'alumno13.c3@dojo.com','Alumno13 Curso3','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(50,'alumno9.c2@dojo.com','Alumno9 Curso2','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(51,'alumno3.c1@dojo.com','Alumno3 Curso1','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(52,'alumno58.c12@dojo.com','Alumno58 Curso12','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',5,1,NULL),(53,'alumno53.c11@dojo.com','Alumno53 Curso11','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',5,1,NULL),(54,'alumno49.c10@dojo.com','Alumno49 Curso10','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',4,1,NULL),(55,'alumno44.c9@dojo.com','Alumno44 Curso9','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',4,1,NULL),(56,'alumno40.c8@dojo.com','Alumno40 Curso8','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',3,1,NULL),(57,'alumno34.c7@dojo.com','Alumno34 Curso7','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',3,1,NULL),(58,'alumno30.c6@dojo.com','Alumno30 Curso6','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(59,'alumno21.c5@dojo.com','Alumno21 Curso5','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(60,'alumno17.c4@dojo.com','Alumno17 Curso4','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(61,'alumno11.c3@dojo.com','Alumno11 Curso3','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(62,'alumno10.c2@dojo.com','Alumno10 Curso2','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(63,'alumno2.c1@dojo.com','Alumno2 Curso1','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(64,'alumno57.c12@dojo.com','Alumno57 Curso12','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',5,1,NULL),(65,'alumno52.c11@dojo.com','Alumno52 Curso11','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',5,1,NULL),(66,'alumno47.c10@dojo.com','Alumno47 Curso10','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',4,1,NULL),(67,'alumno45.c9@dojo.com','Alumno45 Curso9','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',4,1,NULL),(68,'alumno39.c8@dojo.com','Alumno39 Curso8','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',3,1,NULL),(69,'alumno33.c7@dojo.com','Alumno33 Curso7','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',3,1,NULL),(70,'alumno29.c6@dojo.com','Alumno29 Curso6','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(71,'alumno23.c5@dojo.com','Alumno23 Curso5','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(72,'alumno20.c4@dojo.com','Alumno20 Curso4','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',2,1,NULL),(73,'alumno15.c3@dojo.com','Alumno15 Curso3','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(74,'alumno7.c2@dojo.com','Alumno7 Curso2','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(75,'alumno4.c1@dojo.com','Alumno4 Curso1','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(76,'marce@gmail.com','marce','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(77,'ale@gmail.com','ale','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(78,'adminregional@gmail.com','adminregional','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','ADMIN',1,1,NULL),(79,'sonnydu@gmail.com','sonia duarte','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(80,'marcec@admin.com','MarceC','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','ADMIN',1,1,NULL),(81,'marceins@gmail.com','Marce Colum','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','INSTRUCTOR',1,1,NULL),(82,'nuevoadmin@admin.com','nuevoadmin','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','ADMIN',1,1,NULL),(83,'alumnonuevo@gmail.com','alumnonuevo','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(84,'fb@gmail.com','fabio gonzales','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,1,NULL),(85,'otrousuario@gmail.com','otrousuario','$2a$10$qSNOWcs8ThANFvg97gWH4.KV8GkGVzoRrYFZ0jBLLag7q6jKBBHVe','USER',1,0,NULL),(86,'juan.perez@example.com','Juan Perez','$2a$10$1ZGuYkwK19xP6YeTsqHy1eweJ.iAE5LBAnYcoDoklJI.Ndk89yyTq','USER',3,1,NULL),(87,'maria.lopez@example.com','Maria Lopez','$2a$10$B.IPCzEzrEm.v0SXsJ2urOcNgzPU91EaoPkQXbKinuAHZrQp7FRRe','USER',3,1,NULL),(88,'carlos.g@example.com','Carlos Gomez','$2a$10$QSeZhq0Ri7wfnzm/xWoFGefB9ncumXTkSuhnkj2.KzHNaYbJktSDm','USER',3,1,NULL),(89,'cinthia.g@example.com','Cinthia Alvarez','$2a$10$4vGNjjo.m8omQNHKx5dRRuuUFHA0fuXhLufrdZ4hUFBjvpXJBzuN6','USER',3,1,NULL),(90,'juan.perez1@example.com','Juan P','$2a$10$d5eYMWr5EqPELEKhr4.VTOfwS5ifgUUopapR0BQeJZmZXOQxQoXCS','USER',3,1,NULL),(91,'maria.lopez1@example.com','Maria L','$2a$10$tNKTbxNF4EXSOmj27cTYj.6u.1Mlv8ueLX9JzvA0j8B9Fxapm8/Ke','USER',3,1,NULL),(92,'carlos.g1@example.com','Carlos G','$2a$10$8Bh77Sa626OhWXeKxJzoE.6VYuNsd9klw5gQxt62Y7BjtEIMPVPbq','USER',3,1,NULL),(93,'cinthia.g1@example.com','Cinthia A','$2a$10$1wJWUZ/iKi76CIoSqALwvOl37wfPREzLRMwbB/KtPieN0qSUfkWYS','USER',3,1,NULL),(94,'juan.perez2@example.com','Juan P2','$2a$10$38z3vQTKgp4J81LGuZxZiuKZylBBkjJ8EgyeiKGHxXW4wm.b9wR1q','USER',1,1,NULL),(95,'maria.lopez2@example.com','Maria L2','$2a$10$b4tCZgcMcoHZxGfFT6fUQ.ntUhdER/WlFyCfEkXmTJLebd9Wp5toa','USER',1,1,NULL),(96,'carlos.g2@example.com','Carlos G2','$2a$10$vDfFgG6jLg2BJKbvUrDmZukTMxGgVtCNaJdEgcHJdcgpS1YInTDEO','USER',1,1,NULL),(97,'cinthia.g2@example.com','Cinthia A2','$2a$10$XuaTFi4mgSDiBJ0og0Vm..qe1HdTSR9RW7GMAKbsVp2LjJwHYo0ku','USER',1,1,NULL),(98,'juan.perez3@example.com','Juan P3','$2a$10$Xp4uK2Z5Pvg4/TLOPAo3HeEiiUqwAo/BjSrcOkiaMn9cE2tusI/Wu','USER',3,1,NULL),(99,'maria.lopez3@example.com','Maria L3','$2a$10$PWXd7Jg/hcPiTtDlLp2R.eACBUrFEgOk1U8OPI2hNdAY8ZSAg.B.C','USER',3,1,NULL),(100,'carlos.g3@example.com','Carlos G3','$2a$10$ucd1Y2VD8W05jlSe/9RQweric7dlh0wfXQAUAveu/lY6423JswVF.','USER',3,1,NULL),(101,'cinthia.g3@example.com','Cinthia A3','$2a$10$PzBD5EnMaRdmQ2THFwFPW.gz0MUfndSbwTlDBzXkC4gaNsJ0311ee','USER',3,1,NULL),(102,'juan.perez4@example.com','Juan P4','$2a$10$/I9w1mNToCxigtvRrXMThuguWkA47BoIvH2.kHnYvVbr0f0bfUiAK','USER',3,1,NULL),(103,'maria.lopez4@example.com','Maria L4','$2a$10$6fL3I0C1eKCyiHVgc2e/JOUGsqE0LjA4NGBvHltYUv16FYxwMLbPe','USER',3,1,NULL),(104,'carlos.g4@example.com','Carlos G4','$2a$10$FzgXygmYUfugsb.ddu7zLOKtaAVt0gDSsmuCJy2ZsU6zpl6YoyU.W','USER',3,1,NULL),(105,'cinthia.g4@example.com','Cinthia A4','$2a$10$Qp0x39mnq/XjuJ1WdaNA3euvQmLGwmqRelIB1F84GE0mU36ovqbY2','USER',3,1,NULL),(106,'juan.perez45@example.com','Juan P5','$2a$10$j2VT.mrIDWwFBjrgYbXW1O2d1n0zQ7IrQ.tEVZO3lg0Ax8E2GsXqK','USER',3,1,NULL),(107,'maria.lopez45@example.com','Maria L5','$2a$10$ZUOyVo4OzbAhOIgq67fMBOiNjapgSyfJbXvObATj752xhIGNFtQ.2','USER',3,1,NULL),(108,'carlos.g45@example.com','Carlos G5','$2a$10$bCXygclzPVbUAU5gAq6Q2.F8RttmbU/1LUdLpKM822V1ByouGejhC','USER',3,1,NULL),(109,'cinthia.g45@example.com','Cinthia A5','$2a$10$e2kUQlE5DsxtNRhaCqPVeuXn8LFpnwHJhX0MLB4wCY2Sq2ZZznYfy','USER',3,1,NULL),(110,'martin.colum@gmail.com','Martin Colum','$2a$10$tFyAk9PZlU/idXHlMonm7O57U1KbAZ5nsUFFBLh/X.QekMJ4djurW','USER',1,1,'recien empieza');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-15  0:13:45
