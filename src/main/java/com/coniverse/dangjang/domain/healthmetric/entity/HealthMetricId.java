package com.coniverse.dangjang.domain.healthmetric.entity;

import java.io.Serializable;
import java.time.LocalDate;

import com.coniverse.dangjang.domain.code.enums.CommonCode;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 건강지표 복합키
 *
 * @author TEO
 * @since 1.0.0
 */
@Embeddable
@Getter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class HealthMetricId implements Serializable {
	private String oauthId;
	private LocalDate createdAt;
	@Enumerated(EnumType.STRING)
	@Column(name = "code")
	private CommonCode commonCode;

	/**
	 * HealthMetric 생성 시 사용하는 생성자
	 *
	 * @author TEO
	 * @since 1.0.0
	 */
	protected HealthMetricId(LocalDate createdAt, CommonCode commonCode) {
		this.createdAt = createdAt;
		this.commonCode = commonCode;
	}
}
