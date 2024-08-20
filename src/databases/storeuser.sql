-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 14, 2024 at 09:06 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `employeesystem`
--

-- --------------------------------------------------------

--
-- Table structure for table `storeuser`
--

CREATE TABLE `storeuser` (
  `id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `username` varchar(200) NOT NULL,
  `email` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `storeuser`
--

INSERT INTO `storeuser` (`id`, `name`, `username`, `email`, `password`) VALUES
(1, 'Mark', 'markymarky', 'mark@hotmail.com', '$2a$12$h25.mkvnLvz/y8saa/6wcOo/SrSF.nV0JQFXrnAWqqB01kv6ji/jm'),
(2, 'Mark Noriel', 'markymark', 'mark@gmail.com', '$2a$12$KLdccNHUrra04iWm8K9V/uZkeF/UqnDICD6ZEEe.oW./dmIfviUyS');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `storeuser`
--
ALTER TABLE `storeuser`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `storeuser`
--
ALTER TABLE `storeuser`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
