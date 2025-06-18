-- DROP DATABASE capstone;
CREATE DATABASE IF NOT EXISTS dodge_travel;
use dodge_travel;

-- 사용자 테이블
CREATE TABLE user (
    user_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    nickname VARCHAR(50),
    id VARCHAR(50) UNIQUE,
    gender VARCHAR(10),
    role VARCHAR(20),
    phone_number VARCHAR(20),
    user_image VARCHAR(255),
    user_ban BOOL DEFAULT FALSE,
    created_at DATETIME
);

-- 설정 테이블
CREATE TABLE setting (
    setting_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    alert_level INT,
    night_mod BOOL,
    color_blind BOOL,
    PRIMARY KEY (setting_id, user_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 개인 메시지 테이블
CREATE TABLE dm (
    dm_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    sender VARCHAR(50),
    content TEXT,
    sent_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 장소 정보 테이블
CREATE TABLE locate_info (
    info_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    place_name VARCHAR(100),
    place_location VARCHAR(100),
    place_detail TEXT
);

-- 장소 리뷰 테이블
CREATE TABLE locate_review (
    review_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    locate_info_id INT NOT NULL,
    review_detail TEXT,
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (locate_info_id) REFERENCES locate_info(info_id)
);

-- 위험 종류 테이블
CREATE TABLE danger_detail (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    danger_type VARCHAR(50),
    danger_detail TEXT,
    danger_Countermeasure TEXT
);

-- 실시간 위험 수집 테이블
CREATE TABLE realtime_danger (
    danger_id INT PRIMARY KEY AUTO_INCREMENT,
    danger_detail_id INT NOT NULL,
    locate_name VARCHAR(100),
    place_location VARCHAR(200),
    occurred_at DATETIME,
    FOREIGN KEY (danger_detail_id) REFERENCES danger_detail(detail_id)
);

-- 실시간 알림 테이블
CREATE TABLE real_time_notification (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    danger_id INT NOT NULL,
    detail_id INT NOT NULL,
    content TEXT,
    sent_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (detail_id) REFERENCES danger_detail(detail_id),
    FOREIGN KEY (danger_id) REFERENCES realtime_danger(danger_id)
);

-- 위험 지역 기록 정보 테이블
CREATE TABLE danger_record (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    detail_id INT NOT NULL,
    bbox_min_lng DOUBLE,
    bbox_min_lat DOUBLE,
    bbox_max_lng DOUBLE,
    bbox_max_lat DOUBLE,
    danger_json_path VARCHAR(255),
    FOREIGN KEY (detail_id) REFERENCES danger_detail(detail_id)
); 

-- 자신 경로 테이블
CREATE TABLE self_route (
    self_route_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    route_name VARCHAR(200),
    route_result TEXT,
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 즐겨찾기 경로 테이블
CREATE TABLE saved_route (
    saved_route_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    route_name VARCHAR(200),
    route_result TEXT,
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 자신 경로 목적지 테이블
CREATE TABLE destination_self (
    destination_id INT AUTO_INCREMENT,
    self_route_id INT NOT NULL,
    info_id INT,
    destination_order INT NOT NULL,
    destination_locate TEXT,
    PRIMARY KEY (destination_id, self_route_id),
    FOREIGN KEY (info_id) REFERENCES locate_info(info_id),
    FOREIGN KEY (self_route_id) REFERENCES self_route(self_route_id)
);

-- 즐겨찾기 경로 목적지 테이블
CREATE TABLE destination_save (
    destination_id INT AUTO_INCREMENT,
    saved_route_id INT NOT NULL,
    info_id INT,
    destination_order INT NOT NULL,
    destination_locate TEXT,
    PRIMARY KEY (destination_id, saved_route_id),
    FOREIGN KEY (info_id) REFERENCES locate_info(info_id),
    FOREIGN KEY (saved_route_id) REFERENCES saved_route(saved_route_id)
);

-- 게시글 카테고리 테이블
CREATE TABLE board_category (
    category_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    upper_category_id INT,
    category_detail VARCHAR(100),
    FOREIGN KEY (upper_category_id) REFERENCES board_category(category_id)
);
-- 게시글 카테고리 테이블 (계층형 구조: 광역시/도 -> 시/군/구)
-- 
-- 부모 카테고리 ID 매핑:
-- ┌─────────────────────┬────┐
-- │ 카테고리명          │ ID │
-- ├─────────────────────┼────┤
-- │ 서울특별시          │ 1  │
-- │ 부산광역시          │ 2  │
-- │ 대구광역시          │ 3  │
-- │ 인천광역시          │ 4  │
-- │ 광주광역시          │ 5  │
-- │ 대전광역시          │ 6  │
-- │ 울산광역시          │ 7  │
-- │ 경기도              │ 8  │
-- │ 강원특별자치도      │ 9  │
-- │ 충청북도            │ 10 │
-- │ 충청남도            │ 11 │
-- │ 전북특별자치도      │ 12 │
-- │ 전라남도            │ 13 │
-- │ 경상북도            │ 14 │
-- │ 경상남도            │ 15 │
-- │ 제주특별자치도      │ 16 │
-- └─────────────────────┴────┘

-- 경로 게시판 테이블
CREATE TABLE route_board (
    route_board_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    saved_route_id INT,
    self_route_id INT,
    category_id INT,
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    title VARCHAR(200),
    content TEXT,
    created_at DATETIME,
    board_status INT DEFAULT 0, -- 0: 일반, 1: 공지, 2: 인기글
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (saved_route_id) REFERENCES saved_route(saved_route_id),
    FOREIGN KEY (self_route_id) REFERENCES self_route(self_route_id),
    FOREIGN KEY (category_id) REFERENCES board_category(category_id)
);

-- 댓글 테이블
CREATE TABLE comment (
    comment_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    route_board_id INT NOT NULL,
    content TEXT,
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (route_board_id) REFERENCES route_board(route_board_id)
);

-- 게시글 즐겨찾기 테이블
CREATE TABLE board_favorite (
    favorite_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    route_board_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_board (user_id, route_board_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (route_board_id) REFERENCES route_board(route_board_id) ON DELETE CASCADE
);

-- 신고내역 테이블
CREATE TABLE report (
    report_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    target_id INT NOT NULL,
    report_type VARCHAR(50),
    report_content TEXT,
    report_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (target_id) REFERENCES route_board(route_board_id)
);

-- 신고 결과 테이블
CREATE TABLE report_result (
    result_id INT NOT NULL AUTO_INCREMENT,
    report_id INT NOT NULL,
    ban_period INT,
    result_content TEXT,
    result_date DATETIME,
    PRIMARY KEY (result_id, report_id),
    FOREIGN KEY (report_id) REFERENCES report(report_id)
);


