package com.coniverse.dangjang.domain.point.dto.response;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자의 현재 포인트를 전달하는 Response
 *
 * @author EVE
 * @since 1.6.0
 */
@Getter
@Setter
@Entity
public class UserPointResponse {
	@Id
	String oauthId;
	int totalPoint;

	public UserPointResponse(String oauthId, int totalPoint) {
		this.oauthId = oauthId;
		this.totalPoint = totalPoint;
	}

	public UserPointResponse() {
		oauthId = "";
		totalPoint = 0;
	}

}
