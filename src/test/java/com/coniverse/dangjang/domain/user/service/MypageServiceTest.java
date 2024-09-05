package com.coniverse.dangjang.domain.user.service;

import static com.coniverse.dangjang.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.coniverse.dangjang.domain.user.dto.response.MypageResponse;
import com.coniverse.dangjang.domain.user.entity.User;
import com.coniverse.dangjang.domain.user.repository.UserRepository;

/**
 * 마이페이지 서비스 테스트
 *
 * @author EVE
 * @since 1.0.0
 */
@SpringBootTest
class MypageServiceTest {
	@Autowired
	private MypageService mypageService;
	@Autowired
	private UserRepository userRepository;

	private User 유저;

	@Test
	void 마이페이지_정보를_불러온다() {
		// given
		유저 = userRepository.save(헬스커넥트_연동_유저());

		// when
		MypageResponse response = mypageService.getMypage(유저.getOauthId());
		// then
		assertThat(response.nickname()).isEqualTo(유저.getNickname());
		assertThat(response.point()).isEqualTo(0);

	}
}
