package com.coniverse.dangjang.domain.healthmetric.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 건강지표 request patch dto이다.
 *
 * @author TEO
 * @since 1.0.0
 */
public record HealthMetricPatchRequest(@NotBlank(message = "건강지표 상세명은 필수로 입력해야 합니다.") String title, String newTitle,
									   @NotBlank(message = "단위는 필수로 입력해야 합니다.") String unit) {
}
