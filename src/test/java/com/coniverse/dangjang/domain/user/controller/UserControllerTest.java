package com.coniverse.dangjang.domain.user.controller;

import static com.coniverse.dangjang.support.SimpleMockMvc.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.coniverse.dangjang.domain.user.dto.SignUpRequest;
import com.coniverse.dangjang.domain.user.dto.TestRequestMethod;
import com.coniverse.dangjang.domain.user.service.UserService;
import com.coniverse.dangjang.support.ControllerTest;

/**
 * @author EVE
 * @since 1.0
 */
public class UserControllerTest extends ControllerTest {
	private final String URI = "/api/duplicateNickname";
	@Autowired
	private UserService userService;

	@Test
	void 중복되지_않은_닉네임_확인을_성공한다() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("nickname", "hello");
		// when
		ResultActions resultActions = get(mockMvc, URI, params);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase())
		);

	}

	@Test
	void 중복된_닉네임_확인을_성공한다() throws Exception {
		// given
		SignUpRequest signUpRequest = TestRequestMethod.getSignUpRequestNaver();
		userService.signUp(signUpRequest);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("nickname", signUpRequest.nickname());
		// when
		ResultActions resultActions = get(mockMvc, URI, params);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase())

		);

	}

}
