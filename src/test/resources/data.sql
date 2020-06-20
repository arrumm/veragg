-- states with zip code ranges
INSERT INTO state (id, name) VALUES ('BE', 'Berlin');
INSERT INTO state_zip_ranges (state_id, from, to) VALUES ('BE', '10115', '14199');

INSERT INTO state (id, name) VALUES ('SL', 'Saarland');
INSERT INTO state_zip_ranges (state_id, from, to) VALUES ('SL', '66000', '66999');
