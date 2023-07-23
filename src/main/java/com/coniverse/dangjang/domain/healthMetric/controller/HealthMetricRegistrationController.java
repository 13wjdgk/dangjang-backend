package com.coniverse.dangjang.domain.healthMetric.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.coniverse.dangjang.domain.healthMetric.dto.request.HealthMetricRequest;
import com.coniverse.dangjang.domain.healthMetric.dto.response.HealthMetricResponse;
import com.coniverse.dangjang.global.dto.SuccessMultiResponse;
import com.coniverse.dangjang.global.dto.SuccessSingleResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

/**
 * 건강 지표 등록 controller interface이다.
 *
 * @author TEO
 * @since 1.0.0
 */
@Validated
public interface HealthMetricRegistrationController {
	/**
	 * HTTP POST METHOD
	 * TODO @AuthenticationPrincipal Map<String, Object> principal
	 *
	 * @since 1.0.0
	 */
	ResponseEntity<SuccessSingleResponse<HealthMetricResponse>> post(@Positive @PathVariable(value = "month") int month,
		@Positive @PathVariable(name = "day") int day,
		@Valid @RequestBody HealthMetricRequest requestBody);

	/**
	 * HTTP PATCH METHOD
	 *
	 * @since 1.0.0
	 */
	ResponseEntity<SuccessSingleResponse<HealthMetricResponse>> patch(@Positive @PathVariable(value = "month") int month,
		@Positive @PathVariable(name = "day") int day,
		@Valid @RequestBody HealthMetricRequest requestBody);

	/**
	 * HTTP GET METHOD
	 *
	 * @since 1.0.0
	 */
	ResponseEntity<SuccessMultiResponse<HealthMetricResponse>> get(@Positive @PathVariable(value = "month") int month,
		@Positive @PathVariable(name = "day") int day);

}

