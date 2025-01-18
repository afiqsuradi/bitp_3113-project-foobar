CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    membership_id INT,
    status ENUM('DONE', 'PROCESSING'),
    FOREIGN KEY (membership_id) REFERENCES memberships(id)
);