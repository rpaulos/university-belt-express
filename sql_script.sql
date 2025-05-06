CREATE DATABASE ubeltexpress;
USE ubeltexpress;

-- creates the admin table
CREATE TABLE admins (
	admin_ID VARCHAR(10) PRIMARY KEY,
    admin_username VARCHAR(20) UNIQUE NOT NULL,
    admin_password VARCHAR(30) NOT NULL,
    admin_email VARCHAR(30) UNIQUE NOT NULL,
    admin_first_name VARCHAR(20) NOT NULL,
    admin_last_name VARCHAR(20) NOT NULL,
    admin_date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- creates the table for storing location of universities (no dependecies)
CREATE TABLE university_location (
	university_location_ID VARCHAR(15) PRIMARY KEY,
    city VARCHAR(20) NOT NULL,
    street VARCHAR(50) NOT NULL,
    zip_code VARCHAR(4) NOT NULL
);

-- creates the table for storing location of restaurants (no dependecies)
CREATE TABLE restaurant_location (
	restaurant_location_ID VARCHAR(15) PRIMARY KEY,
    city VARCHAR(20) NOT NULL,
    street VARCHAR(50) NOT NULL,
    zip_code VARCHAR(4) NOT NULL
);

-- creates university table (depends on university_location)
CREATE TABLE university (
	university_ID VARCHAR(15) PRIMARY KEY,
    university_location_ID VARCHAR(15) NOT NULL,
    university_name VARCHAR(50) UNIQUE NOT NULL,
    
    FOREIGN KEY (university_location_ID) 
    REFERENCES university_location(university_location_ID)
	ON DELETE CASCADE
);

-- creates restaurant table (depends on restaurant_location)
CREATE TABLE restaurant (
	restaurant_ID VARCHAR(15) PRIMARY KEY,
    restaurant_location_ID VARCHAR(15) NOT NULL,
    restaurant_name VARCHAR(50) NOT NULL,
    
    FOREIGN KEY (restaurant_location_ID) 
    REFERENCES restaurant_location(restaurant_location_ID)
	ON DELETE CASCADE
);
-- creates customer table
CREATE TABLE customer (
	customer_ID VARCHAR(15) PRIMARY KEY,
    university_ID VARCHAR(15) NOT NULL,
    customer_phone_number VARCHAR(11) UNIQUE NOT NULL,
    customer_email VARCHAR(30) UNIQUE NOT NULL,
    customer_first_name VARCHAR(30) NOT NULL,
    customer_last_name VARCHAR(30) NOT NULL,
    customer_date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (university_ID) 
    REFERENCES university(university_ID)
    ON DELETE CASCADE
);

CREATE TABLE price_range (
	price_range_ID VARCHAR(15) PRIMARY KEY,
    price_range_level VARCHAR(20) NOT NULL
);

-- create product table
CREATE TABLE product (
	product_ID VARCHAR(15) PRIMARY KEY,
    restaurant_ID VARCHAR(15) NOT NULL,
    price_range_ID VARCHAR(15) NOT NULL,
    product_name VARCHAR(20) NOT NULL,
    product_quantity INT NOT NULL,
    product_price DECIMAL(10, 2) NOT NULL,
    
    FOREIGN KEY (price_range_ID)
    REFERENCES price_range(price_range_ID)
);

-- creates wallet with foreign key to who owns it
CREATE TABLE pandapay_wallet (
	customer_ID VARCHAR(15) UNIQUE NOT NULL,
    customer_balance FLOAT(10, 2) NOT NULL,
    
    FOREIGN KEY (customer_ID) 
    REFERENCES customer(customer_ID)
    ON DELETE CASCADE
);

-- creates wallet with foreign key to who owns it
CREATE TABLE restaurant_pandapay_wallet (
	restaurant_ID VARCHAR(15) UNIQUE NOT NULL,
    restaurant_balance FLOAT(10, 2) NOT NULL,
    
    FOREIGN KEY (restaurant_ID) 
    REFERENCES restaurant(restaurant_ID)
    ON DELETE CASCADE
);

-- Auto creates a wallet everytime a new user is added
DELIMITER $$

CREATE TRIGGER create_wallet_after_customer_insert
AFTER INSERT ON customer
FOR EACH ROW
BEGIN
    INSERT INTO pandapay_wallet (customer_ID, customer_balance)
    VALUES (NEW.customer_ID, 0.00);
END$$

DELIMITER ;

-- Auto creates a wallet everytime a new restaurant is added
DELIMITER $$

CREATE TRIGGER create_wallet_after_restaurant_insert
AFTER INSERT ON restaurant
FOR EACH ROW
BEGIN
    INSERT INTO restaurant_pandapay_wallet (restaurant_ID, restaurant_balance)
    VALUES (NEW.restaurant_ID, 0.00);
END$$

DELIMITER ;
        
-- INSERTING VALUES
-- School abrev, City
INSERT INTO university_location(university_location_ID, city, street, zip_code)
	VALUES
		('UST_MNL', 'Manila', 'España Blvd.', '1008'),
        ('NU_MNL', 'Manila', 'Jhocson St.', '1008'),
        ('FEU_MNL', 'Manila', 'Nicanor Reyes St.', '1008'),
		('CEU_MNL', 'Manila', 'Mendiola St.', '1005'),
		('SBU_MNL', 'Manila', 'Mendiola St.', '1005'),
		('MAP_MNL', 'Manila', 'Muralla St., Intramuros', '1002');

-- School abrev, number of school added
INSERT INTO university (university_ID, university_location_ID, university_name)
	VALUES
		('UST-0001', 'UST_MNL', 'University of Santo Tomas'),
        ('NU-0002', 'NU_MNL', 'National University'),
        ('FEU-0003', 'FEU_MNL', 'Far Eastern University'),
        ('CEU-0004', 'CEU_MNL', 'Centro Escolar University'),
        ('SBU-0005', 'SBU_MNL', 'San Beda University'),
        ('MAP-0006', 'MAP_MNL', 'Mapúa University');

-- Customers
INSERT INTO customer (customer_ID, university_ID, customer_phone_number, customer_email, customer_first_name, customer_last_name)
	VALUES 
		('2023-00001', 'UST-0001', '09171234567', 'aynbernos@email.com', 'Ayn', 'Bernos'),
		('2023-00002', 'UST-0001', '09171234568', 'ejobiena@email.com', 'EJ', 'Obiena'),
		('2023-00003', 'NU-0002', '09171234569', 'raepaulos@email.com', 'Rae', 'Paulos'),
		('2023-00004', 'NU-0002', '09171234570', 'tristansevilla@email.com', 'Tristan', 'Sevilla'),
		('2023-00005', 'NU-0002', '09171234571', 'jaredpilapil@email.com', 'Jared', 'Pilapil');
        
-- Insert into price_range
INSERT INTO price_range (price_range_ID, price_range_level)
	VALUES 
	  ('PR1', 'Affordable'),
	  ('PR2', 'Mid-range'),
	  ('PR3', 'Expensive');
    
-- Insert into restaurant_location
INSERT INTO restaurant_location (restaurant_location_ID, city, street, zip_code)
	VALUES 
		('RL001', 'Manila', 'Legarda St., Sampaloc', '1008');

-- Insert into restaurant
INSERT INTO restaurant (restaurant_ID, restaurant_location_ID, restaurant_name)
	VALUES 
		('R001', 'RL001', 'Jollibee Legarda Bustillos');

-- Insert into product (restaurant_pandapay_wallet is auto-handled by trigger)
INSERT INTO product (product_ID, restaurant_ID, price_range_ID, product_name, product_quantity, product_price)
	VALUES 
	  ('P00001', 'R001', 'PR1', 'Burger Steak', 50, 60.00),
	  ('P00002', 'R001', 'PR1', 'Jollispaghetti', 50, 65.00),
	  ('P00003', 'R001', 'PR1', '1pc Chickenjoy', 50, 89.50);
      
SELECT * FROM product;

SELECT * FROM customers;



    


    
    