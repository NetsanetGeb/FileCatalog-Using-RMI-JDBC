-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Dec 03, 2017 at 07:29 PM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `filecatalog`
--

-- --------------------------------------------------------

--
-- Table structure for table `file`
--

CREATE TABLE IF NOT EXISTS `file` (
  `name` varchar(100) CHARACTER SET utf8 NOT NULL,
  `size` bigint(20) unsigned NOT NULL,
  `owner` varchar(50) CHARACTER SET utf8 NOT NULL,
  `access` enum('public','private') CHARACTER SET utf8 NOT NULL,
  `action` enum('read','write') CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`name`),
  KEY `owner` (`owner`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `file`
--

INSERT INTO `file` (`name`, `size`, `owner`, `access`, `action`) VALUES
('id1212-hw2.pdf', 205880, 'samri', 'public', 'write'),
('id1212-hw3.pdf', 216875, 'elsa', 'private', NULL),
('loggedView.PNG', 14435, 'netsi', 'public', 'read'),
('regisView.PNG', 10997, 'netsi', 'public', 'write'),
('updateSuc.PNG', 6598, 'elsa', 'public', 'read');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `username` varchar(50) CHARACTER SET utf8 NOT NULL,
  `password` varchar(100) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `password`) VALUES
('dad', 'df3939f11965e7e75dbc046cd9af1c67'),
('Ebuy', '71f1b3454548bd4a305eea0d062d0a67'),
('elsa', '783833680e6da5cf6cd7481a44d8fa4c'),
('kebede', 'a0c45c5330cc1b5f0dedd6cf6a159597'),
('netsi', '64bc5efee296963ab561bf64f804ec0a'),
('Sami', '4f8de24d6093ac5d25c7cfafc474d49f'),
('Samri', 'd607d93fffd9ceb85201d06c15bb56dc'),
('selam', '9beec4cfc112a47472b408858c6a719a');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `file`
--
ALTER TABLE `file`
  ADD CONSTRAINT `owner` FOREIGN KEY (`owner`) REFERENCES `user` (`username`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
