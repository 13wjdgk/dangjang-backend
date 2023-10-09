package com.coniverse.dangjang.domain.healthmetric.service;

import static com.coniverse.dangjang.fixture.HealthMetricFixture.*;
import static com.coniverse.dangjang.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.coniverse.dangjang.domain.analysis.service.AnalysisService;
import com.coniverse.dangjang.domain.code.enums.CommonCode;
import com.coniverse.dangjang.domain.guide.common.service.GuideService;
import com.coniverse.dangjang.domain.healthmetric.dto.request.HealthConnectPostRequest;
import com.coniverse.dangjang.domain.healthmetric.dto.request.HealthConnectRegisterRequest;
import com.coniverse.dangjang.domain.healthmetric.repository.HealthMetricRepository;
import com.coniverse.dangjang.domain.point.service.PointService;
import com.coniverse.dangjang.domain.user.entity.User;
import com.coniverse.dangjang.domain.user.repository.UserRepository;
import com.coniverse.dangjang.domain.user.service.UserSearchService;

/**
 * @author TEO, EVE
 * @since 1.0.0
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthConnectServiceTest {
	@Autowired
	private HealthConnectService healthConnectService;
	@Autowired
	private HealthMetricRepository healthMetricRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserSearchService userSearchService;
	@MockBean
	private GuideService guideService;
	@MockBean
	private AnalysisService analysisService;
	@MockBean
	private PointService pointService;

	@AfterEach
	void tearDown() {
		healthMetricRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	void 헬스_커넥트_데이터_전체를_성공적으로_저장한다() {
		// given
		User user = userRepository.save(유저_테오());
		String oauthId = user.getOauthId();
		String type = CommonCode.BEFORE_BREAKFAST.getTitle();
		int count = 30;
		HealthConnectPostRequest request = 헬스_커넥트_데이터_등록_요청(type, count);

		// when
		healthConnectService.registerHealthConnectData(request, oauthId);

		// then
		assertThat(healthMetricRepository.findAll()).hasSize(count);
	}

	@Test
	void 헬스_커넥트_데이터를_저장할_때_중복되는_데이터는_건너뛰고_저장한다() {
		// given
		User user = userRepository.save(유저_테오());
		String oauthId = user.getOauthId();
		String type = CommonCode.BEFORE_BREAKFAST.getTitle();
		int prevCount = 30;
		HealthConnectPostRequest prevRequest = 헬스_커넥트_데이터_등록_요청(type, prevCount);
		healthConnectService.registerHealthConnectData(prevRequest, oauthId);

		// when
		int count = 50;
		HealthConnectPostRequest request = 헬스_커넥트_데이터_등록_요청(type, count);
		healthConnectService.registerHealthConnectData(request, oauthId);

		// then
		assertThat(healthMetricRepository.findAll()).hasSize(count);
	}

	@Test
	void 헬스_커넥트를_연동한다() {
		// given
		User user = userRepository.save(유저_테오());
		String oauthId = user.getOauthId();
		HealthConnectRegisterRequest request = 헬스_커넥트_연동_요청(true);

		// when
		healthConnectService.interlockHealthConnect(request, oauthId);

		// then
		User 연동된_유저 = userSearchService.findUserByOauthId(oauthId);
		assertTrue(연동된_유저.isConnectingHealthConnect());
	}

	@Test
	void 헬스_커넥트_연동을_취소한다() {
		// given
		User user = userRepository.save(헬스커넥트_연동_유저());
		String oauthId = user.getOauthId();
		HealthConnectRegisterRequest request = 헬스_커넥트_연동_요청(false);

		// when
		healthConnectService.interlockHealthConnect(request, oauthId);

		// then
		User 연동된_유저 = userSearchService.findUserByOauthId(oauthId);
		assertTrue(연동된_유저.isDisconnectedHealthConnect());
	}
}
