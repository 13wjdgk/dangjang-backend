package com.coniverse.dangjang.domain.healthMetric.controller;

import static com.coniverse.dangjang.fixture.BloodSugarFixture.*;
import static com.coniverse.dangjang.support.SimpleMockMvc.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import com.coniverse.dangjang.domain.healthMetric.dto.request.HealthMetricRequest;
import com.coniverse.dangjang.domain.healthMetric.dto.response.HealthMetricResponse;
import com.coniverse.dangjang.domain.healthMetric.service.bloodSugar.BloodSugarRegistrationService;
import com.coniverse.dangjang.support.ControllerTest;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author TEO
 * @since 1.0.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BloodSugarRegistrationControllerTest extends ControllerTest {
	private final HealthMetricResponse bloodSugarResponse = 혈당_등록_응답();
	private String content;
	@Autowired
	private BloodSugarRegistrationService bloodSugarService;

	private static IntStream provideMonth() {
		return IntStream.range(1, 13);
	}

	private static IntStream provideDay() {
		return IntStream.range(1, 32);
	}

	@BeforeAll
	void setUp() throws JsonProcessingException {
		content = objectMapper.writeValueAsString(혈당_등록_요청());
	}

	@Order(100)
	@Test
	void 혈당을_등록하면_성공_메시지를_반환한다() throws Exception {
		// given
		given(bloodSugarService.save(any(), anyInt(), anyInt())).willReturn(bloodSugarResponse);

		// when
		ResultActions resultActions = post(mockMvc, "/api/health-metric/blood-sugar/{month}/{day}", content, 7, 8);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()),
			jsonPath("$.data.createdAt").value(bloodSugarResponse.createdAt().toString()),
			jsonPath("$.data.healthMetricCode").value(bloodSugarResponse.healthMetricCode().toString()),
			jsonPath("$.data.healthMetricType").value(bloodSugarResponse.healthMetricType().toString()),
			jsonPath("$.data.unit").value(bloodSugarResponse.unit())
		);
	}

	@Order(150)
	@Test
	void 혈당을_수정하면_성공_메시지를_반환한다() throws Exception {
		// given
		given(bloodSugarService.update(any(), anyInt(), anyInt())).willReturn(bloodSugarResponse);

		// when
		ResultActions resultActions = patch(mockMvc, "/api/health-metric/blood-sugar/{month}/{day}", content, 7, 8);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()),
			jsonPath("$.data.createdAt").value(bloodSugarResponse.createdAt().toString())
		);
	}

	@Order(200)
	@ParameterizedTest
	@ValueSource(ints = {-1, 0, 13})
	void 범위를_벗어나는_PathVariable_month를_입력하면_예외가_발생한다(int month) throws Exception {
		// when
		ResultActions resultActions = post(mockMvc, "/api/health-metric/blood-sugar/{month}/{day}", content, month, 1);

		// then
		resultActions.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.errorCode").value(400),
			jsonPath("$.violationErrors[0].rejectedValue").value(month)
		);
	}

	@Order(300)
	@ParameterizedTest
	@MethodSource("provideMonth")
	void 올바른_PathVariable_month를_입력하면_성공메시지를_반환한다(int month) throws Exception {
		// given
		given(bloodSugarService.save(any(), anyInt(), anyInt())).willReturn(bloodSugarResponse);

		// when
		ResultActions resultActions = post(mockMvc, "/api/health-metric/blood-sugar/{month}/{day}", content, month, 8);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase())
		);
	}

	@Order(400)
	@ParameterizedTest
	@ValueSource(ints = {-1, 0, 32})
	void 범위를_벗어나는_PathVariable_day를_입력하면_예외가_발생한다(int day) throws Exception {
		// when
		ResultActions resultActions = post(mockMvc, "/api/health-metric/blood-sugar/{month}/{day}", content, 1, day);

		// then
		resultActions.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.errorCode").value(400),
			jsonPath("$.violationErrors[0].rejectedValue").value(day)
		);
	}

	@Order(500)
	@ParameterizedTest
	@MethodSource("provideDay")
	void 올바른_PathVariable_day를_입력하면_성공메시지를_반환한다(int day) throws Exception {
		// given
		given(bloodSugarService.save(any(), anyInt(), anyInt())).willReturn(bloodSugarResponse);

		// when
		ResultActions resultActions = post(mockMvc, "/api/health-metric/blood-sugar/{month}/{day}", content, 1, day);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase())
		);
	}

	@Order(600)
	@Test
	void 건강지표_등록_RequestBody의_건강지표_타입이_비어있으면_예외가_발생한다() throws Exception {
		// given
		HealthMetricRequest bloodSugarRequest = new HealthMetricRequest("", "100");
		String content = objectMapper.writeValueAsString(bloodSugarRequest);

		// when
		ResultActions resultActions = post(mockMvc, "/api/health-metric/blood-sugar/{month}/{day}", content, 7, 8);

		// then
		resultActions.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.errorCode").value(400),
			jsonPath("$.fieldErrors[0].field").value("healthMetricType"),
			jsonPath("$.fieldErrors[0].rejectedValue").value("")
		);
	}

	@Order(700)
	@Test
	void 건강지표_등록_RequestBody의_unit이_비어있으면_예외가_발생한다() throws Exception {
		// given
		HealthMetricRequest bloodSugarRequest = new HealthMetricRequest("아침 식전", " ");
		String content = objectMapper.writeValueAsString(bloodSugarRequest);

		// when
		ResultActions resultActions = post(mockMvc, "/api/health-metric/blood-sugar/{month}/{day}", content, 7, 8);

		// then
		resultActions.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.errorCode").value(400),
			jsonPath("$.fieldErrors[0].field").value("unit"),
			jsonPath("$.fieldErrors[0].rejectedValue").value(" ")
		);
	}
}
