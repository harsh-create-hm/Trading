CREATE TABLE traders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trader_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE
);

CREATE TABLE portfolio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trader_id VARCHAR(50) UNIQUE
);

CREATE TABLE holdings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    portfolio_id BIGINT,
    stock VARCHAR(20),
    sector VARCHAR(20),
    quantity INT,
    UNIQUE (portfolio_id, stock)
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trader_id VARCHAR(50),
    stock VARCHAR(20),
    sector VARCHAR(20),
    quantity INT,
    side VARCHAR(10),
    status VARCHAR(20),
    created_at TIMESTAMP
);