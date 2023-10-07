package com.coniverse.dangjang.domain.auth.controller;

import static com.coniverse.dangjang.fixture.LoginFixture.*;
import static com.coniverse.dangjang.support.SimpleMockMvc.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import com.coniverse.dangjang.domain.auth.dto.AuthToken;
import com.coniverse.dangjang.domain.auth.dto.request.KakaoLoginRequest;
import com.coniverse.dangjang.domain.auth.dto.request.NaverLoginRequest;
import com.coniverse.dangjang.domain.auth.dto.response.LoginResponse;
import com.coniverse.dangjang.domain.auth.service.OauthLoginService;
import com.coniverse.dangjang.support.ControllerTest;

/**
 * @author EVE, TEO
 * @since 1.0.0
 */
class LoginControllerTest extends ControllerTest {
	private static final String URI = "/api/auth";
	private final LoginResponse response = 로그인_응답();
	@Autowired
	private OauthLoginService oauthLoginService;

	@Test
	void 카카오_로그인에_성공한다() throws Exception {
		// given
		KakaoLoginRequest request = 카카오_로그인_요청();
		String content = objectMapper.writeValueAsString(request);
		AuthToken authToken = new AuthToken();
		authToken.setAccessToken("accessToken");
		authToken.setRefreshToken("refreshToken");
		given(oauthLoginService.login(request)).willReturn(response);
		given(oauthLoginService.getAuthToken(response.nickname())).willReturn(authToken);

		// when
		ResultActions resultActions = post(mockMvc, URI + "/kakao", content);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()),
			header().exists("AccessToken"),
			header().exists("RefreshToken")
		);

	}

	@Test
	void 카카오_로그인_시_RequestBody의_access_token이_비어있으면_예외가_발생한다() throws Exception {
		// given
		KakaoLoginRequest request = new KakaoLoginRequest("");
		String content = objectMapper.writeValueAsString(request);

		// when
		ResultActions resultActions = post(mockMvc, URI + "/kakao", content);

		// then
		resultActions.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.errorCode").value(400),
			jsonPath("$.fieldErrors[0].field").value("accessToken"),
			jsonPath("$.fieldErrors[0].rejectedValue").value("")
		);
	}

	@Test
	void 네이버_로그인에_성공한다() throws Exception {
		// given
		NaverLoginRequest request = 네이버_로그인_요청();
		String content = objectMapper.writeValueAsString(request);
		AuthToken authToken = new AuthToken();
		authToken.setAccessToken("accessToken");
		authToken.setRefreshToken("refreshToken");
		given(oauthLoginService.login(request)).willReturn(response);
		given(oauthLoginService.getAuthToken(response.nickname())).willReturn(authToken);

		// when
		ResultActions resultActions = post(mockMvc, URI + "/naver", content);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()),
			header().exists("AccessToken"),
			header().exists("RefreshToken")
		);
	}

	@Test
	void 네이버_로그인_시_RequestBody의_access_token이_비어있으면_예외가_발생한다() throws Exception {
		// given
		NaverLoginRequest request = new NaverLoginRequest(" ");
		String content = objectMapper.writeValueAsString(request);

		// when
		ResultActions resultActions = post(mockMvc, URI + "/naver", content);

		// then
		resultActions.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.errorCode").value(400),
			jsonPath("$.fieldErrors[0].field").value("accessToken"),
			jsonPath("$.fieldErrors[0].rejectedValue").value(" ")
		);
	}

	@Test
	void refreshToken으로_auth토큰을_재발급_한다() throws Exception {
		// given
		String subUrl = URI + "/reissue";
		String header = "Bearer " + "refreshToken";
		AuthToken authToken = AuthToken.of("accessToken", "refreshToken", "", 1000L);
		given(oauthLoginService.reissueToken(any())).willReturn(authToken);
		// when
		ResultActions resultActions = post(mockMvc, subUrl, header);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()),
			header().exists("AccessToken"),
			header().exists("RefreshToken")
		);
	}
}
