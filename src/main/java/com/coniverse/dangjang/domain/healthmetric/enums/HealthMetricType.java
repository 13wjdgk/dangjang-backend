package com.coniverse.dangjang.domain.healthmetric.enums;

import java.util.Arrays;

import com.coniverse.dangjang.domain.healthmetric.exception.HealthMetricTypeNonExistentException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 건강지표 타입
 *
 * @author TEO
 * @since 1.0.0
 */
@AllArgsConstructor
public enum HealthMetricType {
	BED_TIME("취침시간"),
	WAKE_UP_TIME("기상시간"),
	EMPTY_STOMACH("공복"),
	BEFORE_BREAKFAST("아침식전"),
	AFTER_BREAKFAST("아침식후"),
	BEFORE_LUNCH("점심식전"),
	AFTER_LUNCH("점심식후"),
	BEFORE_DINNER("저녁식전"),
	AFTER_DINNER("저녁식후"),
	BEFORE_SLEEP("취침전"),
	ETC("기타"),
	SYSTOLIC("수축"),
	DIASTOLIC("이완"),
	MEASUREMENT("몸무게"),
	HBA1C("당화혈색소"),
	HIGH_INTENSITY_MINUTES("고강도운동"),
	MODERATE_INTENSITY_MINUTES("중강도운동"),
	STEP_COUNT("걸음수");

	@Getter
	private final String title;

	/**
	 * title로 건강지표 타입을 찾는다.
	 *
	 * @param title 건강지표 타입 제목
	 * @return HealthMetricType 건강지표 타입
	 * @throws HealthMetricTypeNonExistentException 건강지표 타입을 찾을 수 없을 때 발생하는 예외
	 * @since 1.0.0
	 */
	public static HealthMetricType findByTitle(String title) {
		return Arrays.stream(HealthMetricType.values())
			.filter(type -> type.getTitle().equals(title))
			.findAny()
			.orElseThrow(HealthMetricTypeNonExistentException::new);
	}
}
