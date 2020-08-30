UPDATE state_zip_codes SET location_with_addition = CONCAT(location, ' ', location_addition)
UPDATE state_zip_codes SET location_with_addition = REPLACE(location_with_addition, ' /', '/')
UPDATE state_zip_codes SET location_with_addition = REPLACE(location_with_addition, '  ', ' ')
UPDATE state_zip_codes SET location_with_addition = CASE RIGHT(location_with_addition, 1) WHEN ' ' THEN LEFT(location_with_addition, LENGTH(location_with_addition)-1) ELSE location_with_addition END
