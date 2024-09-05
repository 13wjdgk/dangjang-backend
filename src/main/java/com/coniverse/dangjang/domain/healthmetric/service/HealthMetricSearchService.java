package com.coniverse.dangjang.domain.healthmetric.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coniverse.dangjang.domain.code.enums.CommonCode;
import com.coniverse.dangjang.domain.code.enums.GroupCode;
import com.coniverse.dangjang.domain.healthmetric.dto.HealthMetricLastDateResponse;
import com.coniverse.dangjang.domain.healthmetric.entity.HealthMetric;
import com.coniverse.dangjang.domain.healthmetric.exception.HealthMetricNotFoundException;
import com.coniverse.dangjang.domain.healthmetric.repository.HealthMetricRepository;

import lombok.RequiredArgsConstructor;

/**
 * 건강 지표 조회 service
 *
 * @author TEO, EVE
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HealthMetricSearchService {
	private final HealthMetricRepository healthMetricRepository;

	/**
	 * 건강 지표를 조회한다.
	 *
	 * @param oauthId   유저 PK
	 * @param createdAt 생성일
	 * @param type      건강 지표 타입
	 * @return HealthMetric 건강 지표
	 * @throws HealthMetricNotFoundException 유저의 건강 지표를 찾을 수 없을 경우 발생한다.
	 * @since 1.0.0
	 */
	public HealthMetric findByHealthMetricId(String oauthId, LocalDate createdAt, CommonCode type) {
		return healthMetricRepository.findByHealthMetricId(oauthId, createdAt, type).orElseThrow(HealthMetricNotFoundException::new);
	}

	/**
	 * 최근 건강 지표를 조회한다.
	 *
	 * @param oauthId 유저 PK
	 * @param type    건강 지표 타입
	 * @return HealthMetric 건강 지표
	 * @throws HealthMetricNotFoundException 유저의 건강 지표를 찾을 수 없을 경우 발생한다.
	 * @since 1.0.0
	 */
	public HealthMetric findLastHealthMetricById(String oauthId, CommonCode type) {
		return healthMetricRepository.findByHealthMetricId(oauthId, type).orElseThrow(HealthMetricNotFoundException::new);
	}

	/**
	 * 주간 건강 지표를 조회한다.
	 *
	 * @param oauthId 유저 PK
	 * @param type    건강 지표 타입
	 * @return HealthMetric 건강 지표
	 * @since 1.0.0
	 */
	public List<HealthMetric> findWeeklyHealthMetricById(String oauthId, CommonCode type, LocalDate startDate, LocalDate endDate) {
		return healthMetricRepository.findLastWeekByHealthMetricId(oauthId, type, startDate, endDate);
	}

	/**
	 * 주간 건강 지표를 조회한다.
	 *
	 * @param oauthId 유저 PK
	 * @param code    그룹 코드
	 * @return HealthMetric 건강 지표
	 * @since 1.0.0
	 */
	public List<HealthMetric> findWeeklyHealthMetricByGroupCode(String oauthId, GroupCode code, LocalDate startDate, LocalDate endDate) {
		return healthMetricRepository.findLastWeekByGroupCodeAndCreatedAt(oauthId, code, startDate, endDate);
	}

	/**
	 * 유저의 마지막 건강 지표 생성일을 조회한다.
	 *
	 * @param oauthId 유저 아이디
	 * @return HealthMetricLastDateResponse 유저의 마지막 건강 지표 생성일
	 * @throws HealthMetricNotFoundException 유저의 건강 지표를 찾을 수 없을 경우 발생한다.
	 * @since 1.1.0
	 */
	public HealthMetricLastDateResponse findHealthMetricLastDate(String oauthId) {
		LocalDate lastDate = healthMetricRepository.findCreatedAtByOauthId(oauthId).orElseThrow(HealthMetricNotFoundException::new);
		return new HealthMetricLastDateResponse(lastDate);
	}

	/**
	 * 그룹코드와 생성날짜로 조회된 건강지표 개수를 카운트한다
	 *
	 * @param oauthId   유저 PK
	 * @param createdAt 생성일
	 * @param groupCode 그룹코드
	 * @return int 건강 지표 조회 개수
	 * @since 1.3.0
	 */
	public int findByGroupCode(String oauthId, GroupCode groupCode, LocalDate createdAt) {
		return healthMetricRepository.findHealthMetricCountByGroupCode(oauthId, groupCode, createdAt);
	}

	/**
	 * 일정 기간 내 유저의 모든 건강 지표를 조회한다.
	 *
	 * @param oauthId 유저 PK
	 * @return HealthMetric 건강 지표
	 * @since 1.0.0
	 */
	public List<HealthMetric> findAllHealthMetricByDate(String oauthId, LocalDate startDate, LocalDate endDate) {
		return healthMetricRepository.findAllHealthMetricByDate(oauthId, startDate, endDate);
	}

}
