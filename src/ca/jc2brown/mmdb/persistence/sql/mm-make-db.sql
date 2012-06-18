--
-- Create database
--
CREATE DATABASE IF NOT EXISTS `ioidb`;
USE `ioidb`;

--
-- List of tables
--
/*
Genres
MovieActors
MovieDirectors
MovieFiles
MovieGenres
MovieRatings
Movies
MovieWriters
People
Ratings
*/

--
-- Drop any existing tables
--
DROP TABLE IF EXISTS `Genres`;
DROP TABLE IF EXISTS `MovieActors`;
DROP TABLE IF EXISTS `MovieDirectors`;
DROP TABLE IF EXISTS `MovieFiles`;
DROP TABLE IF EXISTS `MovieGenres`;
DROP TABLE IF EXISTS `MovieRatings`;
DROP TABLE IF EXISTS `Movies`;
DROP TABLE IF EXISTS `MovieWriters`;
DROP TABLE IF EXISTS `People`;
DROP TABLE IF EXISTS `Ratings`;

--
-- Create tables
--

CREATE TABLE `Genres` (
  `Id` 			bigint(20) 		NOT NULL,
  `Genre` 		varchar(255) 	NOT NULL,
  PRIMARY KEY  (`Id`)
);

CREATE TABLE `MovieActors` (
  `Id` 			bigint(20) 		NOT NULL,
  `MovieId` 	bigint(20) 		NOT NULL,
  `ActorId` 	bigint(20) 		NOT NULL,
  PRIMARY KEY  (`Id`)
);

CREATE TABLE `MovieDirectors` (
  `Id` 			bigint(20) 		NOT NULL,
  `MovieId` 	bigint(20) 		NOT NULL,
  `DirectorId` 	bigint(20) 		NOT NULL,
  PRIMARY KEY  (`Id`)
);

CREATE TABLE `MovieFiles` (
  `Id` 			bigint(20) 		NOT NULL,
  `Filename` 	varchar(255) 	NOT NULL,
  `Origin` 		varchar(255) 	NOT NULL,
  PRIMARY KEY  (`Id`)
);

CREATE TABLE `MovieGenres` (
  `Id` 			bigint(20) 		NOT NULL,
  `MovieId` 	bigint(20) 		NOT NULL,
  `GenreId` 	bigint(20) 		NOT NULL,
  PRIMARY KEY  (`Id`)
);

CREATE TABLE `MovieRatings` (
  `Id` 			bigint(20) 		NOT NULL,
  `MovieId` 	bigint(20) 		NOT NULL,
  `RatingId` 	bigint(20) 		NOT NULL,
  PRIMARY KEY  (`Id`)
);
	
CREATE TABLE `Movies` (
  `Id` 			bigint(20) 		NOT NULL,
  `MovieFileId` bigint(20) 		NOT NULL,
  `Plot` 		varchar(65535) 	default NULL,
  `Rating` 		varchar(16) 	default NULL,
  `Reception` 	varchar(16) 	default NULL,
  `Released` 	varchar(255) 	default NULL,
  `Runtime` 	varchar(255) 	default NULL,
  `Title` 		varchar(255) 	NOT NULL,
  `Year` 		varchar(16) 	default NULL,
  PRIMARY KEY  (`Id`)
);

CREATE TABLE `MovieWriters` (
  `Id` 			bigint(20) 		NOT NULL,
  `MovieId` 	bigint(20) 		NOT NULL,
  `WriterId` 	bigint(20) 		NOT NULL,
  PRIMARY KEY  (`Id`)
);

CREATE TABLE `People` (
  `Id` 			bigint(20) 		NOT NULL,
  `Firstname` 	varchar(255) 	default NULL,
  `Lastname` 	varchar(255) 	NOT NULL default "",
  PRIMARY KEY  (`Id`)
);

CREATE TABLE `Ratings` (
  `Id` 					bigint(20) 		NOT NULL,
  `PreferredRatingId` 	bigint(20) 		default NULL,
  `Rating` 				varchar(255) 	NOT NULL,
  PRIMARY KEY  (`Id`)
);


--
-- Add foreign keys
--
	
ALTER TABLE 		MovieActors
	ADD FOREIGN KEY (MovieId)	
	REFERENCES Movies(Id);	
ALTER TABLE 		MovieActors
	ADD FOREIGN KEY (ActorId)	
	REFERENCES People(Id);	
	
ALTER TABLE 		MovieDirectors
	ADD FOREIGN KEY (MovieId)	
	REFERENCES Movies(Id);	
ALTER TABLE 		MovieDirectors
	ADD FOREIGN KEY (DirectorId)	
	REFERENCES People(Id);	

ALTER TABLE 		MovieGenres
	ADD FOREIGN KEY (MovieId)	
	REFERENCES Movies(Id);	
ALTER TABLE 		MovieGenres
	ADD FOREIGN KEY (GenreId)	
	REFERENCES Genres(Id);	
	
ALTER TABLE 		MovieRatings
	ADD FOREIGN KEY (MovieId)	
	REFERENCES Movies(Id);	
ALTER TABLE 		MovieRatings
	ADD FOREIGN KEY (RatingId)	
	REFERENCES Rating(Id);
	
ALTER TABLE 		Movies
	ADD FOREIGN KEY (MovieFileId)	
	REFERENCES Moviefiles(Id);	
	
ALTER TABLE 		MovieWriters
	ADD FOREIGN KEY (MovieId)	
	REFERENCES Movies(Id);	
ALTER TABLE 		MovieWriters
	ADD FOREIGN KEY (WriterId)	
	REFERENCES People(Id);	

	

