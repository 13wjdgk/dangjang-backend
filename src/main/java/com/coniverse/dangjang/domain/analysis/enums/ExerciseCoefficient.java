package com.coniverse.dangjang.domain.analysis.enums;

import java.util.Arrays;

import com.coniverse.dangjang.domain.code.enums.CommonCode;
import com.coniverse.dangjang.global.exception.EnumNonExistentException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 운동 계수
 *
 * @author TEO
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ExerciseCoefficient {
	WALK(0.9),
	RUN(2),
	BIKE(2.3),
	SWIM(2),
	HIKING(1.5),
	HEALTH(1.5);

	private final double coefficient;

	/**
	 * 운동 계수를 찾는다.
	 *
	 * @param type 운동 타입
	 * @return 운동의 칼로리 percentage
	 * @throws EnumNonExistentException enum이 존재하지 않을 경우
	 * @since 1.0.0
	 */
	public static double findCoefficientByType(CommonCode type) {
		return Arrays.stream(ExerciseCoefficient.values())
			.filter(c -> c.name().equals(type.name()))
			.findFirst()
			.map(ExerciseCoefficient::getCoefficient)
			.orElseThrow(EnumNonExistentException::new);
	}
}
