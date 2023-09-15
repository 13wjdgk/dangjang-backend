package com.coniverse.dangjang.domain.analysis.enums;

import java.util.Arrays;
import java.util.function.Function;

import com.coniverse.dangjang.domain.analysis.exception.NonAnalyticDataException;

import lombok.Getter;

/**
 * BMI 분석 ENUM
 *
 * @author EVE
 * @since 1.0.0
 */
@Getter
public enum BmiAlert {
	LOW_WEIGHT(bmi -> (0 <= bmi && bmi < 18.5), Alert.LOW_WEIGHT),
	NORMAL_WEIGHT(bmi -> (18.5 <= bmi && bmi < 22.9), Alert.NORMAL_WEIGHT),
	OVERWEIGHT(bmi -> (22.9 <= bmi && bmi < 24.9), Alert.OVERWEIGHT),
	LEVEL_1_OBESITY(bmi -> (24.9 <= bmi && bmi < 29.9), Alert.LEVEL_1_OBESITY),
	LEVEL_2_OBESITY(bmi -> (29.9 <= bmi && bmi < 34.9), Alert.LEVEL_2_OBESITY),
	LEVEL_3_OBESITY(bmi -> (34.9 <= bmi), Alert.LEVEL_3_OBESITY);

	private final Alert alert;
	private final Function<Double, Boolean> alertFunction;

	BmiAlert(Function<Double, Boolean> alertFunction, Alert alert) {
		this.alertFunction = alertFunction;
		this.alert = alert;
	}

	/**
	 * BMI 수치에 따라 경보를 찾는다.
	 *
	 * @param bmi BMI 수치
	 * @return 경보
	 * @throws NonAnalyticDataException 분석할 수 없는 데이터일 때 발생하는 예외
	 * @since 1.0.0
	 */
	public static Alert findAlertByBmi(Double bmi) {

		return Arrays.stream(BmiAlert.values())
			.filter(value -> value.getAlertFunction().apply(bmi))
			.findFirst()
			.orElseThrow(NonAnalyticDataException::new)
			.getAlert();

	}
}
