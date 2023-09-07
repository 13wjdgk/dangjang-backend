package com.coniverse.dangjang.domain.guide.bloodsugar.dto;

import com.coniverse.dangjang.domain.analysis.enums.Alert;
import com.coniverse.dangjang.domain.guide.common.dto.GuideResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 혈당 서브 가이드 응답 dto
 *
 * @author TEO
 * @since 1.0.0
 */
public record SubGuideResponse(String type, @JsonInclude(JsonInclude.Include.NON_NULL) String unit, Alert alert, String content) implements GuideResponse {
}
