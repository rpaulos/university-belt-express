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

SELECT * FROM restaurant_location;

-- Alter the table to add a password field
ALTER TABLE customer
ADD COLUMN customer_password VARCHAR(30) NOT NULL
AFTER customer_email;

UPDATE customer SET customer_password = 'password123' WHERE customer_ID = '2023-00001';
UPDATE customer SET customer_password = 'securePass456' WHERE customer_ID = '2023-00002';
UPDATE customer SET customer_password = 'mySecret789' WHERE customer_ID = '2023-00003';
UPDATE customer SET customer_password = 'tristanPwd2025' WHERE customer_ID = '2023-00004';
UPDATE customer SET customer_password = 'jaredKey321' WHERE customer_ID = '2023-00005';

SELECT * FROM customer;

DELETE FROM customer WHERE customer_ID = '2025-00001';

ALTER TABLE customer
MODIFY COLUMN customer_email VARCHAR(100) NOT NULL UNIQUE;

INSERT INTO restaurant_location (restaurant_location_ID, city, street, zip_code)
VALUES
	('RL002', 'Manila', 'Lacson Avenue, Sampaloc', '1008'),
	('RL003', 'Manila', 'Dapitan Street, Sampaloc', '1008'),
	('RL004', 'Manila', 'P. Noval Street, Sampaloc', '1008'),
	('RL005', 'Manila', 'Lerma Street, Sampaloc', '1008'),
	('RL006', 'Manila', 'Recto Avenue, Sampaloc', '1008'),
	('RL007', 'Manila', 'Morayta Street, Sampaloc', '1008'),
	('RL008', 'Manila', 'M. Dela Fuente Street, Sampaloc', '1008'),
	('RL009', 'Manila', 'Earnshaw Street, Sampaloc', '1008'),
	('RL010', 'Manila', 'G. Tolentino Street, Sampaloc', '1008');
    
INSERT INTO restaurant (restaurant_ID, restaurant_location_ID, restaurant_name)
VALUES
	('RS002', 'RL002', 'Mang Inasal Lacson'),
	('RS003', 'RL002', 'Chowking Lacson'),
	('RS004', 'RL003', 'Jollibee Dapitan'),
	('RS005', 'RL004', 'Tapa King P. Noval'),
	('RS006', 'RL005', 'Angel’s Burger Lerma'),
	('RS007', 'RL006', 'McDonald’s Recto'),
	('RS008', 'RL007', 'Greenwich Morayta'),
	('RS009', 'RL009', 'Pares Retiro Earnshaw'),
	('RS010', 'RL010', 'Samgyupsalamat Tolentino');
    
SELECT * FROM restaurant;

ALTER TABLE restaurant ADD restaurant_header_path VARCHAR(300);

UPDATE restaurant
SET restaurant_header_path = CASE restaurant_name
    WHEN 'Jollibee Legarda Bustillos' THEN 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\Jollibee Legarda Bustillos.png'
    WHEN 'Mang Inasal Lacson' THEN 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\Mang Inasal Lacson.png'
    WHEN 'Chowking Lacson' THEN 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\Chowking Lacson.png'
    WHEN 'Jollibee Dapitan' THEN 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\Jollibee Dapitan.png'
    WHEN 'Tapa King P. Noval' THEN 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\Tapa King P. Noval.png'
    WHEN 'Angel’s Burger Lerma' THEN 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\Angel’s Burger Lerma.png'
    WHEN 'McDonald’s Recto' THEN 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\McDonald’s Recto.png'
    WHEN 'Greenwich Morayta' THEN 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\Greenwich Morayta.png'
    WHEN 'Pares Retiro Earnshaw' THEN 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\Pares Retiro Earnshaw.png'
    WHEN 'Samgyupsalamat Tolentino' THEN 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\Samgyupsalamat Tolentino.png'
    ELSE restaurant_header_path
END
WHERE restaurant_name IN (
    'Jollibee Legarda Bustillos',
    'Mang Inasal Lacson',
    'Chowking Lacson',
    'Jollibee Dapitan',
    'Tapa King P. Noval',
    'Angel’s Burger Lerma',
    'McDonald’s Recto',
    'Greenwich Morayta',
    'Pares Retiro Earnshaw',
    'Samgyupsalamat Tolentino'
);

INSERT INTO restaurant (restaurant_ID, restaurant_location_ID, restaurant_name, restaurant_header_path)
VALUES
	('RS011', 'RL002', 'Super Daddy Milktea Jhocson', 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\Super Daddy Milktea Jhocson.png');

DELETE FROM restaurant
WHERE restaurant_ID = 'RS011';
    
ALTER TABLE product
ADD COLUMN product_description VARCHAR(500);

ALTER TABLE product
ADD COLUMN product_image_path VARCHAR(300);

SELECT * FROM product;

UPDATE product
SET product_description = 'Burger steak with flavorful mushroom gravy and rice.',
    product_image_path = 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Products\\Jollibee\\Burger Steak.png'
WHERE product_ID = 'P00001';

UPDATE product
SET product_description = 'Sweet-style Filipino spaghetti topped with cheese and hotdog slices.',
    product_image_path = 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Products\\Jollibee\\Jollispaghetti.png'
WHERE product_ID = 'P00002';

UPDATE product
SET product_description = 'Crispy fried chicken with rice and gravy.',
    product_image_path = 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Products\\Jollibee\\1pc Chickenjoy.png'
WHERE product_ID = 'P00003';

SELECT * FROM customer;

DELETE FROM restaurant
WHERE restaurant_ID = "RS010";

DELETE FROM restaurant
WHERE restaurant_ID = "RS010";


    
    