package com.coniverse.dangjang.domain.healthmetric.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coniverse.dangjang.domain.code.enums.CommonCode;
import com.coniverse.dangjang.domain.code.enums.GroupCode;
import com.coniverse.dangjang.domain.guide.exercise.document.ExerciseGuide;
import com.coniverse.dangjang.domain.guide.exercise.service.ExerciseGuideSearchService;
import com.coniverse.dangjang.domain.healthmetric.dto.response.BloodSugarMinMax;
import com.coniverse.dangjang.domain.healthmetric.dto.response.HealthMetricChartData;
import com.coniverse.dangjang.domain.healthmetric.dto.response.HealthMetricChartResponse;
import com.coniverse.dangjang.domain.healthmetric.entity.HealthMetric;
import com.coniverse.dangjang.domain.healthmetric.exception.HealthMetricNotFoundException;
import com.coniverse.dangjang.global.util.LocalDateChangeUtil;

import lombok.RequiredArgsConstructor;

/**
 * 건강 지표 차트 조회 service
 *
 * @author EVE
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class HealthMetricChartSearchService {
	private final HealthMetricSearchService healthMetricSearchService;
	private final ExerciseGuideSearchService exerciseGuideSearchService;
	private LocalDateChangeUtil localDateChangeUtil = new LocalDateChangeUtil();

	/**
	 * 건강지표 차트 데이터를 조회한다.
	 *
	 * @param startDate 조회 시작 날짜
	 * @param endDate   조회 종료 날짜
	 * @param oauthId   유저 아이디
	 * @return HealthMetricChartResponse 건강지표 차트 데이터
	 * @author EVE
	 * @since 1.0.0
	 */

	public HealthMetricChartResponse findHealthMetricChart(String oauthId, LocalDate startDate, LocalDate endDate) {
		List<HealthMetric> healthMetrics = healthMetricSearchService.findAllHealthMetricByDate(oauthId, startDate, endDate);
		List<BloodSugarMinMax> bloodSugarMinMaxes = findBloodSugarMinMaxes(healthMetrics);
		List<HealthMetricChartData> weights = findWeights(healthMetrics);
		List<HealthMetricChartData> stepCounts = findStepCounts(healthMetrics);
		List<HealthMetricChartData> exerciseCalories = findExerciseCalories(oauthId, startDate, endDate);
		return new HealthMetricChartResponse(startDate, endDate, bloodSugarMinMaxes, weights, stepCounts, exerciseCalories);
	}

	/**
	 * 일정 기간 내의 혈당 최저,최고 값을 조회한다.
	 *
	 * @param healthMetrics 일정 기간 내, 유저의 모든 건강지표
	 * @return List<BloodSugarMinMax> 혈당 최저,최고 값 리스트
	 * @since 1.0.0
	 */

	private List<BloodSugarMinMax> findBloodSugarMinMaxes(List<HealthMetric> healthMetrics) {
		List<BloodSugarMinMax> bloodSugarMinMaxes = new ArrayList<>();
		healthMetrics = healthMetrics.stream().filter(healthMetric -> GroupCode.BLOOD_SUGAR.equals(healthMetric.getGroupCode())).toList();
		Map<LocalDate, List<HealthMetric>> bloodSugarMapByDate = healthMetrics.stream().collect(Collectors.groupingBy(HealthMetric::getCreatedAt));
		bloodSugarMapByDate.forEach((date, dailyHealthMetrics) -> {
			int minBloodSugar = dailyHealthMetrics.stream().mapToInt(healthMetric -> Integer.parseInt(healthMetric.getUnit())).min().orElseThrow(
				HealthMetricNotFoundException::new);
			int maxBloodSugar = dailyHealthMetrics.stream().mapToInt(healthMetric -> Integer.parseInt(healthMetric.getUnit())).max().orElseThrow(
				HealthMetricNotFoundException::new);
			bloodSugarMinMaxes.add(new BloodSugarMinMax(date, minBloodSugar, maxBloodSugar));

		});
		return bloodSugarMinMaxes;
	}

	/**
	 * 일정 기간 내의 체중 값을 조회한다.
	 *
	 * @param healthMetrics 일정 기간 내, 유저의 모든 건강지표
	 * @return List<HealthMetricChartData> 체중 값 리스트
	 * @since 1.0.0
	 */
	private List<HealthMetricChartData> findWeights(List<HealthMetric> healthMetrics) {
		List<HealthMetricChartData> weights = new ArrayList<>();
		healthMetrics.stream().filter(healthMetric -> CommonCode.MEASUREMENT.equals(healthMetric.getType()))
			.forEach(healthMetric -> weights.add(new HealthMetricChartData(healthMetric.getCreatedAt(), Integer.parseInt(healthMetric.getUnit()))));

		return weights;
	}

	/**
	 * 일정 기간 내의 걸음수를 조회한다.
	 *
	 * @param healthMetrics 일정 기간 내, 유저의 모든 건강지표
	 * @return List<HealthMetricChartData> 걸음수 리스트
	 * @since 1.0.0
	 */
	private List<HealthMetricChartData> findStepCounts(List<HealthMetric> healthMetrics) {
		List<HealthMetricChartData> stepCounts = new ArrayList<>();
		healthMetrics.stream().filter(healthMetric -> CommonCode.STEP_COUNT.equals(healthMetric.getType()))
			.forEach(healthMetric ->
				stepCounts.add(new HealthMetricChartData(healthMetric.getCreatedAt(), Integer.parseInt(healthMetric.getUnit())))
			);
		return stepCounts;
	}

	/**
	 * 기한 내의 운동 소모 칼로리를 조회한다.
	 *
	 * @param startDate 조회 시작 날짜
	 * @param endDate   조회 종료 날짜
	 * @param oauthId   유저 아이디
	 * @return List<HealthMetricChartData> 칼로리 리스트
	 * @since 1.0.0
	 */
	private List<HealthMetricChartData> findExerciseCalories(String oauthId, LocalDate startDate, LocalDate endDate) {
		List<HealthMetricChartData> exerciseCalories = new ArrayList<>();
		List<ExerciseGuide> exerciseGuides = exerciseGuideSearchService.findWeekByOauthIdAndCreatedAt(oauthId, startDate, endDate);
		exerciseGuides.forEach(exerciseGuide -> {
			int calorie = exerciseGuide.getExerciseCalories()
				.stream()
				.mapToInt(exerciseCalorie -> exerciseCalorie.calorie())
				.sum();
			if (calorie != 0) { //TODO 람다로 해결
				exerciseCalories.add(new HealthMetricChartData(localDateChangeUtil.convertDateToKST(exerciseGuide.getCreatedAt()), calorie));
			}
		});
		return exerciseCalories;
	}
}
