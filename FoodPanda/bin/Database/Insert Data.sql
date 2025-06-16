INSERT INTO restaurant_location (restaurant_location_ID, city, address, zip_code) VALUES
('RL001', 'Makati', '123 Ayala Ave', '1226'),
('RL002', 'Quezon City', '45 Katipunan Ave', '1108'),
('RL003', 'Taguig', '11th Ave, BGC', '1630');

INSERT INTO price_range (price_range_ID, price_range_level) VALUES
('PR001', '₱'),
('PR002', '₱₱'),
('PR003', '₱₱₱');

INSERT INTO restaurant (
    restaurant_ID,
    restaurant_location_ID,
    restaurant_name,
    price_range_ID,
    restaurant_header_path
) VALUES 
('REST001', 'RL001', 'Mang Inasal', 'PR001', 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\mang_inasal.jpg'),
('REST002', 'RL002', 'Shakey\'s Pizza', 'PR002', 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\shakeys.jpg'),
('REST003', 'RL003', 'Conti\'s Bakeshop & Restaurant', 'PR003', 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\contis.jpg'),
('REST004', 'RL001', 'Jollibee', 'PR001', 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\jollibee.jpg'),
('REST005', 'RL002', 'Army Navy', 'PR002', 'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\armynavy.jpg');


UPDATE restaurant
SET restaurant_header_path = CONCAT(
    'C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\',
    restaurant_ID,
    '.png'
)
WHERE restaurant_ID IN (
    'R_RLMAN_00001_001',
    'R_RLMAN_00002_001',
    'R_RLMAN_00003_001',
    'R_RLMAN_00005_001'
);

DELETE FROM restaurant
WHERE restaurant_ID = 'R_RLMAN_00005_001';

DELETE FROM restaurant_location
WHERE restaurant_location_ID = "RLMAN_00005";

ALTER TABLE product
ADD COLUMN product_image_path VARCHAR(500);

UPDATE restaurant
SET price_range_ID = 'PR001'
WHERE price_range_ID IS NULL;

-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Alter the column with default value
ALTER TABLE restaurant
MODIFY price_range_ID VARCHAR(15) NOT NULL DEFAULT 'PR001';

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

DELETE FROM business_owner
WHERE business_owner_id IN (
  'BO_R_RLMAN_00003_001_001',
  'BO_R_RLMAN_00004_001_001',
  'BO_R_RLMAN_00005_001_001',
  'BO_R_RLMAN_00006_001_001',
  'BO_R_RLMAN_00007_001_001',
  'BO_R_RLMAN_00008_001_001',
  'BO_R_RLMAN_00009_001_001',
  'BO_R_RLMAN_00010_001_001',
  'BO_R_RLMAN_00011_001_001',
  'BO_R_RLMAN_00012_001_001',
  'BO_R_RLMAN_00013_001_001',
  'BO_R_RLMAN_00014_001_001'
);

DELETE FROM restaurant
WHERE restaurant_ID IN (
  'R_RLMAN_00003_001',
  'R_RLMAN_00004_001',
  'R_RLMAN_00005_001',
  'R_RLMAN_00006_001',
  'R_RLMAN_00007_001',
  'R_RLMAN_00008_001',
  'R_RLMAN_00009_001',
  'R_RLMAN_00010_001',
  'R_RLMAN_00011_001',
  'R_RLMAN_00012_001',
  'R_RLMAN_00013_001',
  'R_RLMAN_00014_001'
);

DELETE FROM restaurant_location
WHERE restaurant_location_ID IN (
  'RLMAN_00003',
  'RLMAN_00004',
  'RLMAN_00005',
  'RLMAN_00006',
  'RLMAN_00007',
  'RLMAN_00008',
  'RLMAN_00009',
  'RLMAN_00010',
  'RLMAN_00011',
  'RLMAN_00012',
  'RLMAN_00013',
  'RLMAN_00014'
);

DELETE FROM restaurant WHERE restaurant_ID = "R_RLMAN_00013_001";





