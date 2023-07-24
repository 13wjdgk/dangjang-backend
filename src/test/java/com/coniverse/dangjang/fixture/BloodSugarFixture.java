package com.coniverse.dangjang.fixture;

import java.time.LocalDate;

import com.coniverse.dangjang.domain.healthMetric.dto.request.HealthMetricRequest;
import com.coniverse.dangjang.domain.healthMetric.dto.response.HealthMetricResponse;
import com.coniverse.dangjang.domain.healthMetric.entity.HealthMetric;
import com.coniverse.dangjang.domain.healthMetric.entity.HealthMetricCode;
import com.coniverse.dangjang.domain.healthMetric.entity.HealthMetricType;
import com.coniverse.dangjang.domain.user.entity.User;

/**
 * 혈당 fixture
 *
 * @author TEO
 * @since 1.0.0
 */
public class BloodSugarFixture {
	public static HealthMetricRequest 혈당_등록_요청() {
		return new HealthMetricRequest("아침식전", "140");
	}

	public static HealthMetricResponse 혈당_등록_응답() {
		return new HealthMetricResponse(HealthMetricCode.BLOOD_SUGAR, HealthMetricType.BEFORE_BREAKFAST, LocalDate.of(2023, 12, 31), "140");
	}

	public static HealthMetric 정상_혈당(User user) {
		return HealthMetric.builder()
			.createdAt(LocalDate.of(2023, 12, 31))
			.healthMetricCode(HealthMetricCode.BLOOD_SUGAR)
			.healthMetricType(HealthMetricType.BEFORE_BREAKFAST)
			.user(user)
			.unit("100")
			.build();
	}
}
