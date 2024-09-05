package com.coniverse.dangjang.domain.point.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.coniverse.dangjang.domain.auth.service.DefaultOauthLoginService;
import com.coniverse.dangjang.domain.code.enums.GroupCode;
import com.coniverse.dangjang.domain.healthmetric.service.HealthMetricSearchService;
import com.coniverse.dangjang.domain.point.dto.request.UsePointRequest;
import com.coniverse.dangjang.domain.point.dto.response.ProductListResponse;
import com.coniverse.dangjang.domain.point.dto.response.UsePointResponse;
import com.coniverse.dangjang.domain.point.dto.response.UserPointResponse;
import com.coniverse.dangjang.domain.point.entity.PointHistory;
import com.coniverse.dangjang.domain.point.entity.PointProduct;
import com.coniverse.dangjang.domain.point.entity.PurchaseHistory;
import com.coniverse.dangjang.domain.point.enums.EarnPoint;
import com.coniverse.dangjang.domain.point.enums.PointType;
import com.coniverse.dangjang.domain.point.exception.InvalidPointException;
import com.coniverse.dangjang.domain.point.mapper.PointMapper;
import com.coniverse.dangjang.domain.point.repository.PointHistoryRepository;
import com.coniverse.dangjang.domain.point.repository.PurchaseHistoryRepository;
import com.coniverse.dangjang.domain.user.entity.User;
import com.coniverse.dangjang.domain.user.service.UserSearchService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 포인트 관련 서비스
 *
 * @author EVE
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PointService {
	private final PointHistoryRepository pointHistoryRepository;
	private final PointMapper pointMapper;
	private final UserSearchService userSearchService;
	private final PointSearchService pointSearchService;
	private final DefaultOauthLoginService defaultOauthLoginService;

	private final PurchaseHistoryRepository purchaseHistoryRepository;
	private final HealthMetricSearchService healthMetricSearchService;

	/**
	 * 체중,혈당,체중 등록시, 포인트 적립
	 *
	 * @param oauthId   유저 아이디
	 * @param date      LocalDate 저장 날짜
	 * @param groupCode 그룹 코드
	 * @since 1.3.0
	 */
	public void addHealthMetricPoint(String oauthId, LocalDate date, GroupCode groupCode) {
		User user = userSearchService.findUserByOauthId(oauthId);
		int healthMetricCount = healthMetricSearchService.findByGroupCode(oauthId, groupCode, date);
		if (healthMetricCount == 1) {
			addPointEvent(EarnPoint.findByGroupCode(groupCode).getTitle(), user);
		}
	}

	/**
	 * 1일 1접속 포인트 적립
	 * <p>
	 * 1일 1접속 가능 유저인지 자격을 확인하고,
	 * 가능한 유저에게 포인트를 적립한다.
	 *
	 * @param oauthId 유저 아이디
	 * @since 1.0.0
	 */
	@Async
	public void addAccessPoint(String oauthId) {
		User user = userSearchService.findUserByOauthId(oauthId);
		// TODO : 1일 1접속 포인트 적립 로직 수정

	}

	/**
	 * 회원가입 포인트 적립
	 * <p>
	 * 회원가입 유저인지 자격을 확인하고, 포인트를 적립한다.
	 *
	 * @param oauthId 유저 아이디
	 * @since 1.0.0
	 */
	public void addSignupPoint(String oauthId) {
		User user = userSearchService.findUserByOauthId(oauthId);
		if (user.getCreatedAt().toLocalDate().equals(LocalDate.now())) {
			addPointEvent(EarnPoint.REGISTER.getTitle(), user);
		}
	}

	/**
	 * 헬스커넥트 포인트 적립
	 * <p>
	 * 헬스커넥트 연동 유저인지 자격을 확인하고, 포인트를 적립한다.
	 *
	 * @param user 유저
	 * @since 1.0.0
	 */
	public void addHealthConnectPoint(User user) {
		addPointEvent(EarnPoint.HEALTH_CONNECT.getTitle(), user);
	}

	/**
	 * 포인트 상품 구매
	 *
	 * @param oauthId 유저 아이디
	 * @param request 포인트 사용 요청 객체
	 * @since 1.0.0
	 */
	public UsePointResponse purchaseProduct(String oauthId, UsePointRequest request) {
		User user = userSearchService.findUserByOauthId(oauthId);
		PointHistory savedPointHistory = addPointEvent(request.type(), user);
		PurchaseHistory purchase = purchaseHistoryRepository.save(pointMapper.toEntity(user, savedPointHistory.getPointProduct(), request));
		int balancePoint = pointSearchService.findUserPoint(user.getOauthId()).getTotalPoint();
		return new UsePointResponse(purchase.getPhone(), purchase.getPointProduct().getProductName(), savedPointHistory.getChangePoint(),
			balancePoint, purchase.getName(), purchase.getComment());
	}

	/**
	 * 포인트 이벤트 추가
	 * <p>
	 * 포인트 적립 및 사용 이벤트가 발생했을 때,
	 * 포인트 이벤트를 추가한다.
	 *
	 * @param user        유저
	 * @param productName 포인트 상품
	 * @since 1.0.0
	 */
	private PointHistory addPointEvent(String productName, User user) {
		PointProduct product = pointSearchService.findPointProductById(productName);
		int changePoint = getChangePoint(product);
		UserPointResponse userPoint = pointSearchService.findUserPoint(user.getOauthId());
		int balancePoint = getBalancePoint(changePoint, userPoint.getTotalPoint());
		PointHistory savedPointHistory = pointHistoryRepository.save(pointMapper.toEntity(product, user, changePoint));
		return savedPointHistory;
	}

	/**
	 * 구매 포인트 상품에 따른 포인트 변동량 계산
	 *
	 * @param product 포인트 상품
	 * @since 1.0.0
	 */
	private int getChangePoint(PointProduct product) {
		if (product.getType().equals(PointType.USE)) {
			return -product.getPoint();
		}
		return product.getPoint();
	}

	/**
	 * 최종 point 계산
	 * <p>
	 * 만약 최종 계산한 point가 0보다 적다면 오류를 반환한다.
	 *
	 * @param changePoint  포인트 변동량
	 * @param balancePoint 기존 포인트
	 * @throws InvalidPointException 포인트 부족시 발생
	 * @since 1.0.0
	 */
	private int getBalancePoint(int changePoint, int balancePoint) {
		balancePoint += changePoint;
		if (balancePoint >= 0) {
			return balancePoint;
		}
		throw new InvalidPointException("포인트가 부족합니다.");
	}

	/**
	 * 구매 가능 상품 조회
	 * <p>
	 * 유저의 현재 포인트와 구매 가능한 상품을 조회한다.
	 * 11월 10일 이후 포인트 서비스를 종료한다.
	 *
	 * @param oauthId 유저 아이디
	 * @since 1.0.0
	 */
	public ProductListResponse getProducts(String oauthId) {
		int balancePoint = pointSearchService.findUserPoint(oauthId).getTotalPoint();
		List<PointProduct> productList = new ArrayList<>();
		List<String> descriptionListToEarnPoint = pointSearchService.findAllByType(PointType.EARN).stream()
			.map(PointProduct::getDescription)
			.collect(Collectors.toList());
		return new ProductListResponse(balancePoint, productList, descriptionListToEarnPoint);
	}

	/**
	 * 모든 유저의 현재 포인트 조회
	 * <p>
	 * 모든 유저의 현재 포인트를 조회한다.
	 *
	 * @return List<UserPointResponse> 모든 유저별 포인트 조회 응답
	 * @since 1.6.0
	 */
	public List<UserPointResponse> getAllUserPoint(int page) {
		int limit = 100;
		int offset = (page - 1) * limit;
		return pointSearchService.findAllUserPoint(limit, offset);

	}
}
