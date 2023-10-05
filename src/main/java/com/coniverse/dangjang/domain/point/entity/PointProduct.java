package com.coniverse.dangjang.domain.point.entity;

import com.coniverse.dangjang.domain.point.enums.PointType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 포인트 상품 Entity
 *
 * @author EVE
 * @since 1.0.0
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointProduct {
	@Id
	@Column(name = "product_name", nullable = false)
	private String productName;
	private int point;
	@Enumerated(EnumType.STRING)
	@JsonIgnore
	private PointType type;

	@Builder
	private PointProduct(String productName, int point, PointType type) {
		this.productName = productName;
		this.point = point;
		this.type = type;
	}

}
