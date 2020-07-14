UPDATE state_zip_codes SET location_with_addition = CONCAT(location, ' ', location_addition)
UPDATE state_zip_codes SET location_with_addition = REPLACE(location_with_addition, '  ', ' ')
UPDATE state_zip_codes SET location_with_addition = REPLACE(location_with_addition, ' /', '/')
