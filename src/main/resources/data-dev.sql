INSERT INTO users (oauth_id, oauth_provider, nickname, gender, birthday, activity_amount, height, recommended_calorie, status, role, profile_image_path,
                   diabetic, diabetes_year, medicine, injection, created_at, updated_at, health_connect)
VALUES ('11111111', 'KAKAO', 'TEO', 'M', '1997-05-23', 'MEDIUM', 180, 2000, 'ACTIVE', 'ADMIN', 'user/image/teo', false, 0, false, false, '2024-07-21 10:18:27',
        '2024-07-21 10:18:27', 'NEVER_CONNECTED');

INSERT INTO users (oauth_id, oauth_provider, nickname, gender, birthday, activity_amount, height, recommended_calorie, status, role, profile_image_path,
                   diabetic, diabetes_year, medicine, injection, created_at, updated_at, health_connect)
VALUES ('22222222', 'NAVER', 'EVE', 'F', '2001-12-24', 'LOW', 160, 1500, 'ACTIVE', 'USER', 'user/image/eve', true, 3, true, true, '2024-07-21 10:18:27',
        '2024-07-21 10:18:27',
        'NEVER_CONNECTED');
INSERT INTO users (oauth_id, oauth_provider, nickname, gender, birthday, activity_amount, height, recommended_calorie, status, role, profile_image_path,
                   diabetic, diabetes_year, medicine, injection, created_at, updated_at, health_connect)
VALUES ('33333333', 'NAVER', 'user3', 'F', '2001-12-24', 'LOW', 160, 1500, 'ACTIVE', 'USER', 'user/image/eve', true, 3, true, true, '2024-07-21 10:18:27',
        '2024-07-21 10:18:27',
        'NEVER_CONNECTED');
INSERT INTO users (oauth_id, oauth_provider, nickname, gender, birthday, activity_amount, height, recommended_calorie, status, role, profile_image_path,
                   diabetic, diabetes_year, medicine, injection, created_at, updated_at, health_connect)
VALUES ('44444444', 'NAVER', 'user4', 'F', '2001-12-24', 'LOW', 160, 1500, 'ACTIVE', 'USER', 'user/image/eve', true, 3, true, true, '2024-07-21 10:18:27',
        '2024-07-21 10:18:27',
        'NEVER_CONNECTED');

INSERT INTO point_product (product_name, point, type, description)
VALUES ("접속", 500, 'EARN', "1일 1회 접속으로 500 포인트를 적립할 수 있어요!"),
       ("등록", 500, 'EARN', "회원가입으로 500 포인트를 적립할 수 있어요!"),
       ("기기연동", 500, 'EARN', "헬스커넥트 기기연동으로 500 포인트를 적립할 수 있어요!"),
       ("체중", 200, 'EARN', "1일 1회 체중을 기록하면 200 포인트를 적립할 수 있어요!"),
       ("운동", 200, 'EARN', "1일 1회 운동을 기록하면 200 포인트를 적립할 수 있어요!"),
       ("혈당", 300, 'EARN', "1일 1회 혈당을 기록하면 300 포인트를 적립할 수 있어요!"),
       ("스타벅스 오천원 금액권", 5000, 'USE', null),
       ("CU 오천원 금액권", 5000, 'USE', null),
       ("다이소 오천원 금액권", 5000, 'USE', null),
       ("네이버페이 오천원 금액권", 5000, 'USE', null),
       ("다이소 삼천원 금액권", 3000, 'USE', null),
       ("GS25 삼천원 금액권", 3000, 'USE', null);

INSERT INTO notification_type (type)
VALUES ("접속"),
       ("기록");

INSERT INTO user_fcm_token (oauth_id, device_id, fcm_token, created_at, updated_at)
VALUES ("11111111", "device1", "fcmToken1", '2023-10-08', '2023-10-08'),
       ("22222222", "device2", "fcmToken2", '2023-10-08', '2023-10-08'),
       ("11111111", "device3", "fcmToken3", '2023-10-09', '2023-10-09'),
       ("33333333", "device4", "fcmToken4", '2023-10-09', '2023-10-09'),
       ("33333333", "device5", "fcmToken5", '2023-10-10', '2023-10-10'),
       ("44444444", "device6", "fcmToken6", '2023-10-10', '2023-10-10');

-- INSERT INTO notification(title, content, created_at, type, oauth_id, is_read)
-- VALUES ("체중 기록!", "오늘 체중을 기록해보세요 ~ ", '2023-10-08', "기록", "11111111", true),
--        ("접속해주세요!", "오늘은 접속 안하셨네요 ~ 접속해서 포인트 받아가세요", '2023-10-09', "접속", "11111111", false),
--        ("운동 기록 !", "오늘 운동을 기록해보세요 ~ ", '2023-10-10', "기록", "11111111", false),
--        ("체중 기록!", "오늘 체중을 기록해보세요 ~ ", '2023-10-08', "기록", "22222222", true),
--        ("접속해주세요!", "오늘은 접속 안하셨네요 ~ 접속해서 포인트 받아가세요", '2023-10-09', "접속", "22222222", false),
--        ("운동 기록 !", "오늘 운동을 기록해보세요 ~ ", '2023-10-10', "기록", "11111111", false);

INSERT INTO version(created_at, min_version, latest_version)
VALUES (now(), "1.0.0", "1.0.0");