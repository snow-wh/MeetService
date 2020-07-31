create schema meet_service;
use meet_service;
create table members (id integer auto_increment primary key, login varchar(32),password varchar(32));
create table meet (id integer auto_increment primary key,name varchar(32),year int,month int,day int,time varchar(5),leadTeam VARCHAR(32));;