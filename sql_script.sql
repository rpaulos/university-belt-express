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

-- creates university table (depends on university_location)
CREATE TABLE university (
	university_ID VARCHAR(15) PRIMARY KEY,
    university_location_ID VARCHAR(15) NOT NULL,
    university_name VARCHAR(50) UNIQUE NOT NULL,
    
    FOREIGN KEY (university_location_ID) 
    REFERENCES university_location(university_location_ID)
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

-- creates wallet with foreign key to who owns it
CREATE TABLE pandapay_wallet (
	customer_ID VARCHAR(15) UNIQUE NOT NULL,
    customer_balance FLOAT(10, 2) NOT NULL,
    
    FOREIGN KEY (customer_ID) 
    REFERENCES customer(customer_ID)
    ON DELETE CASCADE
);

-- INSERTING VALUES
-- School abrev, City, Location number
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
        
INSERT INTO customer (customer_ID, university_ID, customer_phone_number, customer_email, customer_first_name, customer_last_name)
	VALUES 
		('2023-00001', 'UST-0001', '09171234567', 'aynbernos@email.com', 'Ayn', 'Bernos'),
		('2023-00002', 'UST-0001', '09171234568', 'ejobiena@email.com', 'EJ', 'Obiena'),
		('2023-00003', 'NU-0002', '09171234569', 'raepaulos@email.com', 'Rae', 'Paulos'),
		('2023-00004', 'NU-0002', '09171234570', 'tristansevilla@email.com', 'Tristan', 'Sevilla'),
		('2023-00005', 'NU-0002', '09171234571', 'jaredpilapil@email.com', 'Jared', 'Pilapil');
        
DROP DATABASE ubeltexpress;

SELECT * FROM pandapay_wallet;
    
    


    
    