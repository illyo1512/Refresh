/* ===================== 게시판 카테고리 초기화 스크립트 =========================== */

/* ==================== 부모 카테고리 먼저 생성 ============================ */
/* 광역시/도 카테고리 (ID: 1~16) */

INSERT INTO board_category (category_id, upper_category_id, category_detail) VALUES
(1, NULL, '서울특별시'),
(2, NULL, '부산광역시'),
(3, NULL, '대구광역시'),
(4, NULL, '인천광역시'),
(5, NULL, '광주광역시'),
(6, NULL, '대전광역시'),
(7, NULL, '울산광역시'),
(8, NULL, '경기도'),
(9, NULL, '강원특별자치도'),
(10, NULL, '충청북도'),
(11, NULL, '충청남도'),
(12, NULL, '전북특별자치도'),
(13, NULL, '전라남도'),
(14, NULL, '경상북도'),
(15, NULL, '경상남도'),
(16, NULL, '제주특별자치도');

/* ==================== 하위 카테고리 생성 ============================= */

/* ===================== 서울특별시 하위 구 ================= */
SET @seoul_id = 1;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@seoul_id, '종로구'), (@seoul_id, '중구'), (@seoul_id, '용산구'), (@seoul_id, '성동구'),
(@seoul_id, '광진구'), (@seoul_id, '동대문구'), (@seoul_id, '중랑구'), (@seoul_id, '성북구'),
(@seoul_id, '강북구'), (@seoul_id, '도봉구'), (@seoul_id, '노원구'), (@seoul_id, '은평구'),
(@seoul_id, '서대문구'), (@seoul_id, '마포구'), (@seoul_id, '양천구'), (@seoul_id, '강서구'),
(@seoul_id, '구로구'), (@seoul_id, '금천구'), (@seoul_id, '영등포구'), (@seoul_id, '동작구'),
(@seoul_id, '관악구'), (@seoul_id, '서초구'), (@seoul_id, '강남구'), (@seoul_id, '송파구'),
(@seoul_id, '강동구');

/* ===================== 부산광역시 하위 구 ================= */
SET @busan_id = 2;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@busan_id, '중구'), (@busan_id, '서구'), (@busan_id, '동구'), (@busan_id, '영도구'),
(@busan_id, '부산진구'), (@busan_id, '동래구'), (@busan_id, '남구'), (@busan_id, '북구'),
(@busan_id, '해운대구'), (@busan_id, '사하구'), (@busan_id, '금정구'), (@busan_id, '강서구'),
(@busan_id, '연제구'), (@busan_id, '수영구'), (@busan_id, '사상구');

/* ===================== 대구광역시 하위 구 ================= */
SET @daegu_id = 3;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@daegu_id, '중구'), (@daegu_id, '동구'), (@daegu_id, '서구'),
(@daegu_id, '남구'), (@daegu_id, '북구'), (@daegu_id, '수성구'),
(@daegu_id, '달서구');

/* ===================== 인천광역시 하위 구 ================= */
SET @incheon_id = 4;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@incheon_id, '중구'), (@incheon_id, '동구'), (@incheon_id, '미추홀구'),
(@incheon_id, '연수구'), (@incheon_id, '남동구'), (@incheon_id, '부평구'),
(@incheon_id, '계양구'), (@incheon_id, '서구');

/* ===================== 광주광역시 하위 구 ================= */
SET @gwangju_id = 5;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@gwangju_id, '동구'), (@gwangju_id, '서구'),
(@gwangju_id, '남구'), (@gwangju_id, '북구'),
(@gwangju_id, '광산구');

/* ===================== 대전광역시 하위 구 ================= */
SET @daejeon_id = 6;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@daejeon_id, '동구'), (@daejeon_id, '중구'),
(@daejeon_id, '서구'), (@daejeon_id, '유성구'),
(@daejeon_id, '대덕구');

/* ===================== 울산광역시 하위 구 ================= */
SET @ulsan_id = 7;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@ulsan_id, '중구'), (@ulsan_id, '남구'),
(@ulsan_id, '동구'), (@ulsan_id, '북구');

/* ===================== 경기도 하위 시/군 ==================== */
SET @gyeonggi_id = 8;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@gyeonggi_id,'수원시'),(@gyeonggi_id,'고양시'),(@gyeonggi_id,'용인시'),(@gyeonggi_id,'성남시'),
(@gyeonggi_id,'부천시'),(@gyeonggi_id,'안산시'),(@gyeonggi_id,'안양시'),(@gyeonggi_id,'남양주시'),
(@gyeonggi_id,'화성시'),(@gyeonggi_id,'평택시'),(@gyeonggi_id,'시흥시'),(@gyeonggi_id,'파주시'),
(@gyeonggi_id,'의정부시'),(@gyeonggi_id,'김포시'),(@gyeonggi_id,'광주시'),(@gyeonggi_id,'하남시'),
(@gyeonggi_id,'광명시'),(@gyeonggi_id,'군포시'),(@gyeonggi_id,'오산시'),(@gyeonggi_id,'이천시'),
(@gyeonggi_id,'안성시'),(@gyeonggi_id,'의왕시'),(@gyeonggi_id,'양주시'),(@gyeonggi_id,'구리시'),
(@gyeonggi_id,'포천시'),(@gyeonggi_id,'여주시'),(@gyeonggi_id,'동두천시'),(@gyeonggi_id,'과천시'),
(@gyeonggi_id,'가평군'),(@gyeonggi_id,'양평군'),(@gyeonggi_id,'연천군');

/* ===================== 강원특별자치도 하위 시/군 ============= */
SET @gangwon_id = 9;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@gangwon_id,'춘천시'),(@gangwon_id,'원주시'),(@gangwon_id,'강릉시'),(@gangwon_id,'동해시'),
(@gangwon_id,'태백시'),(@gangwon_id,'속초시'),(@gangwon_id,'삼척시'),
(@gangwon_id,'홍천군'),(@gangwon_id,'철원군'),(@gangwon_id,'횡성군'),(@gangwon_id,'평창군'),
(@gangwon_id,'영월군'),(@gangwon_id,'정선군'),(@gangwon_id,'인제군'),(@gangwon_id,'고성군'),
(@gangwon_id,'양양군'),(@gangwon_id,'화천군'),(@gangwon_id,'양구군');

/* ===================== 충청북도 하위 시/군 ================== */
SET @chungbuk_id = 10;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@chungbuk_id,'청주시'),(@chungbuk_id,'충주시'),(@chungbuk_id,'제천시'),
(@chungbuk_id,'보은군'),(@chungbuk_id,'옥천군'),(@chungbuk_id,'영동군'),(@chungbuk_id,'증평군'),
(@chungbuk_id,'진천군'),(@chungbuk_id,'괴산군'),(@chungbuk_id,'음성군'),(@chungbuk_id,'단양군');

/* ===================== 충청남도 하위 시/군 ================== */
SET @chungnam_id = 11;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@chungnam_id,'천안시'),(@chungnam_id,'공주시'),(@chungnam_id,'보령시'),(@chungnam_id,'아산시'),
(@chungnam_id,'서산시'),(@chungnam_id,'논산시'),(@chungnam_id,'계룡시'),(@chungnam_id,'당진시'),
(@chungnam_id,'금산군'),(@chungnam_id,'부여군'),(@chungnam_id,'서천군'),(@chungnam_id,'청양군'),
(@chungnam_id,'홍성군'),(@chungnam_id,'예산군'),(@chungnam_id,'태안군');

/* ===================== 전북특별자치도 하위 시/군 ============ */
SET @jeonbuk_id = 12;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@jeonbuk_id,'전주시'),(@jeonbuk_id,'군산시'),(@jeonbuk_id,'익산시'),(@jeonbuk_id,'정읍시'),
(@jeonbuk_id,'남원시'),(@jeonbuk_id,'김제시'),
(@jeonbuk_id,'완주군'),(@jeonbuk_id,'진안군'),(@jeonbuk_id,'무주군'),(@jeonbuk_id,'장수군'),
(@jeonbuk_id,'임실군'),(@jeonbuk_id,'순창군'),(@jeonbuk_id,'고창군'),(@jeonbuk_id,'부안군');

/* ===================== 전라남도 하위 시/군 ================== */
SET @jeonnam_id = 13;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@jeonnam_id,'목포시'),(@jeonnam_id,'여수시'),(@jeonnam_id,'순천시'),(@jeonnam_id,'나주시'),
(@jeonnam_id,'광양시'),
(@jeonnam_id,'담양군'),(@jeonnam_id,'곡성군'),(@jeonnam_id,'구례군'),(@jeonnam_id,'고흥군'),
(@jeonnam_id,'보성군'),(@jeonnam_id,'화순군'),(@jeonnam_id,'장흥군'),(@jeonnam_id,'강진군'),
(@jeonnam_id,'해남군'),(@jeonnam_id,'영암군'),(@jeonnam_id,'무안군'),(@jeonnam_id,'함평군'),
(@jeonnam_id,'영광군'),(@jeonnam_id,'장성군'),(@jeonnam_id,'완도군'),(@jeonnam_id,'진도군'),
(@jeonnam_id,'신안군');

/* ===================== 경상북도 하위 시/군 ================== */
SET @gyeongbuk_id = 14;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@gyeongbuk_id,'포항시'),(@gyeongbuk_id,'경주시'),(@gyeongbuk_id,'김천시'),(@gyeongbuk_id,'안동시'),
(@gyeongbuk_id,'구미시'),(@gyeongbuk_id,'영주시'),(@gyeongbuk_id,'영천시'),(@gyeongbuk_id,'상주시'),
(@gyeongbuk_id,'문경시'),(@gyeongbuk_id,'경산시'),
(@gyeongbuk_id,'의성군'),(@gyeongbuk_id,'청송군'),(@gyeongbuk_id,'영양군'),
(@gyeongbuk_id,'영덕군'),(@gyeongbuk_id,'청도군'),(@gyeongbuk_id,'고령군'),
(@gyeongbuk_id,'성주군'),(@gyeongbuk_id,'칠곡군'),(@gyeongbuk_id,'예천군'),
(@gyeongbuk_id,'봉화군'),(@gyeongbuk_id,'울진군'),(@gyeongbuk_id,'울릉군');

/* ===================== 경상남도 하위 시/군 ================== */
SET @gyeongnam_id = 15;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@gyeongnam_id,'창원시'),(@gyeongnam_id,'진주시'),(@gyeongnam_id,'통영시'),(@gyeongnam_id,'사천시'),
(@gyeongnam_id,'김해시'),(@gyeongnam_id,'밀양시'),(@gyeongnam_id,'거제시'),(@gyeongnam_id,'양산시'),
(@gyeongnam_id,'의령군'),(@gyeongnam_id,'함안군'),(@gyeongnam_id,'창녕군'),(@gyeongnam_id,'고성군'),
(@gyeongnam_id,'남해군'),(@gyeongnam_id,'하동군'),(@gyeongnam_id,'산청군'),
(@gyeongnam_id,'함양군'),(@gyeongnam_id,'거창군'),(@gyeongnam_id,'합천군');

/* ===================== 제주특별자치도 하위 시 ============ */
SET @jeju_id = 16;
INSERT INTO board_category (upper_category_id, category_detail) VALUES
(@jeju_id,'제주시'),(@jeju_id,'서귀포시');