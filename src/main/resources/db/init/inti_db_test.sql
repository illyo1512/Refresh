insert into user Values
    (1, 'example@email.com', '흑길동',
    '남성', 'USER', '01012345678', 'password123',
    'user_image.jpg', false, '2023-10-01 12:00:00');

INSERT INTO user (email, nickname, gender, role, phone_number, password, user_image, created_at)
VALUES
('user1@example.com', 'Traveler1', 'male', 'USER', '010-1111-1111', 'pass123', 'image1.png', NOW()),
('user2@example.com', 'Explorer2', 'female', 'USER', '010-2222-2222', 'pass456', 'image2.png', NOW()),
('admin@example.com', 'Admin', 'other', 'ADMIN', '010-9999-9999', 'adminpass', 'admin.png', NOW());

INSERT INTO setting (user_id, night_mod, color_blind, alert_level)
VALUES
(1, TRUE, FALSE, 2),
(2, FALSE, TRUE, 3),
(3, TRUE, TRUE, 1);

INSERT INTO dm (user_id, content, sender, sent_at)
VALUES
(1, 'Hi! Is this route safe?', 'Explorer2', NOW()),
(2, 'Yes, I use it often!', 'Traveler1', NOW()),
(3, 'System update completed.', 'System', NOW());

INSERT INTO locate_info (place_name, place_location, place_detail)
VALUES
('Gangnam Station', 'Seoul, Gangnam-gu', 'Popular shopping and subway area'),
('Haeundae Beach', 'Busan, Haeundae-gu', 'Famous beach in Korea'),
('Namsan Tower', 'Seoul, Yongsan-gu', 'Tourist spot with city view');

INSERT INTO locate_review (user_id, locate_info_id, review_detail, created_at)
VALUES
(1, 1, 'Very crowded at night but safe.', NOW()),
(2, 2, 'Beautiful beach but be cautious at night.', NOW()),
(3, 3, 'Great view, but slippery stairs.', NOW());

INSERT INTO danger_detail (danger_type, danger_detail, danger_Countermeasure)
VALUES
('Theft', 'Frequent pickpocket incidents', 'Use anti-theft bags and stay alert'),
('Assault', 'Late-night assault reports', 'Avoid walking alone at night'),
('Traffic', 'High accident rate at crossing', 'Use pedestrian signals and crosswalks');

INSERT INTO realtime_danger (locate_name, danger_detail_id, place_location, occurred_at)
VALUES
('Gangnam Station Exit 2', 1, 'Seoul, Gangnam-gu, Exit 2', NOW()),
('Haeundae Entrance', 2, 'Busan, Haeundae-gu', NOW()),
('Namsan Park Trail', 3, 'Seoul, Yongsan-gu', NOW());

INSERT INTO real_time_notification (user_id, danger_id, detail_id, content, sent_at)
VALUES
(1, 1, 1, 'Warning: Theft near Gangnam Station Exit 2', NOW()),
(2, 2, 2, 'Caution: Assault reported near Haeundae', NOW()),
(3, 3, 3, 'Traffic hazard around Namsan trail', NOW());

INSERT INTO danger_record (detail_id, danger_location, danger_radius, danger_level)
VALUES
(1, 'Gangnam Station', 150, 3),
(2, 'Haeundae Beach', 200, 2),
(3, 'Namsan Tower', 100, 4);

INSERT INTO self_route (user_id, route_name, route_result, created_at)
VALUES
(1, 'Morning Commute', 'Gangnam → COEX', NOW()),
(2, 'Weekend Walk', 'Haeundae → BEXCO', NOW()),
(3, 'Evening Hike', 'Namsan Trail', NOW());

INSERT INTO saved_route (user_id, route_name, route_result, created_at)
VALUES
(1, 'My Safe Route', 'Route A: Gangnam → Home', NOW()),
(2, 'Beach Path', 'Haeundae to Centum City', NOW()),
(3, 'Hilltop View', 'Namsan Circle Trail', NOW());

INSERT INTO destination_self (self_route_id, info_id, destination_locate, destination_order)
VALUES
(1, 1, 'Gangnam Station', 1),
(2, 2, 'Haeundae Beach', 1),
(3, 3, 'Namsan Tower', 1);

INSERT INTO destination_save (saved_route_id, info_id, destination_locate, destination_order)
VALUES
(1, 1, 'Gangnam Station', 1),
(2, 2, 'Haeundae Beach', 1),
(3, 3, 'Namsan Tower', 1);

INSERT INTO board_category (category_detail, upper_category_id)
VALUES
('Travel Reviews', NULL),
('Safety Reports', NULL),
('Lost and Found', NULL);

INSERT INTO route_board (user_id, saved_route_id, self_route_id, title, content, category_id, created_at)
VALUES
(1, 1, 1, 'Safe morning commute', 'Used this for months with no issues.', 1, NOW()),
(2, 2, 2, 'Watch out near beach', 'Saw strange activity at night.', 2, NOW()),
(3, 3, 3, 'Lost item at Namsan', 'Dropped my phone near tower stairs.', 3, NOW());

INSERT INTO comment (user_id, route_board_id, content, created_at)
VALUES
(2, 1, 'Thanks for the info!', NOW()),
(3, 2, 'That sounds dangerous.', NOW()),
(1, 3, 'Hope you found it!', NOW());

INSERT INTO report (user_id, target_id, report_content, report_type, report_date)
VALUES
(1, 2, 'Inappropriate language', 'Abuse', NOW()),
(2, 3, 'Spam content', 'Spam', NOW()),
(3, 1, 'Misinformation', 'False Info', NOW());

INSERT INTO report_result (report_id, result_content, ban_period, result_date)
VALUES
(1, 'Warning issued, no ban.', 0, NOW()),
(2, 'Content removed. 3-day ban.', 3, NOW()),
(3, 'Verified info. No action.', 0, NOW());
