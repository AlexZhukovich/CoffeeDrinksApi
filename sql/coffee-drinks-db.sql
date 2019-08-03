-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Aug 03, 2019 at 12:37 PM
-- Server version: 5.7.24-log
-- PHP Version: 7.2.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `coffee-drinks-db`
--

-- --------------------------------------------------------

--
-- Table structure for table `coffee_drinks`
--

CREATE TABLE `coffee_drinks` (
  `id` bigint(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `photourl` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `ingredients` varchar(256) NOT NULL COMMENT 'Coffee ingredients'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `coffee_drinks`
--

INSERT INTO `coffee_drinks` (`id`, `title`, `photourl`, `description`, `ingredients`) VALUES
(1, 'Espresso 1', '', 'Espresso desc 1', ''),
(2, 'Espresso 2', '', 'Espresso desc 2', ''),
(3, 'Espresso 3', '', 'Espresso desc 3', ''),
(4, 'Espresso 4', '', 'Espresso desc 4', ''),
(5, 'Espresso 5', '', 'Espresso desc 5', ''),
(6, 'Espresso 6', '', 'Espresso desc 6', ''),
(7, 'Espresso 7', '', 'Espresso desc 7', ''),
(8, 'Espresso 8', '', 'Espresso desc 8', ''),
(9, 'Espresso 9', '', 'Espresso desc 9', ''),
(10, 'Espresso 10', '', 'Espresso desc 10', ''),
(11, 'Espresso 11', '', 'Espresso desc 11', ''),
(12, 'Espresso 12', '', 'Espresso desc 12', ''),
(13, 'Espresso 13', '', 'Espresso desc 13', ''),
(14, 'Espresso 14', '', 'Espresso desc 14', ''),
(15, 'Espresso 15', '', 'Espresso desc 15', ''),
(16, 'Espresso 16', '', 'Espresso desc 16', ''),
(17, 'Espresso 17', '', 'Espresso desc 17', ''),
(18, 'Espresso 18', '', 'Espresso desc 18', ''),
(19, 'Espresso 19', '', 'Espresso desc 19', ''),
(20, 'Espresso 20', '', 'Espresso desc 20', ''),
(21, 'Espresso 21', '', 'Espresso desc 21', ''),
(22, 'Espresso 22', '', 'Espresso desc 22', ''),
(23, 'Espresso 23', '', 'Espresso desc 23', ''),
(24, 'Espresso 24', '', 'Espresso desc 24', ''),
(25, 'Espresso 25', '', 'Espresso desc 25', '');

-- --------------------------------------------------------

--
-- Table structure for table `favourite_coffee_drink`
--

CREATE TABLE `favourite_coffee_drink` (
  `id` bigint(11) NOT NULL,
  `userid` bigint(11) NOT NULL,
  `coffeeid` bigint(11) NOT NULL,
  `favourite` enum('Y','N') NOT NULL DEFAULT 'N'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `sessions`
--

CREATE TABLE `sessions` (
  `id` bigint(20) NOT NULL COMMENT 'Session ID',
  `userid` bigint(20) NOT NULL COMMENT 'User ID',
  `accesstoken` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Access Token',
  `accesstokenexpiry` datetime NOT NULL COMMENT 'Access Token Expiry Date/Time',
  `refreshtoken` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'RefreshToken',
  `refreshtokenexpiry` datetime NOT NULL COMMENT 'Refresh Token Expiry Date/Time'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Sessions table';

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL COMMENT 'User ID',
  `fullname` varchar(255) NOT NULL COMMENT 'Users full name',
  `email` varchar(255) NOT NULL COMMENT 'User''s email',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'User''s password',
  `useractive` enum('Y','N') NOT NULL DEFAULT 'Y' COMMENT 'Is User active',
  `loginattempts` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `coffee_drinks`
--
ALTER TABLE `coffee_drinks`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `favourite_coffee_drink`
--
ALTER TABLE `favourite_coffee_drink`
  ADD PRIMARY KEY (`id`),
  ADD KEY `coffeeid` (`coffeeid`),
  ADD KEY `userid` (`userid`);

--
-- Indexes for table `sessions`
--
ALTER TABLE `sessions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `acceesstoken` (`accesstoken`),
  ADD UNIQUE KEY `refreshtoken` (`refreshtoken`),
  ADD KEY `sessionuserid_fk` (`userid`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `coffee_drinks`
--
ALTER TABLE `coffee_drinks`
  MODIFY `id` bigint(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `favourite_coffee_drink`
--
ALTER TABLE `favourite_coffee_drink`
  MODIFY `id` bigint(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

--
-- AUTO_INCREMENT for table `sessions`
--
ALTER TABLE `sessions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Session ID', AUTO_INCREMENT=1;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'User ID', AUTO_INCREMENT=1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `favourite_coffee_drink`
--
ALTER TABLE `favourite_coffee_drink`
  ADD CONSTRAINT `favouritecoffeedrink_cofeeid_fk` FOREIGN KEY (`coffeeid`) REFERENCES `coffee_drinks` (`id`),
  ADD CONSTRAINT `favouritecoffeedrink_userid_fk` FOREIGN KEY (`userid`) REFERENCES `users` (`id`);

--
-- Constraints for table `sessions`
--
ALTER TABLE `sessions`
  ADD CONSTRAINT `sessions_userid_fk` FOREIGN KEY (`userid`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
