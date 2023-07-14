package com.coniverse.dangjang.domain.auth.dto.AuthToken;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author EVE
 * @since 1.0
 */
class AuthTokenTest {
	@Test
	void AuthToken_객체_생성() {
		AuthToken token = new AuthToken("accessToken", "refreshToken", "B", 1800L);
		assertThat(token).isNotNull();
	}
}
