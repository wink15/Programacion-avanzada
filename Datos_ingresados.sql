-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: localhost    Database: prog_av
-- ------------------------------------------------------
-- Server version	8.0.25

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
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `idClientes` int NOT NULL AUTO_INCREMENT,
  `razonSocial` varchar(45) DEFAULT NULL,
  `persona` int DEFAULT NULL,
  `borrado` int DEFAULT '0',
  PRIMARY KEY (`idClientes`),
  KEY `fk_idPersona_cliente` (`persona`),
  CONSTRAINT `fk_idPersona_cliente` FOREIGN KEY (`persona`) REFERENCES `persona` (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'Corp SA',1,0),(2,'La Firma SRL',1,0),(5,'ACME SRL',22,0),(6,'Industrias Stark',23,0),(7,'WINK IT',24,0),(8,'Americo Flia',25,0);
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `moneda`
--

DROP TABLE IF EXISTS `moneda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `moneda` (
  `idmoneda` int NOT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idmoneda`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `moneda`
--

LOCK TABLES `moneda` WRITE;
/*!40000 ALTER TABLE `moneda` DISABLE KEYS */;
INSERT INTO `moneda` VALUES (1,'Pesos'),(2,'Dolares');
/*!40000 ALTER TABLE `moneda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nocliente`
--

DROP TABLE IF EXISTS `nocliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nocliente` (
  `idNoCliente` int NOT NULL AUTO_INCREMENT,
  `observacion` varchar(45) DEFAULT NULL,
  `persona` int DEFAULT NULL,
  PRIMARY KEY (`idNoCliente`),
  KEY `fk_idPersona` (`persona`),
  CONSTRAINT `fk_idPersona` FOREIGN KEY (`persona`) REFERENCES `persona` (`id_persona`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nocliente`
--

LOCK TABLES `nocliente` WRITE;
/*!40000 ALTER TABLE `nocliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `nocliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perfil`
--

DROP TABLE IF EXISTS `perfil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `perfil` (
  `idperfil` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `descripcion` varchar(45) DEFAULT NULL,
  `borrado` int DEFAULT '0',
  PRIMARY KEY (`idperfil`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfil`
--

LOCK TABLES `perfil` WRITE;
/*!40000 ALTER TABLE `perfil` DISABLE KEYS */;
INSERT INTO `perfil` VALUES (1,'Perfil A','Este es el perfil x el cual consiste en A',0),(2,'Perfil B','Este es el perfil B que consiste en',0),(3,'Perfil C','Este es el perfil Y que consiste en',0),(4,'Perfil D','Este es el perfil D',0),(6,'PErfil 2','dasdsd',1),(7,'Perfil E','Descripcion perfil E',0),(8,'Perfil F','Descripcion perfil F',0),(9,'Perfil G','Descripcion perfil G',0),(10,'Perfil H','Descripcion perfil H',0),(11,'Perfil I','Descripcion perfil I',0),(12,'Perfil k','Descripcion perfil K',0),(13,'Perfil L','Descripcion perfil L',0),(14,'Perfil M ','Descripcion perfil M',0);
/*!40000 ALTER TABLE `perfil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persona`
--

DROP TABLE IF EXISTS `persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persona` (
  `id_persona` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `borrado` int DEFAULT '0',
  PRIMARY KEY (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persona`
--

LOCK TABLES `persona` WRITE;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
INSERT INTO `persona` VALUES (1,'Santiago','Giordano','2000-01-16','2302637110',0),(2,'Leonel','Piccioni','2001-09-20','3467600540',0),(3,'Joel','Dellamaggiore','1999-03-25','3535639496',0),(6,'asdasd','asdasdas','2001-12-21','3535639496',1),(7,'Alma','González','1990-01-14','2302711563',0),(8,'Juana','Rodríguez','1998-07-30','2346567429',0),(9,'Julieta','Gómez','2000-05-27','2346495362',0),(10,'Morena','Fernández','1993-09-08','2346423295',0),(11,'Josefina','López','1997-02-28','2346351228',0),(12,'Benjamín','Martínez','2002-09-18','2346279161',1),(13,'Bautista','Diaz','2000-05-31','2346207094',0),(14,'Felipe','Pérez','1991-05-31','2302207095',1),(15,'Valentino','Pérez','1996-01-15','2302567094',0),(16,'Benicio','Sánchez','1989-12-02','2302927093',0),(17,'Joaquín','Sánchez','1993-10-22','2344287092',0),(18,'Lorenzo','Sosa','1995-10-23','2344647091',0),(19,'Santino','Benítez','2003-09-04','2964287020',0),(20,'Juan Ignacio','Ruiz','1995-04-10','2964647097',1),(21,'Mateo','Flores','2001-09-27','2964007174',1),(22,'Álvaro','Véliz','1978-01-07','2963893995',0),(23,'Amadeo','Silva','1970-08-29','2963754072',0),(24,'Amador','Delfino','1985-04-19','2963614149',0),(25,'Americo','Giordano','1966-12-09','2963474226',0);
/*!40000 ALTER TABLE `persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personal`
--

DROP TABLE IF EXISTS `personal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personal` (
  `idPersonal` int NOT NULL AUTO_INCREMENT,
  `cuit` bigint DEFAULT NULL,
  `persona` int DEFAULT NULL,
  `borrado` int DEFAULT '0',
  PRIMARY KEY (`idPersonal`),
  KEY `fk_idPersona_personal` (`persona`),
  CONSTRAINT `fk_idPersona_personal` FOREIGN KEY (`persona`) REFERENCES `persona` (`id_persona`) ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal`
--

LOCK TABLES `personal` WRITE;
/*!40000 ALTER TABLE `personal` DISABLE KEYS */;
INSERT INTO `personal` VALUES (10,20425110137,2,0),(12,20423378094,1,0),(17,20423378094,1,1),(18,20423377084,3,0),(19,20937779160,6,1),(20,20934449161,7,0),(21,20931119164,8,0),(22,20927789165,9,0),(23,20924459169,10,0),(24,20921129162,11,0),(25,20914469160,12,0),(26,20911139160,13,0),(27,20907809160,14,1),(28,20904479160,15,0),(29,20901149160,16,0),(30,20897819160,17,0),(31,20894489160,18,0),(32,20891159160,19,0),(33,20887829160,20,1),(34,20884499160,21,1);
/*!40000 ALTER TABLE `personal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personal_perfil`
--

DROP TABLE IF EXISTS `personal_perfil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personal_perfil` (
  `idpersonal_perfil` int NOT NULL AUTO_INCREMENT,
  `personal` int DEFAULT NULL,
  `perfil` int DEFAULT NULL,
  `borrado` int DEFAULT '0',
  PRIMARY KEY (`idpersonal_perfil`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal_perfil`
--

LOCK TABLES `personal_perfil` WRITE;
/*!40000 ALTER TABLE `personal_perfil` DISABLE KEYS */;
INSERT INTO `personal_perfil` VALUES (42,18,1,0),(43,18,2,0),(44,18,3,0),(45,20,1,0),(46,20,2,0),(47,20,3,0),(48,20,4,0),(49,20,1,0),(50,20,2,0),(51,20,3,0),(52,20,4,0);
/*!40000 ALTER TABLE `personal_perfil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personal_proyecto`
--

DROP TABLE IF EXISTS `personal_proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personal_proyecto` (
  `idpersonal_proyecto` int NOT NULL AUTO_INCREMENT,
  `proyecto` int NOT NULL,
  `personal` int NOT NULL,
  `borrado` int DEFAULT '0',
  PRIMARY KEY (`idpersonal_proyecto`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal_proyecto`
--

LOCK TABLES `personal_proyecto` WRITE;
/*!40000 ALTER TABLE `personal_proyecto` DISABLE KEYS */;
INSERT INTO `personal_proyecto` VALUES (3,5,10,1),(4,17,20,0),(5,17,18,0),(6,1,20,0),(7,5,21,0),(8,8,10,0),(9,5,10,0),(10,7,18,0),(11,7,20,0),(12,7,10,0),(13,1,18,0);
/*!40000 ALTER TABLE `personal_proyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyecto`
--

DROP TABLE IF EXISTS `proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proyecto` (
  `idproyecto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `fechaInicio` date NOT NULL,
  `fechaConfirmacion` date NOT NULL,
  `fechaFin` date NOT NULL,
  `tipoProyecto` int NOT NULL,
  `cliente` int NOT NULL,
  `observacion` varchar(200) NOT NULL,
  `monto` double(10,2) NOT NULL,
  `ubicacion` int DEFAULT NULL,
  `borrado` int DEFAULT '0',
  PRIMARY KEY (`idproyecto`),
  KEY `fk_ubicacion` (`ubicacion`),
  CONSTRAINT `fk_ubicacion` FOREIGN KEY (`ubicacion`) REFERENCES `ubicaciondelproyecto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto`
--

LOCK TABLES `proyecto` WRITE;
/*!40000 ALTER TABLE `proyecto` DISABLE KEYS */;
INSERT INTO `proyecto` VALUES (1,'Proyecto n° 1','2021-09-22','2021-09-22','2021-09-22',0,0,'Proyecto terminado exitosamente!',0.00,1,0),(4,'Proyecto n° 2','2021-09-22','2021-09-21','2021-09-23',1,1,'Proyecto terminado exitosamente',0.00,2,1),(5,'Proyecto n° 3','2021-09-22','2021-09-21','2021-09-23',1,1,'Proyecto terminado exitosamente',0.00,1,0),(7,'Proyecto n° 4','2021-09-23','2021-09-22','2021-09-24',1,0,'nProyecto terminado exitosamente',0.00,2,0),(8,'Proyecto n° 5','2021-09-22','2021-09-21','2021-09-23',1,1,'Proyecto terminado exitosamente',0.00,1,0),(9,'Proyecto n° 6','2021-09-29','2021-09-28','2021-09-30',6,1,'Proyecto en curso',0.00,1,0),(11,'Proyecto n° 7','2021-09-29','2021-09-28','2021-09-30',6,2,'Proyecto en curso',0.00,NULL,0),(12,'Proyecto n° 8','2021-09-29','2021-09-28','2021-09-30',2,1,'Proyecto en curso!',0.00,NULL,0),(13,'Intento','2021-12-11','2021-12-10','2021-12-18',1,1,'aa',200.00,1,0),(15,'aaaaaa','2021-12-22','2021-12-22','2021-12-30',1,1,'a',21.00,1,1),(16,'AASA','2021-12-22','2021-12-22','2021-12-22',1,1,'a',123.00,1,1),(17,'Proyecto n°9','2021-12-24','2021-12-25','2022-01-31',13,5,'Observaciones para proyecto n°9',5000.00,2,0),(18,'Proyecto n°10','2021-12-25','2021-12-28','2022-02-11',14,6,'Observaciones para el proyecto 10',200000.00,1,0),(19,'Proyecto n°11','2021-12-30','2022-01-11','2022-02-18',15,7,'Observaciones del proyecto 11',300000.00,1,0),(20,'Proyecto n°12','2021-12-27','2021-12-28','2022-07-01',15,8,'Observaciones para proyecto 12',10000.00,2,0);
/*!40000 ALTER TABLE `proyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyecto_perfil`
--

DROP TABLE IF EXISTS `proyecto_perfil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proyecto_perfil` (
  `idproyecto_perfil` int NOT NULL AUTO_INCREMENT,
  `proyecto` int DEFAULT NULL,
  `perfil` int DEFAULT NULL,
  `borrado` int DEFAULT '0',
  PRIMARY KEY (`idproyecto_perfil`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto_perfil`
--

LOCK TABLES `proyecto_perfil` WRITE;
/*!40000 ALTER TABLE `proyecto_perfil` DISABLE KEYS */;
INSERT INTO `proyecto_perfil` VALUES (1,5,1,0),(2,5,2,0),(3,5,3,0),(4,9,1,0),(5,7,2,0),(6,14,1,0),(7,14,3,0),(8,15,1,0),(9,15,2,0),(10,15,3,0),(11,5,4,1),(12,17,1,0),(13,17,7,0),(14,17,14,0),(15,18,7,0),(16,18,9,0),(17,19,1,0),(18,19,9,0),(19,19,8,0),(20,19,2,0),(21,19,3,0),(22,1,1,0),(23,1,2,0),(24,1,3,0),(25,1,4,0);
/*!40000 ALTER TABLE `proyecto_perfil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_proyecto`
--

DROP TABLE IF EXISTS `tipo_proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_proyecto` (
  `idTipoProyecto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `borrado` int DEFAULT '0',
  PRIMARY KEY (`idTipoProyecto`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_proyecto`
--

LOCK TABLES `tipo_proyecto` WRITE;
/*!40000 ALTER TABLE `tipo_proyecto` DISABLE KEYS */;
INSERT INTO `tipo_proyecto` VALUES (1,'ciencia','Tipo de proyecto asociado a las ciencias sociales y naturales ',0),(6,'matematica','Tipo de proyecto asociado a las matematicas',0),(9,'Mecanicaa','Tipo de proceso asociado a la mecanica automotriz ',1),(13,'Tipo proyecto A','Decripcion Tipo proyecto A',0),(14,'Tipo proyecto B','Descripcion Tipo proyecto B',0),(15,'Tipo proyecto C','Descripcion Tipo proyecto C',0),(16,'Tipo proyecto D','Descripcion Tipo proyecto D',0);
/*!40000 ALTER TABLE `tipo_proyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ubicaciondelproyecto`
--

DROP TABLE IF EXISTS `ubicaciondelproyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ubicaciondelproyecto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `descripcion` varchar(45) NOT NULL,
  `moneda` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ubicaciondelproyecto`
--

LOCK TABLES `ubicaciondelproyecto` WRITE;
/*!40000 ALTER TABLE `ubicaciondelproyecto` DISABLE KEYS */;
INSERT INTO `ubicaciondelproyecto` VALUES (1,'Nacional','Proyecto realizado en Argentina',1),(2,'Internacional','Proyecto realizado fuera de Argentina',2);
/*!40000 ALTER TABLE `ubicaciondelproyecto` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-24  4:14:55
