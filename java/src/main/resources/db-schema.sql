--
-- OpenCloudSync: Open source cloud synchronization solution; an extensible software that allows you to synchronize your data with different storage systems.
--
--     Copyright (C) 2012 Sebastien Dubois <seb__at__dsebastien.net>
--
--     This program is free software: you can redistribute it and/or modify
--     it under the terms of the GNU General Public License as published by
--     the Free Software Foundation, either version 3 of the License, or
--     any later version.
--
--     This program is distributed in the hope that it will be useful,
--     but WITHOUT ANY WARRANTY; without even the implied warranty of
--     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--     GNU General Public License for more details.
--
--     You should have received a copy of the GNU General Public License
--     along with this program.  If not, see <http://www.gnu.org/licenses/>.
--

-- References: 
-- http://www.h2database.com/html/grammar.html
-- http://www.h2database.com/html/datatypes.html
-- TODO delete later on:
DROP SCHEMA IF EXISTS OpenCloudSync;

CREATE SCHEMA IF NOT EXISTS OpenCloudSync; -- authorization ...
USE OpenCloudSync;

-- -----------------------------------------------------
-- Table OpenCloudSync.Tree
-- -----------------------------------------------------
DROP TABLE IF EXISTS OpenCloudSync.Tree;

CREATE TABLE IF NOT EXISTS OpenCloudSync.Tree(
  id_tree INT NOT NULL AUTO_INCREMENT,
  digest BINARY(20) NOT NULL UNIQUE,
  last_update_time TIMESTAMP NOT NULL,
  PRIMARY KEY (id_tree)
);


-- -----------------------------------------------------
-- Table OpenCloudSync.File
-- -----------------------------------------------------
DROP TABLE IF EXISTS OpenCloudSync.File ;

CREATE TABLE IF NOT EXISTS OpenCloudSync.File(
  id_file INT NOT NULL AUTO_INCREMENT,
  digest BINARY(20) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id_file)
);


-- -----------------------------------------------------
-- Table OpenCloudSync.FileChunk
-- -----------------------------------------------------
DROP TABLE IF EXISTS OpenCloudSync.FileChunk ;

CREATE TABLE IF NOT EXISTS OpenCloudSync.FileChunk(
  id_filechunk INT NOT NULL AUTO_INCREMENT,
  digest BINARY(20) NOT NULL UNIQUE,
  PRIMARY KEY (id_filechunk),
  UNIQUE INDEX digest_UNIQUE (digest ASC)
);


-- -----------------------------------------------------
-- Table OpenCloudSync.Tree_has_Files
-- -----------------------------------------------------
DROP TABLE IF EXISTS OpenCloudSync.Tree_has_Files;

CREATE TABLE IF NOT EXISTS OpenCloudSync.Tree_has_Files(
  id_tree INT NOT NULL,
  id_file INT NOT NULL,
  PRIMARY KEY (id_tree, id_file),
  CONSTRAINT fk_Trees_has_Files_tree
    FOREIGN KEY (id_tree)
    REFERENCES OpenCloudSync.Tree (id_tree),
  CONSTRAINT fk_Trees_has_Files_file
    FOREIGN KEY (id_file)
    REFERENCES OpenCloudSync.File (id_file),
);

-- -----------------------------------------------------
-- Table OpenCloudSync.File_has_Files
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS OpenCloudSync.File_has_Files(
  id_file_parent INT NOT NULL,
  id_file_child INT NOT NULL,
  PRIMARY KEY (id_file_parent, id_file_child),
  INDEX fk_File_has_File_child (id_file_child ASC) ,
  INDEX fk_File_has_File_parent (id_file_parent ASC) ,
  CONSTRAINT fk_File_has_File_parent
    FOREIGN KEY (id_file_parent)
    REFERENCES OpenCloudSync.File (id_file)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_File_has_File_child
    FOREIGN KEY (id_file_child)
    REFERENCES OpenCloudSync.File (id_file)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table OpenCloudSync.File_has_FileChunks
-- -----------------------------------------------------
DROP TABLE IF EXISTS OpenCloudSync.File_has_FileChunks;

CREATE TABLE IF NOT EXISTS OpenCloudSync.File_has_FileChunks(
  id_filehasfilechunks INT NOT NULL AUTO_INCREMENT,
  id_file INT NOT NULL,
  id_filechunk INT NOT NULL,
  PRIMARY KEY (id_filehasfilechunks),
  CONSTRAINT fk_Files_has_FileChunks_file
    FOREIGN KEY (id_file)
    REFERENCES OpenCloudSync.File (id_file),
  CONSTRAINT fk_Files_has_FileChunks_filechunk
    FOREIGN KEY (id_filechunk)
    REFERENCES OpenCloudSync.FileChunk (id_filechunk)
);

