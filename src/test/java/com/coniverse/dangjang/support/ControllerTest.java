package com.coniverse.dangjang.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.MockMvc;

import com.coniverse.dangjang.domain.auth.controller.LoginController;
import com.coniverse.dangjang.domain.auth.service.JwtTokenProvider;
import com.coniverse.dangjang.domain.auth.service.OauthLoginService;
import com.coniverse.dangjang.domain.healthmetric.controller.HealthMetricRegisterController;
import com.coniverse.dangjang.domain.healthmetric.service.HealthMetricRegisterService;
import com.coniverse.dangjang.domain.intro.controller.IntroController;
import com.coniverse.dangjang.domain.intro.service.IntroService;
import com.coniverse.dangjang.domain.user.controller.SignupController;
import com.coniverse.dangjang.domain.user.controller.UserController;
import com.coniverse.dangjang.domain.user.service.UserSignupService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * WebMvc Test parent class
 * <br/>
 * contollers에 테스트할 controller를 등록하고, controller에서 주입하는 bean들을 MockBean으로 등록한다.
 *
 * @author TEO
 * @since 1.0.0
 */
@WebMvcTest(
	controllers = {
		IntroController.class,
		HealthMetricRegisterController.class,
		LoginController.class,
		SignupController.class,
		UserController.class
	},
	includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@ComponentScan(basePackages = "com.coniverse.dangjang.domain.auth.handler")
@MockBean(JpaMetamodelMappingContext.class)
public class ControllerTest {
	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;
	@MockBean
	private IntroService introService;
	@MockBean
	private OauthLoginService oAuthLoginService;
	@MockBean
	private HealthMetricRegisterService healthMetricRegisterService;
	@MockBean
	private UserSignupService userSignupService;
	@MockBean
	private JwtTokenProvider jwtTokenProvider;
}
