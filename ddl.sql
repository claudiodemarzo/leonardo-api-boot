-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Creato il: Ago 31, 2022 alle 11:55
-- Versione del server: 8.0.30-0ubuntu0.20.04.2
-- Versione PHP: 8.1.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `leonardo`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `annunci_libri`
--

CREATE TABLE IF NOT EXISTS `annunci_libri` (
  `annuncio_id` int NOT NULL AUTO_INCREMENT,
  `isbn` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `prezzo` decimal(6,2) NOT NULL,
  `utente` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `stato` int NOT NULL DEFAULT '1',
  `livello_usura` char(1) NOT NULL,
  `status_libro` int NOT NULL,
  `sold_to` int DEFAULT NULL,
  PRIMARY KEY (`annuncio_id`),
  KEY `annunci_libri_ibfk_2` (`utente`),
  KEY `annunci_libri_ibfk_3` (`isbn`),
  KEY `annunci_libri_ibfk_4` (`status_libro`),
  KEY `annunci_libri_ibfk_5` (`sold_to`)
) ;

-- --------------------------------------------------------

--
-- Struttura stand-in per le viste `annunci_senza_recensione`
-- (Vedi sotto per la vista effettiva)
--
CREATE TABLE IF NOT EXISTS `annunci_senza_recensione` (
`annuncio_id` int
,`isbn` varchar(13)
,`prezzo` decimal(6,2)
,`utente` int
,`created_at` datetime
,`stato` int
,`livello_usura` char(1)
,`status_libro` int
,`sold_to` int
,`recensione_id` int
,`utente_recensito` int
,`utente_recensore` int
,`annuncio` int
,`voto` int
,`commento` text
);

-- --------------------------------------------------------

--
-- Struttura della tabella `cap_citta`
--

CREATE TABLE IF NOT EXISTS `cap_citta` (
  `id_ass` int NOT NULL AUTO_INCREMENT,
  `citta_id` int NOT NULL,
  `cap` char(5) NOT NULL,
  PRIMARY KEY (`id_ass`),
  KEY `citta_id_ibfk_1` (`citta_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `carrello`
--

CREATE TABLE IF NOT EXISTS `carrello` (
  `entry_id` int NOT NULL AUTO_INCREMENT,
  `utente` int NOT NULL,
  `annuncio` int NOT NULL,
  PRIMARY KEY (`entry_id`),
  UNIQUE KEY `carrello_uniquecostraint_1` (`utente`,`annuncio`),
  KEY `carrello_ibfk_2` (`annuncio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `chatroom`
--

CREATE TABLE IF NOT EXISTS `chatroom` (
  `chatroom_id` int NOT NULL AUTO_INCREMENT,
  `utente_mit` int NOT NULL,
  `utente_dest` int NOT NULL,
  `marked_unread` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`chatroom_id`),
  KEY `chatroom_ibfk_1` (`utente_mit`),
  KEY `chatroom_ibfk_2` (`utente_dest`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `citta`
--

CREATE TABLE IF NOT EXISTS `citta` (
  `citta_id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `provincia` int NOT NULL,
  PRIMARY KEY (`citta_id`),
  UNIQUE KEY `nome` (`nome`),
  KEY `citta_ibfk_1` (`provincia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struttura stand-in per le viste `citta_senza_cap`
-- (Vedi sotto per la vista effettiva)
--
CREATE TABLE IF NOT EXISTS `citta_senza_cap` (
`citta_id` int
,`nome` varchar(255)
,`provincia` int
);

-- --------------------------------------------------------

--
-- Struttura stand-in per le viste `citta_simple`
-- (Vedi sotto per la vista effettiva)
--
CREATE TABLE IF NOT EXISTS `citta_simple` (
`citta_id` int
,`nome` varchar(255)
);

-- --------------------------------------------------------

--
-- Struttura della tabella `istituti`
--

CREATE TABLE IF NOT EXISTS `istituti` (
  `istituto_id` int NOT NULL AUTO_INCREMENT,
  `codice_identificativo` varchar(10) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `citta` int NOT NULL,
  `path_logo` longtext NOT NULL,
  `lat` decimal(11,7) NOT NULL,
  `lng` decimal(11,7) NOT NULL,
  PRIMARY KEY (`istituto_id`),
  UNIQUE KEY `codice_identificativo` (`codice_identificativo`),
  KEY `citta` (`citta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `libri`
--

CREATE TABLE IF NOT EXISTS `libri` (
  `libro_id` int NOT NULL AUTO_INCREMENT,
  `isbn` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nome` varchar(255) NOT NULL,
  `descrizione` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `copertina` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `autori` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `casaed` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `pagine` int DEFAULT NULL,
  `categoria` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `prezzolistino` decimal(6,2) DEFAULT NULL,
  `anno_pubblicazione` year DEFAULT NULL,
  PRIMARY KEY (`isbn`),
  UNIQUE KEY `libro_id` (`libro_id`)
) ;

-- --------------------------------------------------------

--
-- Struttura della tabella `livelli`
--

CREATE TABLE IF NOT EXISTS `livelli` (
  `livello_id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `soglia` int NOT NULL,
  PRIMARY KEY (`livello_id`),
  UNIQUE KEY `soglia` (`soglia`)
) ;

-- --------------------------------------------------------

--
-- Struttura della tabella `messaggi`
--

CREATE TABLE IF NOT EXISTS `messaggi` (
  `messaggio_id` int NOT NULL AUTO_INCREMENT,
  `chatroom` int NOT NULL,
  `messaggio` text NOT NULL,
  `status` tinyint(1) NOT NULL,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`messaggio_id`),
  KEY `messaggi_ibfk_1` (`chatroom`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `notifiche`
--

CREATE TABLE IF NOT EXISTS `notifiche` (
  `notifica_id` int NOT NULL AUTO_INCREMENT,
  `type` int NOT NULL,
  `title` text NOT NULL,
  `content` text NOT NULL,
  `timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `letto` tinyint(1) NOT NULL,
  `cancellato` tinyint(1) NOT NULL DEFAULT '0',
  `utente` int NOT NULL,
  PRIMARY KEY (`notifica_id`),
  KEY `notifiche_ibfk_1` (`utente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `provincia`
--

CREATE TABLE IF NOT EXISTS `provincia` (
  `provincia_id` int NOT NULL AUTO_INCREMENT,
  `nome` char(2) NOT NULL,
  `regione` int NOT NULL,
  PRIMARY KEY (`provincia_id`),
  UNIQUE KEY `nome` (`nome`),
  KEY `provincia_ibfk_1` (`regione`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `recensioni`
--

CREATE TABLE IF NOT EXISTS `recensioni` (
  `recensione_id` int NOT NULL AUTO_INCREMENT,
  `utente_recensito` int NOT NULL,
  `utente_recensore` int NOT NULL,
  `annuncio` int NOT NULL,
  `voto` int NOT NULL,
  `commento` text,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`recensione_id`),
  KEY `recensioni_ibfk_1` (`utente_recensito`),
  KEY `recensioni_ibfk_2` (`utente_recensore`),
  KEY `recensioni_ibfk_3` (`annuncio`)
) ;

-- --------------------------------------------------------

--
-- Struttura della tabella `regione`
--

CREATE TABLE IF NOT EXISTS `regione` (
  `regione_id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(70) NOT NULL,
  PRIMARY KEY (`regione_id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `SPRING_SESSION`
--

CREATE TABLE IF NOT EXISTS `SPRING_SESSION` (
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

-- --------------------------------------------------------

--
-- Struttura della tabella `SPRING_SESSION_ATTRIBUTES`
--

CREATE TABLE IF NOT EXISTS `SPRING_SESSION_ATTRIBUTES` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- Struttura della tabella `status_libri`
--

CREATE TABLE IF NOT EXISTS `status_libri` (
  `status_id` int NOT NULL AUTO_INCREMENT,
  `sott_canc` int NOT NULL,
  `sott_noncanc` int NOT NULL,
  `scrit_canc` int NOT NULL,
  `scrit_noncanc` int NOT NULL,
  `pag_manc` int NOT NULL,
  `pag_rov` int NOT NULL,
  `pag_rov_mol` int NOT NULL,
  `cop_rov` int NOT NULL,
  `ins_manc` int NOT NULL,
  PRIMARY KEY (`status_id`)
) ;

-- --------------------------------------------------------

--
-- Struttura della tabella `tags`
--

CREATE TABLE IF NOT EXISTS `tags` (
  `tag_id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `descrizione` varchar(255) NOT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `tags_utenti`
--

CREATE TABLE IF NOT EXISTS `tags_utenti` (
  `id_tagsutenti` int NOT NULL AUTO_INCREMENT,
  `utente` int NOT NULL,
  `tag` int NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_tagsutenti`),
  KEY `tags_utenti_ibfk_1` (`utente`),
  KEY `tags_utenti_ibfk_2` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `utenti`
--

CREATE TABLE IF NOT EXISTS `utenti` (
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
  `online` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`utente_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `telefono` (`telefono`),
  KEY `utenti_ibfk_1` (`istituto`),
  KEY `utenti_ibfk_2` (`citta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Trigger `utenti`
--
DELIMITER $$
CREATE TRIGGER `def_val_foto` BEFORE INSERT ON `utenti` FOR EACH ROW set new.foto = '/assets/imgs/utenti/default.webp'
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `preferences_def_val` BEFORE INSERT ON `utenti` FOR EACH ROW set new.preferences = '{"avatar" : false, "avatarJson" : {}, "chat": {
    "invio":"enter",
    "sfondo":"#ffffff",
    "coloreRicevuto":"#db5461",
    "coloreInviato":"#008cff"
}}'
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struttura stand-in per le viste `utenti_avg_rating`
-- (Vedi sotto per la vista effettiva)
--
CREATE TABLE IF NOT EXISTS `utenti_avg_rating` (
`utente_id` int
,`avg_voto` decimal(14,4)
,`nvoti` bigint
);

-- --------------------------------------------------------

--
-- Struttura stand-in per le viste `utenti_preferences`
-- (Vedi sotto per la vista effettiva)
--
CREATE TABLE IF NOT EXISTS `utenti_preferences` (
`utente_id` int
,`preferences` longtext
);

-- --------------------------------------------------------

--
-- Struttura stand-in per le viste `utenti_public_information`
-- (Vedi sotto per la vista effettiva)
--
CREATE TABLE IF NOT EXISTS `utenti_public_information` (
`utente_id` int
,`username` varchar(30)
,`bio` longtext
,`nome` varchar(30)
,`cognome` varchar(30)
,`istituto` int
,`moreinfo` varchar(255)
,`punti` int
,`foto` char(255)
,`genere` varchar(30)
,`cap` char(5)
,`citta` int
,`created_at` datetime
,`libri_venduti` bigint
,`online` tinyint(1)
);

-- --------------------------------------------------------

--
-- Struttura per vista `annunci_senza_recensione`
--
DROP TABLE IF EXISTS `annunci_senza_recensione`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `annunci_senza_recensione`  AS SELECT `a`.`annuncio_id` AS `annuncio_id`, `a`.`isbn` AS `isbn`, `a`.`prezzo` AS `prezzo`, `a`.`utente` AS `utente`, `a`.`created_at` AS `created_at`, `a`.`stato` AS `stato`, `a`.`livello_usura` AS `livello_usura`, `a`.`status_libro` AS `status_libro`, `a`.`sold_to` AS `sold_to`, `r`.`recensione_id` AS `recensione_id`, `r`.`utente_recensito` AS `utente_recensito`, `r`.`utente_recensore` AS `utente_recensore`, `r`.`annuncio` AS `annuncio`, `r`.`voto` AS `voto`, `r`.`commento` AS `commento` FROM (`annunci_libri` `a` left join `recensioni` `r` on((`a`.`annuncio_id` = `r`.`annuncio`))) WHERE ((`a`.`sold_to` is not null) AND (`a`.`stato` = 2) AND (`r`.`recensione_id` is null))  ;

-- --------------------------------------------------------

--
-- Struttura per vista `citta_senza_cap`
--
DROP TABLE IF EXISTS `citta_senza_cap`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `citta_senza_cap`  AS SELECT DISTINCT `c`.`citta_id` AS `citta_id`, `c`.`nome` AS `nome`, `c`.`provincia` AS `provincia` FROM (`citta` `c` left join `cap_citta` `cp` on((`c`.`citta_id` = `cp`.`citta_id`))) WHERE (`cp`.`cap` is null) ORDER BY `c`.`citta_id` ASC  ;

-- --------------------------------------------------------

--
-- Struttura per vista `citta_simple`
--
DROP TABLE IF EXISTS `citta_simple`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `citta_simple`  AS SELECT `citta`.`citta_id` AS `citta_id`, `citta`.`nome` AS `nome` FROM `citta``citta`  ;

-- --------------------------------------------------------

--
-- Struttura per vista `utenti_avg_rating`
--
DROP TABLE IF EXISTS `utenti_avg_rating`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `utenti_avg_rating`  AS SELECT `u`.`utente_id` AS `utente_id`, avg(`r`.`voto`) AS `avg_voto`, count(`r`.`voto`) AS `nvoti` FROM (`utenti` `u` join `recensioni` `r` on((`u`.`utente_id` = `r`.`utente_recensito`))) GROUP BY `r`.`utente_recensito``utente_recensito`  ;

-- --------------------------------------------------------

--
-- Struttura per vista `utenti_preferences`
--
DROP TABLE IF EXISTS `utenti_preferences`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `utenti_preferences`  AS SELECT `utenti`.`utente_id` AS `utente_id`, `utenti`.`preferences` AS `preferences` FROM `utenti``utenti`  ;

-- --------------------------------------------------------

--
-- Struttura per vista `utenti_public_information`
--
DROP TABLE IF EXISTS `utenti_public_information`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `utenti_public_information`  AS SELECT `ut`.`utente_id` AS `utente_id`, `ut`.`username` AS `username`, `ut`.`bio` AS `bio`, `ut`.`nome` AS `nome`, `ut`.`cognome` AS `cognome`, `ut`.`istituto` AS `istituto`, `ut`.`moreinfo` AS `moreinfo`, `ut`.`punti` AS `punti`, `ut`.`foto` AS `foto`, `ut`.`genere` AS `genere`, `ut`.`cap` AS `cap`, `ut`.`citta` AS `citta`, `ut`.`created_at` AS `created_at`, `subq`.`libri_venduti` AS `libri_venduti`, `ut`.`online` AS `online` FROM (`utenti` `ut` left join (select `annunci_libri`.`utente` AS `utente`,count(`annunci_libri`.`annuncio_id`) AS `libri_venduti` from `annunci_libri` where (`annunci_libri`.`stato` = 2) group by `annunci_libri`.`utente`) `subq` on((`ut`.`utente_id` = `subq`.`utente`)))  ;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `annunci_libri`
--
ALTER TABLE `annunci_libri`
  ADD CONSTRAINT `annunci_libri_ibfk_2` FOREIGN KEY (`utente`) REFERENCES `utenti` (`utente_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `annunci_libri_ibfk_3` FOREIGN KEY (`isbn`) REFERENCES `libri` (`isbn`) ON UPDATE CASCADE,
  ADD CONSTRAINT `annunci_libri_ibfk_4` FOREIGN KEY (`status_libro`) REFERENCES `status_libri` (`status_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `annunci_libri_ibfk_5` FOREIGN KEY (`sold_to`) REFERENCES `utenti` (`utente_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Limiti per la tabella `cap_citta`
--
ALTER TABLE `cap_citta`
  ADD CONSTRAINT `citta_id_ibfk_1` FOREIGN KEY (`citta_id`) REFERENCES `citta` (`citta_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `carrello`
--
ALTER TABLE `carrello`
  ADD CONSTRAINT `carrello_ibfk_1` FOREIGN KEY (`utente`) REFERENCES `utenti` (`utente_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `carrello_ibfk_2` FOREIGN KEY (`annuncio`) REFERENCES `annunci_libri` (`annuncio_id`) ON UPDATE CASCADE;

--
-- Limiti per la tabella `chatroom`
--
ALTER TABLE `chatroom`
  ADD CONSTRAINT `chatroom_ibfk_1` FOREIGN KEY (`utente_mit`) REFERENCES `utenti` (`utente_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `chatroom_ibfk_2` FOREIGN KEY (`utente_dest`) REFERENCES `utenti` (`utente_id`) ON UPDATE CASCADE;

--
-- Limiti per la tabella `citta`
--
ALTER TABLE `citta`
  ADD CONSTRAINT `citta_ibfk_1` FOREIGN KEY (`provincia`) REFERENCES `provincia` (`provincia_id`) ON UPDATE CASCADE;

--
-- Limiti per la tabella `istituti`
--
ALTER TABLE `istituti`
  ADD CONSTRAINT `istituti_ibfk_1` FOREIGN KEY (`citta`) REFERENCES `citta` (`citta_id`);

--
-- Limiti per la tabella `messaggi`
--
ALTER TABLE `messaggi`
  ADD CONSTRAINT `messaggi_ibfk_1` FOREIGN KEY (`chatroom`) REFERENCES `chatroom` (`chatroom_id`) ON UPDATE CASCADE;

--
-- Limiti per la tabella `notifiche`
--
ALTER TABLE `notifiche`
  ADD CONSTRAINT `notifiche_ibfk_1` FOREIGN KEY (`utente`) REFERENCES `utenti` (`utente_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Limiti per la tabella `provincia`
--
ALTER TABLE `provincia`
  ADD CONSTRAINT `provincia_ibfk_1` FOREIGN KEY (`regione`) REFERENCES `regione` (`regione_id`) ON UPDATE CASCADE;

--
-- Limiti per la tabella `recensioni`
--
ALTER TABLE `recensioni`
  ADD CONSTRAINT `recensioni_ibfk_1` FOREIGN KEY (`utente_recensito`) REFERENCES `utenti` (`utente_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `recensioni_ibfk_2` FOREIGN KEY (`utente_recensore`) REFERENCES `utenti` (`utente_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `recensioni_ibfk_3` FOREIGN KEY (`annuncio`) REFERENCES `annunci_libri` (`annuncio_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Limiti per la tabella `SPRING_SESSION_ATTRIBUTES`
--
ALTER TABLE `SPRING_SESSION_ATTRIBUTES`
  ADD CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `SPRING_SESSION` (`PRIMARY_ID`) ON DELETE CASCADE;

--
-- Limiti per la tabella `tags_utenti`
--
ALTER TABLE `tags_utenti`
  ADD CONSTRAINT `tags_utenti_ibfk_1` FOREIGN KEY (`utente`) REFERENCES `utenti` (`utente_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `tags_utenti_ibfk_2` FOREIGN KEY (`tag`) REFERENCES `tags` (`tag_id`) ON UPDATE CASCADE;

--
-- Limiti per la tabella `utenti`
--
ALTER TABLE `utenti`
  ADD CONSTRAINT `utenti_ibfk_1` FOREIGN KEY (`istituto`) REFERENCES `istituti` (`istituto_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `utenti_ibfk_2` FOREIGN KEY (`citta`) REFERENCES `citta` (`citta_id`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
