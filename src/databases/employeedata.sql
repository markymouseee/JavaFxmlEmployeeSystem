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
-- Table structure for table `employeedata`
--

CREATE TABLE `employeedata` (
  `id` int(11) NOT NULL,
  `employeeid` int(200) NOT NULL,
  `firstname` varchar(200) NOT NULL,
  `lastname` varchar(200) NOT NULL,
  `sex` varchar(200) NOT NULL,
  `email` varchar(200) NOT NULL,
  `position` varchar(200) NOT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employeedata`
--

INSERT INTO `employeedata` (`id`, `employeeid`, `firstname`, `lastname`, `sex`, `email`, `position`, `date`) VALUES
(1, 1, 'Mark Noriel', 'Sanama', 'Male', 'sanamamarknoriel@gmail.com', 'Cyber Security Engineer', '2024-06-05'),
(2, 2, 'Mark', 'Zuckerberg', 'Male', 'mark@facebook.com', 'Cyber Security Analyst', '2024-06-08'),
(3, 3, 'Meljune', 'Habulan', 'Male', 'meljunehabulan@gmail.com', 'Cloud Security Analyst', '2024-06-06'),
(4, 4, 'Rendyl John', 'Pregua', 'Male', 'rendyljohn@yahoo.com', 'Full Stack Web Developer', '2024-06-06'),
(5, 5, 'Brilliant', 'Agunod', 'Male', 'brilliantagunod@gmail.com', 'Network Engineer', '2024-06-06'),
(6, 6, 'Ky', 'Ky', 'Female', 'ky@yahoo.com', 'Data Analyst', '2024-06-08'),
(7, 7, 'Hello', 'Kitty', 'Female', 'hellokitty@hellokitty.com', 'Janitor', '2024-06-10');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `employeedata`
--
ALTER TABLE `employeedata`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `employeedata`
--
ALTER TABLE `employeedata`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
