package com.coniverse.dangjang.domain.analysis.vo.AnalysisData;

import java.time.LocalDate;

import com.coniverse.dangjang.domain.code.enums.CommonCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 분석 데이터 추상 클래스
 *
 * @author TEO
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public abstract class AbstractAnalysisData {
	private LocalDate createdAt;
	private CommonCode commonCode;
	private String oauthId;

	/**
	 * 각 분석 데이터마다 단위의 타입이 다르기 때문에 타입을 변환한다
	 *
	 * @param unit 변환할 단위
	 * @since 1.0.0
	 */
	abstract void convertUnit(String unit);
}
