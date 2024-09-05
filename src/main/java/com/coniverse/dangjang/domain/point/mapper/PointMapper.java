package com.coniverse.dangjang.domain.point.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.coniverse.dangjang.domain.point.dto.request.UsePointRequest;
import com.coniverse.dangjang.domain.point.entity.PointHistory;
import com.coniverse.dangjang.domain.point.entity.PointProduct;
import com.coniverse.dangjang.domain.point.entity.PurchaseHistory;
import com.coniverse.dangjang.domain.user.entity.User;

/**
 * Point Mapper
 *
 * @author EVE
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface PointMapper {
	/**
	 * Point Log Entity 변환
	 *
	 * @since 1.0.0
	 */

	@Mapping(target = "changePoint", source = "changePoint")
	PointHistory toEntity(PointProduct pointProduct, User user, int changePoint);

	/**
	 * ProductPurchase Entity 변환
	 *
	 * @since 1.0.0
	 */
	PurchaseHistory toEntity(User user, PointProduct pointProduct, UsePointRequest userInfo);
}
