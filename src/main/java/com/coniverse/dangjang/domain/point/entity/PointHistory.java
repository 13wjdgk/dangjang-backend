package com.coniverse.dangjang.domain.point.entity;

import org.springframework.data.domain.Persistable;

import com.coniverse.dangjang.domain.point.dto.response.UserPointResponse;
import com.coniverse.dangjang.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 포인트 내역 Entity (포인트 적립, 구매)
 *
 * @author EVE
 * @since 1.0.0
 */
@Entity
@NamedNativeQuery(
	name = "findAllUserPoint",
	query =
		" select u.oauth_id as oauthId , COALESCE(p.totalPoint, 0) as totalPoint "
			+ "from  Users  u USE INDEX (PRIMARY)  "
			+ "left join ( "
			+ "    select oauth_id, SUM(change_point) as totalPoint  "
			+ "    from POINT_HISTORY "
			+ "    group by oauth_id "
			+ ") p on u.oauth_id = p.oauth_id "
			+ " LIMIT :limit OFFSET :offset",
	resultSetMapping = "mapToUserPointResponse"
)
@SqlResultSetMapping(
	name = "mapToUserPointResponse",
	classes = @ConstructorResult(
		targetClass = UserPointResponse.class,
		columns = {
			@ColumnResult(name = "oauthId", type = String.class),
			@ColumnResult(name = "totalPoint", type = Integer.class)
		}
	)
)

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PointHistory implements Persistable<PointId> {
	@EmbeddedId
	@Getter(AccessLevel.PRIVATE)
	private PointId pointId;
	@Column(nullable = false)
	private int changePoint;
	@MapsId("oauthId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "oauth_id", insertable = false, updatable = false)
	private User user;
	@MapsId("productName")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_name", insertable = false, updatable = false)
	private PointProduct pointProduct;

	@Builder
	private PointHistory(User user, int changePoint, PointProduct pointProduct) {
		this.pointId = new PointId();
		this.changePoint = changePoint;
		this.user = user;
		this.pointProduct = pointProduct;
	}

	/**
	 * 사용자 oauthId 조회
	 *
	 * @return String oauthId 사용자 아이디
	 * @since 1.0.0
	 */
	public String getOauthId() {
		return this.pointId.getOauthId();
	}

	/**
	 * product 조회
	 *
	 * @return String Product 포인트 상품
	 * @since 1.0.0
	 */
	public String getProductName() {
		return this.pointId.getProductName();
	}

	@Override
	public PointId getId() {
		return this.pointId;
	}

	@Override
	public boolean isNew() {
		return this.getOauthId() == null;
	}
}
