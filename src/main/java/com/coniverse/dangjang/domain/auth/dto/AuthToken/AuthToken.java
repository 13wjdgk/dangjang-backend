package com.coniverse.dangjang.domain.auth.dto.AuthToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AuthToken 객체
 *
 * @author EVE
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {
	private String accessToken;
	private String refreshToken;
	private String grantType;
	private Long expiresIn;

	public static AuthToken of(String accessToken, String refreshToken, String grantType, Long expiresIn) {
		return new AuthToken(accessToken, refreshToken, grantType, expiresIn);
	}
}