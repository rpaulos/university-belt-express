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
	university_location_ID VARCHAR(10) PRIMARY KEY,
    city VARCHAR(20) NOT NULL,
    street VARCHAR(20) NOT NULL,
    zip_code VARCHAR(4) NOT NULL
);

-- creates university table (depends on university_location)
CREATE TABLE university (
	university_ID VARCHAR(10) PRIMARY KEY,
    university_location_ID VARCHAR(10) NOT NULL,
    university_name VARCHAR(50) UNIQUE NOT NULL,
    
    FOREIGN KEY (university_location_ID) REFERENCES university_location(university_location_ID)
);
-- creates customer table
CREATE TABLE customer (
	customer_ID VARCHAR(10) PRIMARY KEY,
    university_ID VARCHAR(10) NOT NULL,
    pandapay_ID VARCHAR(10) UNIQUE NOT NULL,
    customer_phone_number VARCHAR(11) UNIQUE NOT NULL,
    customer_email VARCHAR(30) UNIQUE NOT NULL,
    customer_first_name VARCHAR(30) NOT NULL,
    customer_last_name VARCHAR(30) NOT NULL,
    customer_date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (university_ID) REFERENCES university(university_ID)
);

-- creates wallet with foreign key to who owns it
CREATE TABLE pandapay_wallet (
	pandapay_ID VARCHAR(10) PRIMARY KEY,
	customer_ID VARCHAR(10) UNIQUE NOT NULL,
    customer_balance FLOAT(10, 2) NOT NULL,
    
    FOREIGN KEY (customer_ID) REFERENCES customer(customer_ID)
);

-- add constraint to customer table to what wallet they own
ALTER TABLE customer
ADD CONSTRAINT fk_pandapay_ID
FOREIGN KEY (pandapay_ID) REFERENCES pandapay_wallet(pandapay_ID);

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

    
SELECT * FROM university_location;
SELECT * FROM university;

    
    
    