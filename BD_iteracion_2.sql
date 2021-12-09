-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: localhost    Database: prog_av
-- ------------------------------------------------------
-- Server version	8.0.26

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
  PRIMARY KEY (`idClientes`),
  KEY `fk_idPersona_cliente` (`persona`),
  CONSTRAINT `fk_idPersona_cliente` FOREIGN KEY (`persona`) REFERENCES `persona` (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'Leandro Martinez',2),(2,'Nicolas Rodriguez',1);
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
  PRIMARY KEY (`idperfil`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfil`
--

LOCK TABLES `perfil` WRITE;
/*!40000 ALTER TABLE `perfil` DISABLE KEYS */;
INSERT INTO `perfil` VALUES (1,'Perfil A','Este es el perfil x el cual consiste en A'),(2,'Perfil B','Este es el perfil B que consiste en'),(3,'Perfil C','Este es el perfil Y que consiste en'),(4,'Perfil D','Este es el perfil D que consiste en');
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
  PRIMARY KEY (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persona`
--

LOCK TABLES `persona` WRITE;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
INSERT INTO `persona` VALUES (1,'Santiago','Giordano','2000-01-16','2302637110'),(2,'Leonel','Piccioni','2020-09-20','3467600540'),(3,'Joel','Dellamaggiore','2020-03-25','3535639496');
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
  PRIMARY KEY (`idPersonal`),
  KEY `fk_idPersona_personal` (`persona`),
  CONSTRAINT `fk_idPersona_personal` FOREIGN KEY (`persona`) REFERENCES `persona` (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal`
--

LOCK TABLES `personal` WRITE;
/*!40000 ALTER TABLE `personal` DISABLE KEYS */;
INSERT INTO `personal` VALUES (10,20425110137,3),(12,20423378094,1),(13,20414141414,2);
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
  PRIMARY KEY (`idpersonal_perfil`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal_perfil`
--

LOCK TABLES `personal_perfil` WRITE;
/*!40000 ALTER TABLE `personal_perfil` DISABLE KEYS */;
INSERT INTO `personal_perfil` VALUES (1,10,1),(2,10,2),(3,10,3),(4,12,3);
/*!40000 ALTER TABLE `personal_perfil` ENABLE KEYS */;
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
  PRIMARY KEY (`idproyecto`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto`
--

LOCK TABLES `proyecto` WRITE;
/*!40000 ALTER TABLE `proyecto` DISABLE KEYS */;
INSERT INTO `proyecto` VALUES (1,'Proyecto n° 1','2021-09-22','2021-09-22','2021-09-22',0,0,'Proyecto terminado exitosamente!',0.00),(4,'Proyecto n° 2','2021-09-22','2021-09-21','2021-09-23',1,1,'Proyecto terminado exitosamente',0.00),(5,'Proyecto n° 3','2021-09-22','2021-09-21','2021-09-23',1,1,'Proyecto terminado exitosamente',0.00),(7,'Proyecto n° 4','2021-09-23','2021-09-22','2021-09-24',1,0,'nProyecto terminado exitosamente',0.00),(8,'Proyecto n° 5','2021-09-22','2021-09-21','2021-09-23',1,1,'Proyecto terminado exitosamente',0.00),(9,'Proyecto n° 6','2021-09-29','2021-09-28','2021-09-30',6,1,'Proyecto en curso',0.00),(11,'Proyecto n° 7','2021-09-29','2021-09-28','2021-09-30',6,2,'Proyecto en curso',0.00),(12,'Proyecto n° 8','2021-09-29','2021-09-28','2021-09-30',2,1,'Proyecto en curso!',0.00);
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
  PRIMARY KEY (`idproyecto_perfil`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto_perfil`
--

LOCK TABLES `proyecto_perfil` WRITE;
/*!40000 ALTER TABLE `proyecto_perfil` DISABLE KEYS */;
INSERT INTO `proyecto_perfil` VALUES (1,5,1),(2,5,2),(3,5,3),(4,9,1),(5,7,2);
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
  PRIMARY KEY (`idTipoProyecto`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_proyecto`
--

LOCK TABLES `tipo_proyecto` WRITE;
/*!40000 ALTER TABLE `tipo_proyecto` DISABLE KEYS */;
INSERT INTO `tipo_proyecto` VALUES (1,'ciencia','Tipo de proyecto asociado a las ciencias sociales y naturales '),(6,'matematica','Tipo de proyecto asociado a las matematicas'),(9,'Mecanicaa','Tipo de proceso asociado a la mecanica automotriz ');
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
INSERT INTO `ubicaciondelproyecto` VALUES (1,'Nacional','Proyecto realizado en Argentina',1),(2,'Exterior','Proyecto realizado fuera de Argentina',2);
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

-- Dump completed on 2021-12-09 14:21:47
