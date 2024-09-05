package com.coniverse.dangjang.domain.point.service;

import static com.coniverse.dangjang.fixture.HealthMetricFixture.*;
import static com.coniverse.dangjang.fixture.PointFixture.*;
import static com.coniverse.dangjang.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.coniverse.dangjang.domain.code.enums.CommonCode;
import com.coniverse.dangjang.domain.code.enums.GroupCode;
import com.coniverse.dangjang.domain.healthmetric.dto.request.HealthConnectRegisterRequest;
import com.coniverse.dangjang.domain.healthmetric.dto.request.HealthMetricPostRequest;
import com.coniverse.dangjang.domain.healthmetric.repository.HealthMetricRepository;
import com.coniverse.dangjang.domain.healthmetric.service.HealthConnectService;
import com.coniverse.dangjang.domain.healthmetric.service.HealthMetricRegisterService;
import com.coniverse.dangjang.domain.point.dto.request.UsePointRequest;
import com.coniverse.dangjang.domain.point.dto.response.ProductListResponse;
import com.coniverse.dangjang.domain.point.enums.EarnPoint;
import com.coniverse.dangjang.domain.point.enums.PointType;
import com.coniverse.dangjang.domain.point.exception.InvalidPointException;
import com.coniverse.dangjang.domain.point.repository.PointHistoryRepository;
import com.coniverse.dangjang.domain.point.repository.PointProductRepository;
import com.coniverse.dangjang.domain.point.repository.PurchaseHistoryRepository;
import com.coniverse.dangjang.domain.user.entity.User;
import com.coniverse.dangjang.domain.user.repository.UserRepository;

/**
 * 포인트 Service 테스트
 *
 * @author EVE
 * @since 1.0.0
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PointHistoryServiceTest {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PointService pointService;
	@Autowired
	private PointProductRepository pointProductRepository;
	@Autowired
	private PointHistoryRepository pointHistoryRepository;
	@Autowired
	private PointSearchService pointSearchService;
	@Autowired
	private HealthConnectService healthConnectService;
	@Autowired
	private PurchaseHistoryRepository purchaseHistoryRepository;
	@Autowired
	private HealthMetricRegisterService healthMetricRegisterService;
	@Autowired
	private HealthMetricRepository healthMetricRepository;

	private User 유저;
	private LocalDate today = LocalDate.now();

	@BeforeAll
	void setUp() {
		pointProductRepository.saveAll(전체_포인트_상품_목록());
	}

	@AfterEach
	void tearDown() {
		pointHistoryRepository.deleteAll();
		purchaseHistoryRepository.deleteAll();
		userRepository.deleteAll();

	}

	@Order(100)
	@Test
	void 회원가입_포인트_적립을_받는다() {
		//given
		유저 = userRepository.save(포인트_유저(today));
		int accessPoint = EarnPoint.REGISTER.getChangePoint();

		//when
		pointService.addSignupPoint(유저.getOauthId());

		//then
		// TODO : 유저포인트 조회 테스트
	}

	@Order(200)
	@Test
	void 하루에_한번_접속하면_포인트_적립을_받는다() throws InterruptedException {
		//given
		유저 = userRepository.save(포인트_유저(today.minusDays(1)));
		// TODO : 유저포인트 조회 테스트
	}

	@Order(300)
	@Test
	@Transactional
	void Health_Connect_연동_포인트_적립을_받는다() {
		//given
		유저 = userRepository.save(헬스커넥트_연동_유저());

		//when
		pointService.addHealthConnectPoint(유저);
		//then
		User 접속한_유저 = userRepository.findById(유저.getOauthId()).get();

		// TODO : 유저포인트 조회 테스트
	}

	@Order(330)
	@Test
	@Transactional
	void 체중_등록시_포인트_적립을_받는다() {

		//given
		유저 = userRepository.save(헬스커넥트_연동_유저());

		HealthMetricPostRequest request = 체중_건강지표_등록_요청();

		// when
		healthMetricRegisterService.register(request, 유저.getOauthId());

		// TODO : 유저포인트 조회 테스트
	}

	@Order(360)
	@Test
	@Transactional
	void 운동_등록시_포인트_적립을_받는다() {
		//given
		유저 = userRepository.save(헬스커넥트_연동_유저());

		healthMetricRepository.save(건강지표_엔티티(유저, CommonCode.MEASUREMENT, today));

		// TODO : 유저포인트 조회 테스트

		// when

		int 등록후_포인트 = 0;

		//then
		// TODO : 유저포인트 조회 테스트
	}

	@Order(380)
	@Test
	@Transactional
	void 혈당_등록시_포인트_적립을_받는다() {
		//given
		유저 = userRepository.save(헬스커넥트_연동_유저());

		// TODO : 유저포인트 조회 테스트
		// when

		int 등록후_포인트 = 0;

		//then
		// TODO : 유저포인트 조회 테스트
	}

	@Order(390)
	@Test
	@Transactional
	void 체중_혈당_운동_기록하지_않았으면_포인트를_등록하지_않는다() {
		//given
		유저 = userRepository.save(헬스커넥트_연동_유저());

		// when
		pointService.addHealthMetricPoint(유저.getOauthId(), today, GroupCode.BLOOD_SUGAR);
		pointService.addHealthMetricPoint(유저.getOauthId(), today, GroupCode.EXERCISE);
		pointService.addHealthMetricPoint(유저.getOauthId(), today, GroupCode.WEIGHT);

		//then
		// TODO : 유저포인트 조회 테스트
	}

	@Order(400)
	@Test
	void 구매중_포인트가_부족하면_에러를_발생한다() {
		//given
		유저 = userRepository.save(포인트_유저(today));

		UsePointRequest request = 포인트_사용_요청(유저, "다이소 오천원 금액권");
		//when&then
		assertThatThrownBy(() -> {
			pointService.purchaseProduct(유저.getOauthId(), request);
		}).isInstanceOf(InvalidPointException.class);
	}

	@Order(500)
	@ParameterizedTest
	@ValueSource(strings = {"스타벅스 오천원 금액권", "CU 오천원 금액권", "네이버페이 오천원 금액권"})
	void 포인트_상품을_구매한다(String product) {
		//given
		유저 = userRepository.save(포인트_유저(today));

		UsePointRequest request = 포인트_사용_요청(유저, "다이소 오천원 금액권");

		//when
		pointService.purchaseProduct(유저.getOauthId(), request);

		//then
		User 접속한_유저 = userRepository.findById(유저.getOauthId()).get();

		int 구매_내역_횟수 = purchaseHistoryRepository.findAll().size();

		assertThat(구매_내역_횟수).isEqualTo(1);
		purchaseHistoryRepository.deleteAll();
	}

	@Order(550)
	@Test
	void 존재하지_않는_포인트_상품을_구매하면_예외를_던진다() {
		//given
		유저 = userRepository.save(포인트_유저(today));
		// TODO : 유저포인트 조회 테스트

		//when

		// TODO : 유저포인트 조회 테스트
	}

	@Order(600)
	@Test
	void 포인트_상품을_조회한다() {
		//given
		유저 = userRepository.save(포인트_유저(today));

		int earnTypeProductSize = pointSearchService.findAllByType(PointType.EARN).size();
		//when
		ProductListResponse response = pointService.getProducts(유저.getOauthId());
		//then
		assertThat(response.descriptionListToEarnPoint()).hasSize(earnTypeProductSize);
	}

	@Order(700)
	@Test
	void 동시_상품구매_요청_한번만_수행한다() throws InterruptedException {
		//given
		유저 = userRepository.save(포인트_유저(today));

		UsePointRequest request = 포인트_사용_요청(유저, "다이소 오천원 금액권");
		ExecutorService executorService = Executors.newFixedThreadPool(30);
		AtomicInteger successCount = new AtomicInteger();
		AtomicInteger failedCount = new AtomicInteger();
		CountDownLatch latch = new CountDownLatch(30);

		for (int i = 0; i < 100; i++) {
			executorService.submit(() -> {
				try {
					pointService.purchaseProduct(유저.getOauthId(), request);
					successCount.incrementAndGet();
				} catch (Exception e) {
					failedCount.incrementAndGet();
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();
		//then
		User 접속한_유저 = userRepository.findById(유저.getOauthId()).get();
		System.out.println("point log start =====================");
		System.out.println("success count : " + successCount.get());
		System.out.println("failed count : " + failedCount.get());
		int 포인트_로그_내역_횟수 = pointHistoryRepository.findAll().size();
		int 구매_내역_횟수 = purchaseHistoryRepository.findAll().size();
		System.out.println("point log finish =====================");

		// TODO : 유저포인트 조회 테스트
		assertThat(구매_내역_횟수).isOne();
		assertThat(포인트_로그_내역_횟수).isOne();

	}

	@Order(900)
	@Test
	void 동시_헬스커넥트_연결요청_포인트를_한번만_얻는다() throws InterruptedException {
		//given
		유저 = userRepository.save(포인트_유저(today));

		HealthConnectRegisterRequest request = 헬스_커넥트_연동_요청(true);
		// when
		ExecutorService executorService = Executors.newFixedThreadPool(30);
		AtomicInteger successCount = new AtomicInteger();
		AtomicInteger failedCount = new AtomicInteger();
		CountDownLatch latch = new CountDownLatch(30);
		for (int i = 0; i < 30; i++) {
			executorService.submit(() -> {
				try {
					healthConnectService.interlockHealthConnect(request, 유저.getOauthId());
					successCount.incrementAndGet();
				} catch (Exception e) {
					System.out.println("error : " + e.getMessage());
					failedCount.incrementAndGet();
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		//then
		User 연동된_유저 = userRepository.findById(유저.getOauthId()).get();
		System.out.println("point log start =====================");
		System.out.println("success count : " + successCount.get());
		System.out.println("failed count : " + failedCount.get());
		System.out.println("point log finish =====================");
		int 포인트_로그_내역_횟수 = pointHistoryRepository.findAll().size();
		// TODO : 유저포인트 조회 테스트

		assertThat(포인트_로그_내역_횟수).isOne();
	}

}
