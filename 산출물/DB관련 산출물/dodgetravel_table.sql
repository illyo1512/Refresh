-- DROP DATABASE capstone;
create database dodge_travel;
use dodge_travel;

-- 사용자 테이블
CREATE TABLE user (
    user_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100),
    nickname VARCHAR(50),
    gender VARCHAR(10),
    role VARCHAR(20),
    phone_number VARCHAR(20),
    password VARCHAR(255),
    user_image VARCHAR(255),
    user_ban BOOL DEFAULT FALSE,
    created_at DATETIME
);

-- 설정 테이블
CREATE TABLE setting (
    setting_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    night_mod BOOL,
    color_blind BOOL,
    alert_level INT,
    PRIMARY KEY (setting_id, user_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 개인 메시지 테이블
CREATE TABLE dm (
    dm_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    content TEXT,
    sender VARCHAR(50),
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
    danger_detail VARCHAR(200),
    danger_Countermeasure VARCHAR(500)
);

-- 실시간 위험 수집 테이블
CREATE TABLE realtime_danger (
    danger_id INT PRIMARY KEY AUTO_INCREMENT,
    locate_name VARCHAR(100),
    danger_detail_id INT NOT NULL,
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
    danger_location VARCHAR(200),
    danger_radius INT,
    danger_level INT,
    FOREIGN KEY (detail_id) REFERENCES danger_detail(detail_id)
); 

-- 자신 경로 목적지 테이블
CREATE TABLE destination_self (
    destination_id INT AUTO_INCREMENT,
    self_route_id INT NOT NULL,
    info_id INT NOT NULL,
    destination_locate VARCHAR(300),
    destination_order INT NOT NULL,
    PRIMARY KEY (destination_id, self_route_id),
    FOREIGN KEY (info_id) REFERENCES locate_info(info_id),
    FOREIGN KEY (self_route_id) REFERENCES self_route(self_route_id)
);

-- 즐겨찾기 경로 목적지 테이블
CREATE TABLE destination_save (
    destination_id INT AUTO_INCREMENT,
    saved_route_id INT NOT NULL,
    info_id INT NOT NULL,
    destination_locate VARCHAR(300),
    destination_order INT NOT NULL,
    PRIMARY KEY (destination_id, saved_route_id),
    FOREIGN KEY (info_id) REFERENCES locate_info(info_id),
    FOREIGN KEY (saved_route_id) REFERENCES saved_route(saved_route_id)
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

-- 게시글 카테고리 테이블
CREATE TABLE board_category (
	category_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    category_detail VARCHAR(100)
);

-- 경로 게시판 테이블
CREATE TABLE route_board (
    route_board_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
	saved_route_id INT,
	self_route_id INT,
    title VARCHAR(200),
    content TEXT,
    category_id INT,
    created_at DATETIME,
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

-- 신고내역 테이블
CREATE TABLE report (
    report_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    target_id INT NOT NULL,
    report_content TEXT,
    report_type VARCHAR(50),
    report_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (target_id) REFERENCES route_board(route_board_id)
);

-- 신고 결과 테이블
CREATE TABLE report_result (
    result_id INT NOT NULL AUTO_INCREMENT,
    report_id INT NOT NULL,
    result_content TEXT,
    ban_period INT,
    result_date DATETIME,
    FOREIGN KEY (report_id) REFERENCES report(report_id),
    PRIMARY KEY (result_id, report_id)
);


