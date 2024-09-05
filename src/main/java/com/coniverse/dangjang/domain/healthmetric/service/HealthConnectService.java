package com.coniverse.dangjang.domain.healthmetric.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coniverse.dangjang.domain.analysis.service.AnalysisService;
import com.coniverse.dangjang.domain.guide.common.service.GuideService;
import com.coniverse.dangjang.domain.healthmetric.dto.request.HealthConnectPostRequest;
import com.coniverse.dangjang.domain.healthmetric.dto.request.HealthConnectRegisterRequest;
import com.coniverse.dangjang.domain.healthmetric.entity.HealthMetric;
import com.coniverse.dangjang.domain.healthmetric.mapper.HealthMetricMapper;
import com.coniverse.dangjang.domain.healthmetric.repository.HealthConnectRepository;
import com.coniverse.dangjang.domain.point.service.PointService;
import com.coniverse.dangjang.domain.user.entity.User;
import com.coniverse.dangjang.domain.user.service.UserSearchService;

import lombok.RequiredArgsConstructor;

/**
 * health connect service
 *
 * @author TEO
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class HealthConnectService {
	private final UserSearchService userSearchService;
	private final HealthMetricMapper healthMetricMapper;
	private final GuideService guideService;
	private final AnalysisService analysisService;
	private final HealthConnectRepository healthConnectRepository;
	private final PointService pointService;

	/**
	 * health connect로 받은 n개의 건강 지표 데이터를 등록한다.
	 *
	 * @param request 건강 지표 request post dto n개
	 * @param oauthId 건강 지표 등록 유저 PK
	 * @since 1.0.0
	 */
	public void registerHealthConnectData(HealthConnectPostRequest request, String oauthId) { // TODO async
		final User user = userSearchService.findUserByOauthId(oauthId);

		List<HealthMetric> healthMetrics = request.data()
			.stream()
			.map(data -> healthMetricMapper.toEntity(data, user))
			.toList();
		healthConnectRepository.insertBatch(healthMetrics);
		healthMetrics.forEach(h -> guideService.createGuide(analysisService.analyze(h)));
	}

	/**
	 * health connect 연동 여부를 설정한다
	 *
	 * @param request health connect 연동 여부
	 * @param oauthId 유저 아이디
	 * @since 1.0.0
	 */
	public void interlockHealthConnect(HealthConnectRegisterRequest request, String oauthId) {
		User user = userSearchService.findUserByOauthId(oauthId);
		if (request.healthConnectInterlock()) {
			if (user.isNeverConnectedHealthConnect()) {
				pointService.addHealthConnectPoint(user);
			}
			user.connectToHealthConnect();
		} else if (user.isConnectingHealthConnect()) {
			user.disconnectToHealthConnect();
		}
	}
}
