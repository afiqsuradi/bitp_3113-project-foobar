CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    total DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'PAID') NOT NULL,
    point DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);