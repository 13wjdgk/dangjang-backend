package com.coniverse.dangjang.domain.point.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coniverse.dangjang.domain.point.dto.response.UserPointResponse;
import com.coniverse.dangjang.domain.point.entity.PointProduct;
import com.coniverse.dangjang.domain.point.enums.PointType;
import com.coniverse.dangjang.domain.point.exception.InvalidPointException;
import com.coniverse.dangjang.domain.point.repository.PointHistoryRepository;
import com.coniverse.dangjang.domain.point.repository.PointProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 포인트 관련 Search Service
 *
 * @author EVE
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PointSearchService {
	private final PointProductRepository pointProductRepository;

	private final PointHistoryRepository pointHistoryRepository;

	/**
	 * 포인트 상품 조회
	 *
	 * @param productName 포인트 상품 이름
	 * @since 1.0.0
	 */
	public PointProduct findPointProductById(String productName) {
		return pointProductRepository.findById(productName)
			.orElseThrow(() -> new InvalidPointException("포인트 상품이 없습니다."));
	}

	/**
	 * type에 따른 포인트 상품 목록 조회
	 *
	 * @param type 포인트 상품 타입
	 * @since 1.0.0
	 */
	public List<PointProduct> findAllByType(PointType type) {
		return pointProductRepository.findAllByType(type);
	}

	/**
	 * 사용자 포인트 조회
	 *
	 * @since 1.6.0
	 */
	public UserPointResponse findUserPoint(String oauthId) {
		UserPointResponse userPoint = pointHistoryRepository.findUserPoint(oauthId).orElse(new UserPointResponse(oauthId, 0));
		return userPoint;
	}

	/**
	 * 모든 사용자 포인트 조회
	 *
	 * @since 1.6.0
	 */
	public List<UserPointResponse> findAllUserPoint(int limit, int offset) {
		long startTime = System.currentTimeMillis();
		log.info("checkTime test DB - start : " + startTime);
		List<UserPointResponse> allUserPoint = pointHistoryRepository.findAllUserPoint(limit, offset);
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime); // 밀리초 단위
		log.info("checkTime test DB - end : " + endTime);
		log.info("checkTime test DB - duration : " + duration);

		return allUserPoint;
	}

}
