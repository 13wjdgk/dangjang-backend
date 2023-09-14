package com.coniverse.dangjang.domain.guide.exercise.service;

import static com.coniverse.dangjang.fixture.AnalysisDataFixture.*;
import static com.coniverse.dangjang.fixture.HealthMetricFixture.*;
import static com.coniverse.dangjang.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.coniverse.dangjang.domain.analysis.strategy.ExerciseAnalysisStrategy;
import com.coniverse.dangjang.domain.code.enums.CommonCode;
import com.coniverse.dangjang.domain.guide.exercise.document.ExerciseGuide;
import com.coniverse.dangjang.domain.guide.exercise.dto.ExerciseCalorie;
import com.coniverse.dangjang.domain.guide.exercise.dto.WalkGuideContent;
import com.coniverse.dangjang.domain.guide.exercise.repository.ExerciseGuideRepository;
import com.coniverse.dangjang.domain.healthmetric.service.HealthMetricSearchService;
import com.coniverse.dangjang.domain.user.entity.User;

/**
 * @author EVE
 * @since 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExerciseGuideGenerateServiceTest {
	private final String 등록_일자 = "2023-12-31";
	@Autowired
	private ExerciseGuideGenerateService exerciseGuideGenerateService;
	@MockBean
	private HealthMetricSearchService healthMetricSearchService;
	@Autowired
	private ExerciseGuideRepository exerciseGuideRepository;
	@Autowired
	private ExerciseAnalysisStrategy exerciseAnalysisStrategy;

	private String 테오_아이디;

	private User user;

	@BeforeEach
	void setUp() {
		user = 유저_테오();
		테오_아이디 = user.getOauthId();
		LocalDate 등록_일자_LocalDate = LocalDate.parse(this.등록_일자);
		when(healthMetricSearchService.findLastHealthMetricById(any(), any()))
			.thenReturn(체중건강지표_엔티티(user, 등록_일자_LocalDate));
	}

	@Order(200)
	@ParameterizedTest
	@ValueSource(strings = {"0", "11000", "1506", "33000", "9000"})
	void 걸음수_조언을_성공적으로_등록한다(String unit) {
		// given
		var data = exerciseAnalysisStrategy.analyze(걸음수_분석_데이터(user, CommonCode.STEP_COUNT, unit));
		exerciseGuideRepository.deleteAll();

		// when
		exerciseGuideGenerateService.createGuide(data);

		// then
		Optional<ExerciseGuide> 등록된_운동가이드 = exerciseGuideRepository.findByOauthIdAndCreatedAt(테오_아이디, 등록_일자);
		WalkGuideContent walkGuideContent = new WalkGuideContent(등록된_운동가이드.get().getNeedStepByTTS(), 등록된_운동가이드.get().getNeedStepByLastWeek());
		assertThat(등록된_운동가이드.get().getContent()).isEqualTo(walkGuideContent.guideTTS);
		assertThat(등록된_운동가이드.get().getComparedToLastWeek()).isEqualTo(walkGuideContent.guideLastWeek);
	}

	@Order(250)
	@ParameterizedTest
	@ValueSource(strings = {"10000", "0", "3000", "200", "11000"})
	void 걸음수_조언을_성공적으로_수정한다(String unit) {
		// given
		var data = exerciseAnalysisStrategy.analyze(걸음수_분석_데이터(user, CommonCode.STEP_COUNT, unit));

		// when
		exerciseGuideGenerateService.updateGuide(data);

		// then
		Optional<ExerciseGuide> 등록된_운동가이드 = exerciseGuideRepository.findByOauthIdAndCreatedAt(테오_아이디, 등록_일자);
		WalkGuideContent walkGuideContent = new WalkGuideContent(등록된_운동가이드.get().getNeedStepByTTS(), 등록된_운동가이드.get().getNeedStepByLastWeek());
		assertThat(등록된_운동가이드.get().getContent()).isEqualTo(walkGuideContent.guideTTS);
		assertThat(등록된_운동가이드.get().getComparedToLastWeek()).isEqualTo(walkGuideContent.guideLastWeek);
	}

	@Order(400)
	@ParameterizedTest
	@MethodSource("com.coniverse.dangjang.fixture.AnalysisDataFixture#운동_시간_목록")
	void 운동_조언을_성공적으로_등록한다(CommonCode type, String createdAt, String unit) {
		// given
		var data = exerciseAnalysisStrategy.analyze(운동_분석_데이터(user, type, createdAt, unit));
		Optional<ExerciseGuide> 이전_등록된_운동가이드 = exerciseGuideRepository.findByOauthIdAndCreatedAt(테오_아이디, createdAt);
		List<ExerciseCalorie> exerciseCalories = new ArrayList<>();
		int 등록되어야할_칼로리수 = 0;
		if (이전_등록된_운동가이드.isPresent()) {
			exerciseCalories = 이전_등록된_운동가이드.get().getExerciseCalories();
			등록되어야할_칼로리수 = 이전_등록된_운동가이드.get().getExerciseCalories().size();
		}
		if (type.equals(CommonCode.STEP_COUNT) == false) {
			exerciseCalories.add(운동_칼로리_데이터(type, Integer.parseInt(unit), 70));
			등록되어야할_칼로리수 += 1;
		}

		//when
		exerciseGuideGenerateService.createGuide(data);

		// then
		Optional<ExerciseGuide> 등록된_운동가이드 = exerciseGuideRepository.findByOauthIdAndCreatedAt(테오_아이디, createdAt);

		assertThat(등록된_운동가이드.get().getExerciseCalories()).hasSize(등록되어야할_칼로리수);
		assertThat(등록된_운동가이드.get().getExerciseCalories()).isEqualTo(exerciseCalories);
	}

	@Order(450)
	@ParameterizedTest
	@MethodSource("com.coniverse.dangjang.fixture.AnalysisDataFixture#운동_시간_수정목록")
	void 운동_조언을_성공적으로_수정한다(CommonCode type, String createdAt, String unit) {

		// given
		int weight = Integer.parseInt(healthMetricSearchService.findLastHealthMetricById(user.getOauthId(), CommonCode.MEASUREMENT).getUnit());
		var data = exerciseAnalysisStrategy.analyze(운동_분석_데이터(user, type, createdAt, unit));
		Optional<ExerciseGuide> 이전_등록된_운동가이드 = exerciseGuideRepository.findByOauthIdAndCreatedAt(테오_아이디, 등록_일자);
		int 등록되어있는_운동칼로리수 = 0;

		//when
		exerciseGuideGenerateService.updateGuide(data);

		// then
		Optional<ExerciseGuide> 등록된_운동가이드 = exerciseGuideRepository.findByOauthIdAndCreatedAt(테오_아이디, 등록_일자);
		if (이전_등록된_운동가이드.isPresent()) {
			등록되어있는_운동칼로리수 = 이전_등록된_운동가이드.get().getExerciseCalories().size();

		}
		assertThat(등록된_운동가이드.get().getExerciseCalories()).hasSize(등록되어있는_운동칼로리수);
		if (등록된_운동가이드.isPresent()) {
			for (int i = 0; i < 등록된_운동가이드.get().getExerciseCalories().size(); i++) {
				if (등록된_운동가이드.get().getExerciseCalories().get(i).type().equals(type)) {
					assertThat(등록된_운동가이드.get().getExerciseCalories().get(i)).isEqualTo(운동_칼로리_데이터(type, Integer.parseInt(unit), weight));
					break;
				}
			}
		}

	}
}
