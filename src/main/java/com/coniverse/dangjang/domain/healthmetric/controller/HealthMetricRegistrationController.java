package com.coniverse.dangjang.domain.healthmetric.controller;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coniverse.dangjang.domain.healthmetric.dto.request.HealthMetricPatchRequest;
import com.coniverse.dangjang.domain.healthmetric.dto.request.HealthMetricPostRequest;
import com.coniverse.dangjang.domain.healthmetric.dto.response.HealthMetricResponse;
import com.coniverse.dangjang.domain.healthmetric.service.HealthMetricRegistrationService;
import com.coniverse.dangjang.global.dto.SuccessSingleResponse;
import com.coniverse.dangjang.global.util.CreatedAtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 건강 지표 등록 controller
 *
 * @author TEO
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/health-metric")
@RequiredArgsConstructor
@Validated
public class HealthMetricRegistrationController { // TODO @AuthenticationPrincipal Map<String, Object> principal
	private final HealthMetricRegistrationService healthMetricRegistrationService;

	/**
	 * HTTP POST METHOD
	 *
	 * @since 1.0.0
	 */
	@PostMapping("/{month}/{day}")
	public ResponseEntity<SuccessSingleResponse<HealthMetricResponse>> post(@Range(min = 1, max = 12) @PathVariable(value = "month") int month,
		@Range(min = 1, max = 31) @PathVariable(name = "day") int day, @Valid @RequestBody HealthMetricPostRequest postRequest) {
		LocalDate createdAt = CreatedAtUtil.generateCreatedAt(month, day);
		HealthMetricResponse response = healthMetricRegistrationService.register(postRequest, createdAt, "11111111");
		return ResponseEntity.ok().body(new SuccessSingleResponse<>(HttpStatus.OK.getReasonPhrase(), response));
	}

	/**
	 * HTTP PATCH METHOD
	 *
	 * @since 1.0.0
	 */
	@PatchMapping("/{month}/{day}")
	public ResponseEntity<SuccessSingleResponse<HealthMetricResponse>> patch(@Range(min = 1, max = 12) @PathVariable(value = "month") int month,
		@Range(min = 1, max = 31) @PathVariable(name = "day") int day, @Valid @RequestBody HealthMetricPatchRequest patchRequest) {
		LocalDate createdAt = CreatedAtUtil.generateCreatedAt(month, day);
		HealthMetricResponse response = healthMetricRegistrationService.update(patchRequest, createdAt, "11111111");
		return ResponseEntity.ok().body(new SuccessSingleResponse<>(HttpStatus.OK.getReasonPhrase(), response));
	}
}
