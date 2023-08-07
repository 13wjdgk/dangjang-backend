INSERT INTO users (oauth_id, oauth_provider, nickname, gender, birthday, activity_amount, height, recommended_calorie, status, role, profile_image_path,
                   diabetes, created_at, updated_at)
VALUES ('11111111', 'KAKAO', 'TEO', 'M', '1997-05-23', 'MEDIUM', 180, 2000, 'ACTIVE', 'ROLE_ADMIN', 'user/image/teo', false, now(), now());

INSERT INTO users (oauth_id, oauth_provider, nickname, gender, birthday, activity_amount, height, recommended_calorie, status, role, profile_image_path,
                   diabetes, diabetes_year, medicine, injection, created_at, updated_at)
VALUES ('22222222', 'NAVER', 'EVE', 'F', '2001-12-24', 'LOW', 160, 1500, 'ACTIVE', 'ROLE_USER', 'user/image/eve', true, 3, true, true, now(), now());
