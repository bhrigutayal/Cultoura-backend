

-- Insert Date Plan Sections
INSERT INTO ACTIVITY_SECTION_MODEL  (title, plan_type,section_id) VALUES 
('Morning Date Activities', 'DATE',1),
('Afternoon Date Activities', 'DATE',2),
('Evening Date Activities', 'DATE',3);

-- Insert Hangout (Friends) Plan Sections
INSERT INTO ACTIVITY_SECTION_MODEL (title, plan_type,section_id) VALUES 
('Morning Hangout Activities', 'FRIENDS',1),
('Afternoon Hangout Activities', 'FRIENDS',2),
('Evening Hangout Activities', 'FRIENDS',3);

-- Insert Family Plan Sections
INSERT INTO ACTIVITY_SECTION_MODEL (title, plan_type,section_id) VALUES 
('Morning Family Activities', 'FAMILY',1),
('Afternoon Family Activities', 'FAMILY',2),
('Evening Family Activities', 'FAMILY',3);

-- Insert Vacation Plan Sections
INSERT INTO ACTIVITY_SECTION_MODEL (title, plan_type,section_id) VALUES 
('Morning Vacation Activities', 'VACATION',1),
('Afternoon Vacation Activities', 'VACATION',2),
('Evening Vacation Activities', 'VACATION',3);
-- Insert Date Plan Activities
INSERT INTO ACTIVITY_ITEM_MODEL  (id, title, description, image_resource_id, start_hour, end_hour, cost, rating, date, section_id) VALUES 
-- Morning Date Activities
(1, 'Coffee Shop Date', 'Romantic morning coffee at a cozy cafe', 1, 8, 10, 15.50, 4.5, '2025-04-01', 1),
(13, 'Morning Walk in Park', 'Scenic walk in a beautiful park', 2, 9, 11, 0, 4.7, '2025-04-01', 1),

-- Afternoon Date Activities
(2, 'Art Museum Visit', 'Explore local art together', 3, 12, 14, 25.00, 4.3, '2025-04-01', 2),
(14, 'Lunch at Romantic Restaurant', 'Intimate lunch date', 4, 13, 15, 75.00, 4.8, '2025-04-01', 2),

-- Evening Date Activities
(3, 'Sunset Boat Ride', 'Romantic evening boat cruise', 5, 18, 20, 100.00, 4.9, '2025-04-01', 3),
(15, 'Dinner and Movie', 'Classic date night', 6, 19, 22, 80.00, 4.6, '2025-04-01', 3);

-- Insert Friends Hangout Activities
INSERT INTO ACTIVITY_ITEM_MODEL  (id, title, description, image_resource_id, start_hour, end_hour, cost, rating, date, section_id) VALUES 
-- Morning Hangout Activities
(4, 'Brunch Meetup', 'Weekend brunch with friends', 7, 10, 12, 30.00, 4.4, '2025-04-02', 1),
(16, 'Morning Hiking', 'Group hiking adventure', 8, 8, 11, 10.00, 4.6, '2025-04-02', 1),

-- Afternoon Hangout Activities
(5, 'Bowling', 'Fun bowling session', 9, 13, 15, 40.00, 4.5, '2025-04-02', 2),
(18, 'Board Game Cafe', 'Afternoon board games', 10, 14, 16, 25.00, 4.7, '2025-04-02', 2),

-- Evening Hangout Activities
(6, 'Karaoke Night', 'Singing and fun with friends', 11, 20, 23, 50.00, 4.8, '2025-04-02', 3),
(17, 'Pub Crawl', 'Evening bar hopping', 12, 21, 1, 75.00, 4.6, '2025-04-02', 3);

-- Insert Family Activities
INSERT INTO ACTIVITY_ITEM_MODEL  (id, title, description, image_resource_id, start_hour, end_hour, cost, rating, date, section_id) VALUES 
-- Morning Family Activities
(7, 'Pancake Breakfast', 'Family breakfast cooking', 13, 8, 10, 20.00, 4.7, '2025-04-03', 1),
(19, 'Zoo Visit', 'Family trip to the zoo', 14, 9, 12, 60.00, 4.5, '2025-04-03', 1),

-- Afternoon Family Activities
(8, 'Picnic in the Park', 'Family picnic and games', 15, 12, 15, 30.00, 4.6, '2025-04-03', 2),
(20, 'Bicycle Ride', 'Family bicycle adventure', 16, 14, 16, 0, 4.4, '2025-04-03', 2),

-- Evening Family Activities
(9, 'Movie Night', 'Family movie and popcorn', 17, 19, 22, 25.00, 4.8, '2025-04-03', 3),
(21, 'Stargazing', 'Night sky observation', 18, 20, 22, 0, 4.9, '2025-04-03', 3);

-- Insert Vacation Activities
INSERT INTO ACTIVITY_ITEM_MODEL  (id, title, description, image_resource_id, start_hour, end_hour, cost, rating, date, section_id) VALUES 
-- Morning Vacation Activities
(10, 'Beach Sunrise', 'Morning beach walk', 19, 6, 8, 0, 4.9, '2025-04-04', 1),
(22, 'Local Market Tour', 'Explore local markets', 20, 8, 11, 25.00, 4.7, '2025-04-04', 1),

-- Afternoon Vacation Activities
(11, 'Historical Site Visit', 'Explore local history', 21, 12, 15, 50.00, 4.6, '2025-04-04', 2),
(23, 'Adventure Sports', 'Exciting afternoon activities', 22, 14, 17, 100.00, 4.8, '2025-04-04', 2),

-- Evening Vacation Activities
(12, 'Local Cuisine Dinner', 'Taste local restaurant specialties', 23, 19, 21, 75.00, 4.7, '2025-04-04', 3),
(24, 'Cultural Performance', 'Evening entertainment show', 24, 20, 22, 60.00, 4.5, '2025-04-04', 3);