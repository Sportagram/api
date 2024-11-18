CREATE DATABASE sport_db;
USE sport_db;

CREATE TABLE user (
	userId VARCHAR (255) PRIMARY KEY,
    email VARCHAR(255),
    googlename VARCHAR(255),
    myteam VARCHAR(255),
    nickname VARCHAR(255),
    role VARCHAR(255)
);


CREATE TABLE team_news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_name VARCHAR(255),
    title VARCHAR(255),
    image_url TEXT,
    news_url VARCHAR(255),
    created_at DATETIME,
    category VARCHAR(255)
);