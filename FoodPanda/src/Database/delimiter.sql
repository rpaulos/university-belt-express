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

CREATE TRIGGER create_wallet_after_owner_insert
AFTER INSERT ON business_owner
FOR EACH ROW
BEGIN
    INSERT INTO business_owner_pandapay_wallet (business_owner_ID, business_owner_balance)
    VALUES (NEW.business_owner_ID, 0.00);
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER update_price_range_after_insert
AFTER INSERT ON product
FOR EACH ROW
BEGIN
  DECLARE avg_price DECIMAL(10,2);
  DECLARE new_price_range VARCHAR(10);

  SELECT AVG(product_price)
  INTO avg_price
  FROM product
  WHERE restaurant_ID = NEW.restaurant_ID;

  IF avg_price < 100 THEN
    SET new_price_range = '₱';
  ELSEIF avg_price < 300 THEN
    SET new_price_range = '₱₱';
  ELSE
    SET new_price_range = '₱₱₱';
  END IF;

  UPDATE restaurant
  SET price_range = new_price_range
  WHERE restaurant_ID = NEW.restaurant_ID;
END;
$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER update_price_range_after_update
AFTER UPDATE ON product
FOR EACH ROW
BEGIN
  DECLARE avg_price DECIMAL(10,2);
  DECLARE new_price_range VARCHAR(10);

  SELECT AVG(product_price)
  INTO avg_price
  FROM product
  WHERE restaurant_ID = NEW.restaurant_ID;

  IF avg_price < 100 THEN
    SET new_price_range = '₱';
  ELSEIF avg_price < 300 THEN
    SET new_price_range = '₱₱';
  ELSE
    SET new_price_range = '₱₱₱';
  END IF;

  UPDATE restaurant
  SET price_range = new_price_range
  WHERE restaurant_ID = NEW.restaurant_ID;
END;
$$

DELIMITER ;



DROP TRIGGER IF EXISTS trg_update_price_range;
DROP TRIGGER IF EXISTS trg_update_price_range_update;
DROP TRIGGER IF EXISTS set_default_price_range;

SHOW TRIGGERS;

