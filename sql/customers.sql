-- Name:                 Amelia Hamulewicz 
-- Student Number:       C00296605 
-- Title:                Customer
-- Date:                 4 March 2025

CREATE TABLE Customer 
(
    customerId INT AUTO_INCREMENT PRIMARY KEY,
    fname VARCHAR(50) NOT NULL,
    sname VARCHAR(50) NOT NULL,
    address VARCHAR(255),
    email VARCHAR(100) NOT NULL UNIQUE,
    phone CHAR(10) NOT NULL
);

CREATE TABLE Invoice 
(
    invoiceId INT AUTO_INCREMENT PRIMARY KEY,
    customerId INT NOT NULL,
    totalAmount DECIMAL(10, 2) NOT NULL,
    invoiceDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customerId) REFERENCES Customer(customerId)
);

CREATE TABLE Product 
(
    productId INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL
);