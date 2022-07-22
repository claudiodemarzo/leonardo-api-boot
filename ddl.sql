-- MySQL dump 10.13  Distrib 8.0.29, for Linux (x86_64)
--
-- Host: localhost    Database: leonardo
-- ------------------------------------------------------
-- Server version	8.0.29-0ubuntu0.20.04.3

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
-- Table structure for table `SPRING_SESSION`
--

DROP TABLE IF EXISTS `SPRING_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SPRING_SESSION` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint NOT NULL,
  `LAST_ACCESS_TIME` bigint NOT NULL,
  `MAX_INACTIVE_INTERVAL` int NOT NULL,
  `EXPIRY_TIME` bigint NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SPRING_SESSION_ATTRIBUTES`
--

DROP TABLE IF EXISTS `SPRING_SESSION_ATTRIBUTES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SPRING_SESSION_ATTRIBUTES` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `SPRING_SESSION` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `annunci_libri`
--

DROP TABLE IF EXISTS `annunci_libri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `annunci_libri` (
  `annuncio_id` int NOT NULL AUTO_INCREMENT,
  `isbn` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `prezzo` decimal(6,2) NOT NULL,
  `utente` int NOT NULL,
  `citta` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `stato` int NOT NULL DEFAULT '1',
  `livello_usura` char(1) NOT NULL,
  PRIMARY KEY (`annuncio_id`),
  KEY `citta` (`citta`),
  KEY `utente` (`utente`),
  KEY `annunci_libri_ibfk_3` (`isbn`),
  CONSTRAINT `annunci_libri_ibfk_1` FOREIGN KEY (`citta`) REFERENCES `citta` (`citta_id`),
  CONSTRAINT `annunci_libri_ibfk_2` FOREIGN KEY (`utente`) REFERENCES `utenti` (`utente_id`),
  CONSTRAINT `annunci_libri_ibfk_3` FOREIGN KEY (`isbn`) REFERENCES `libri` (`isbn`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `annunci_libri_chk_1` CHECK ((`prezzo` > 0)),
  CONSTRAINT `annunci_libri_chk_2` CHECK ((`stato` in (1,2,3))),
  CONSTRAINT `annunci_libri_chk_3` CHECK ((`livello_usura` in (_utf8mb4'n',_utf8mb4'o',_utf8mb4'b',_utf8mb4'd',_utf8mb4'a')))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `carrello`
--

DROP TABLE IF EXISTS `carrello`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrello` (
  `entry_id` int NOT NULL AUTO_INCREMENT,
  `utente` int NOT NULL,
  `annuncio` int NOT NULL,
  PRIMARY KEY (`entry_id`),
  UNIQUE KEY `carrello_uniquecostraint_1` (`utente`,`annuncio`),
  KEY `annuncio` (`annuncio`),
  CONSTRAINT `carrello_ibfk_1` FOREIGN KEY (`utente`) REFERENCES `utenti` (`utente_id`),
  CONSTRAINT `carrello_ibfk_2` FOREIGN KEY (`annuncio`) REFERENCES `annunci_libri` (`annuncio_id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chatroom`
--

DROP TABLE IF EXISTS `chatroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chatroom` (
  `chatroom_id` int NOT NULL AUTO_INCREMENT,
  `utente_mit` int NOT NULL,
  `utente_dest` int NOT NULL,
  PRIMARY KEY (`chatroom_id`),
  KEY `utente_mit` (`utente_mit`),
  KEY `utente_dest` (`utente_dest`),
  CONSTRAINT `chatroom_ibfk_1` FOREIGN KEY (`utente_mit`) REFERENCES `utenti` (`utente_id`),
  CONSTRAINT `chatroom_ibfk_2` FOREIGN KEY (`utente_dest`) REFERENCES `utenti` (`utente_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `citta`
--

DROP TABLE IF EXISTS `citta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `citta` (
  `citta_id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `lat` decimal(11,7) NOT NULL,
  `lng` decimal(11,7) NOT NULL,
  `provincia` int NOT NULL,
  `cap` char(5) NOT NULL,
  PRIMARY KEY (`citta_id`),
  UNIQUE KEY `nome` (`nome`),
  KEY `provincia` (`provincia`),
  CONSTRAINT `citta_ibfk_1` FOREIGN KEY (`provincia`) REFERENCES `provincia` (`provincia_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `citta_simple`
--

DROP TABLE IF EXISTS `citta_simple`;
/*!50001 DROP VIEW IF EXISTS `citta_simple`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `citta_simple` AS SELECT 
 1 AS `citta_id`,
 1 AS `nome`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `istituti`
--

DROP TABLE IF EXISTS `istituti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `istituti` (
  `istituto_id` int NOT NULL AUTO_INCREMENT,
  `codice_identificativo` varchar(10) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `citta` int NOT NULL,
  `path_logo` longtext NOT NULL,
  `lat` decimal(11,7) NOT NULL,
  `lng` decimal(11,7) NOT NULL,
  PRIMARY KEY (`istituto_id`),
  UNIQUE KEY `codice_identificativo` (`codice_identificativo`),
  KEY `citta` (`citta`),
  CONSTRAINT `istituti_ibfk_1` FOREIGN KEY (`citta`) REFERENCES `citta` (`citta_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `libri`
--

DROP TABLE IF EXISTS `libri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libri` (
  `libro_id` int NOT NULL AUTO_INCREMENT,
  `isbn` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nome` varchar(255) NOT NULL,
  `descrizione` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `copertina` text NOT NULL,
  `autori` text NOT NULL,
  `casaed` varchar(255) NOT NULL,
  `pagine` int NOT NULL,
  `categoria` varchar(255) NOT NULL,
  `prezzolistino` decimal(6,2) NOT NULL,
  `anno_pubblicazione` year NOT NULL,
  PRIMARY KEY (`isbn`),
  UNIQUE KEY `libro_id` (`libro_id`),
  CONSTRAINT `libri_chk_1` CHECK ((`pagine` > 0)),
  CONSTRAINT `libri_chk_2` CHECK ((`prezzolistino` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `livelli`
--

DROP TABLE IF EXISTS `livelli`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `livelli` (
  `livello_id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `soglia` int NOT NULL,
  PRIMARY KEY (`livello_id`),
  UNIQUE KEY `soglia` (`soglia`),
  CONSTRAINT `livelli_chk_1` CHECK ((`soglia` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `messaggi`
--

DROP TABLE IF EXISTS `messaggi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messaggi` (
  `messaggio_id` int NOT NULL AUTO_INCREMENT,
  `chatroom` int NOT NULL,
  `messaggio` text NOT NULL,
  `status` tinyint(1) NOT NULL,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`messaggio_id`),
  KEY `chatroom` (`chatroom`),
  CONSTRAINT `messaggi_ibfk_1` FOREIGN KEY (`chatroom`) REFERENCES `chatroom` (`chatroom_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `provincia`
--

DROP TABLE IF EXISTS `provincia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `provincia` (
  `provincia_id` int NOT NULL AUTO_INCREMENT,
  `nome` char(2) NOT NULL,
  `regione` int NOT NULL,
  PRIMARY KEY (`provincia_id`),
  UNIQUE KEY `nome` (`nome`),
  KEY `regione` (`regione`),
  CONSTRAINT `provincia_ibfk_1` FOREIGN KEY (`regione`) REFERENCES `regione` (`regione_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recensioni`
--

DROP TABLE IF EXISTS `recensioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recensioni` (
  `recensione_id` int NOT NULL AUTO_INCREMENT,
  `utente_recensito` int NOT NULL,
  `utente_recensore` int NOT NULL,
  `voto` int NOT NULL,
  PRIMARY KEY (`recensione_id`),
  KEY `utente_recensito` (`utente_recensito`),
  KEY `utente_recensore` (`utente_recensore`),
  CONSTRAINT `recensioni_ibfk_1` FOREIGN KEY (`utente_recensito`) REFERENCES `utenti` (`utente_id`),
  CONSTRAINT `recensioni_ibfk_2` FOREIGN KEY (`utente_recensore`) REFERENCES `utenti` (`utente_id`),
  CONSTRAINT `recensioni_chk_1` CHECK (((`voto` > 0) and (`voto` < 6)))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `regione`
--

DROP TABLE IF EXISTS `regione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `regione` (
  `regione_id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(70) NOT NULL,
  PRIMARY KEY (`regione_id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `tag_id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `descrizione` varchar(255) NOT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tags_utenti`
--

DROP TABLE IF EXISTS `tags_utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags_utenti` (
  `id_tagsutenti` int NOT NULL AUTO_INCREMENT,
  `utente` int NOT NULL,
  `tag` int NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_tagsutenti`),
  KEY `utente` (`utente`),
  KEY `tag` (`tag`),
  CONSTRAINT `tags_utenti_ibfk_1` FOREIGN KEY (`utente`) REFERENCES `utenti` (`utente_id`),
  CONSTRAINT `tags_utenti_ibfk_2` FOREIGN KEY (`tag`) REFERENCES `tags` (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `utenti`
--

DROP TABLE IF EXISTS `utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utenti` (
  `utente_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `username` varchar(30) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `telefono` char(10) DEFAULT NULL,
  `bio` longtext,
  `indirizzo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `nome` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `cognome` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `istituto` int DEFAULT NULL,
  `moreinfo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `punti` int NOT NULL DEFAULT '0',
  `foto` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'assets/imgs/utenti/default.webp',
  `email_confermata` tinyint(1) NOT NULL DEFAULT '0',
  `datanascita` datetime NOT NULL,
  `genere` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `cap` char(5) DEFAULT NULL,
  `citta` int DEFAULT NULL,
  `preferences` longtext,
  `reset_token` varchar(255) DEFAULT NULL,
  `confirm_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`utente_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `telefono` (`telefono`),
  KEY `utenti_ibfk_1` (`istituto`),
  KEY `utenti_ibfk_2` (`citta`),
  CONSTRAINT `utenti_ibfk_1` FOREIGN KEY (`istituto`) REFERENCES `istituti` (`istituto_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `utenti_ibfk_2` FOREIGN KEY (`citta`) REFERENCES `citta` (`citta_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `preferences_def_val` BEFORE INSERT ON `utenti` FOR EACH ROW set new.preferences = '{"avatar" : false, "avatarJson" : {} }' */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `def_val_foto` BEFORE INSERT ON `utenti` FOR EACH ROW set new.foto = '/assets/imgs/utenti/default.webp' */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Temporary view structure for view `utenti_avg_rating`
--

DROP TABLE IF EXISTS `utenti_avg_rating`;
/*!50001 DROP VIEW IF EXISTS `utenti_avg_rating`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `utenti_avg_rating` AS SELECT 
 1 AS `utente_id`,
 1 AS `avg_voto`,
 1 AS `nvoti`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `utenti_preferences`
--

DROP TABLE IF EXISTS `utenti_preferences`;
/*!50001 DROP VIEW IF EXISTS `utenti_preferences`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `utenti_preferences` AS SELECT 
 1 AS `utente_id`,
 1 AS `preferences`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `utenti_public_information`
--

DROP TABLE IF EXISTS `utenti_public_information`;
/*!50001 DROP VIEW IF EXISTS `utenti_public_information`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `utenti_public_information` AS SELECT 
 1 AS `utente_id`,
 1 AS `username`,
 1 AS `bio`,
 1 AS `nome`,
 1 AS `cognome`,
 1 AS `istituto`,
 1 AS `moreinfo`,
 1 AS `punti`,
 1 AS `foto`,
 1 AS `genere`,
 1 AS `cap`,
 1 AS `citta`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `citta_simple`
--

/*!50001 DROP VIEW IF EXISTS `citta_simple`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `citta_simple` AS select `citta`.`citta_id` AS `citta_id`,`citta`.`nome` AS `nome` from `citta` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `utenti_avg_rating`
--

/*!50001 DROP VIEW IF EXISTS `utenti_avg_rating`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `utenti_avg_rating` AS select `u`.`utente_id` AS `utente_id`,avg(`r`.`voto`) AS `avg_voto`,count(`r`.`voto`) AS `nvoti` from (`utenti` `u` join `recensioni` `r` on((`u`.`utente_id` = `r`.`utente_recensito`))) group by `r`.`utente_recensito` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `utenti_preferences`
--

/*!50001 DROP VIEW IF EXISTS `utenti_preferences`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `utenti_preferences` AS select `utenti`.`utente_id` AS `utente_id`,`utenti`.`preferences` AS `preferences` from `utenti` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `utenti_public_information`
--

/*!50001 DROP VIEW IF EXISTS `utenti_public_information`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `utenti_public_information` AS select `utenti`.`utente_id` AS `utente_id`,`utenti`.`username` AS `username`,`utenti`.`bio` AS `bio`,`utenti`.`nome` AS `nome`,`utenti`.`cognome` AS `cognome`,`utenti`.`istituto` AS `istituto`,`utenti`.`moreinfo` AS `moreinfo`,`utenti`.`punti` AS `punti`,`utenti`.`foto` AS `foto`,`utenti`.`genere` AS `genere`,`utenti`.`cap` AS `cap`,`utenti`.`citta` AS `citta` from `utenti` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-22 10:14:22
