package com.coniverse.dangjang.domain.auth.service.authToken;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.coniverse.dangjang.domain.auth.dto.AuthToken.AuthToken;

/**
 * @author EVE
 * @since 1.0
 */
@SpringBootTest
class AuthTokenGeneratorTest {
	@Autowired
	private AuthTokensGenerator authTokensGenerator;

	@Value("${oauth.kakao.sample-id}")
	private String exID;

	@Test
	void 토큰을_생성한다() {

		assertThat(authTokensGenerator.generate(exID)).isNotNull();
	}

	@Test
	void 유저_id를_추출한다() {
		//given
		AuthToken authToken = authTokensGenerator.generate(exID);
		assertThat(authTokensGenerator.extractMemberId(authToken.getAccessToken())).isNotNull();
	}

}
