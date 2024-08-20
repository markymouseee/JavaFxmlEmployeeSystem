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
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(200) NOT NULL,
  `uid` int(200) NOT NULL,
  `name` varchar(200) NOT NULL,
  `username` varchar(200) NOT NULL,
  `email` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  `role` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `uid`, `name`, `username`, `email`, `password`, `role`) VALUES
(1, 1, 'Mark Noriel Sanama', 'admin', 'sanamamarknoriel@gmail.com', '$2a$12$wmGthZZdS78trdzNGETjC.Al/D1FnO./sYX94mXmH4HdctLgiH6fO', 'Super Admin'),
(2, 2, 'Rey Jhon Pedroso', 'reyjhon', 'reyjohn@admin.com', '$2a$12$1RDuYVpV3UOsbFMjHQmhoOcdU7H/w0cX2IkUX7iMN5DCD7zPHJNvW', 'Admin'),
(3, 3, 'Brilliant Agunod', 'brilliant', 'brilliant@admin.com', '$2a$12$1pIS.XvnveqPmKhTnXNKiu3gOdSdVSN9xNrkvn0Pbr0I5Li4mjcOS', 'Admin'),
(4, 4, 'Vincent Carillo', 'vincent', 'vincent@user.com', '$2a$12$GpocbLc/CGErfa1sR/FXr.FL.MLdgq.6i8EUdxQPLUckedygcAPpi', 'Admin'),
(5, 5, 'Rendyl John Sanama', 'rendyl123', 'rendyljohn@gmail.com', '$2a$12$2USKfK5voyaNHzeaO.AZBOouH/OrJ3OCUXLpIeUTMcous3zDYf1FW', 'User'),
(6, 6, 'Mark Noriel Sanama', 'mark', 'markyy@gmail.com', '$2a$12$8FSQrm2U/aBe12FMc0Gqd.AseuENlun65TjI4i0qAvT2Djetpuc7.', 'User'),
(7, 7, 'Mark Noriel Sanama', 'markymouseee3', 'markymousee@admin.com', '$2a$12$uS3k7gt6lPbPQ7Z1z5uLS..opL76IKZbx6IpSZGhmU4K5AFMW6PEy', 'Super Admin'),
(8, 8, 'Mark Noriel S. Pregua', 'syndxcate', 'syndicate@yahoo.com', '$2a$12$HQiuK4Gh8Hvgurm7Vv5hueBAyR23hdEjz8K1roEe/Zn2Q0lqRQWmi', 'User'),
(9, 9, 'Mark Noriel Sanama', 'mark123', 'mark@outllook.com', '$2a$12$JJ27ZJF7dZqIURpGY/P4geY8svM/dYZP109hZ5u9apWutWTal3hra', 'Admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
