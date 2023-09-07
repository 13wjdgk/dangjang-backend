package com.coniverse.dangjang.global.util;

import java.time.DateTimeException;

import com.coniverse.dangjang.global.exception.IncorrectCreatedAtException;
import com.coniverse.dangjang.global.validator.LocalDate;

/**
 * 생성일자와 관련한 util
 *
 * @author TEO
 * @since 1.0.0
 * @deprecated use {@link LocalDate}
 */
@Deprecated(since = "1.0.0")
public class CreatedAtUtil {
	/**
	 * 생성일자를 생성한다.
	 *
	 * @param month 생성일자의 월
	 * @param day   생성일자의 일
	 * @return LocalDate 생성일자
	 * @throws IncorrectCreatedAtException 생성일자가 올바르지 않을 때 발생하는 예외
	 * @since 1.0.0
	 */
	public static java.time.LocalDate generateCreatedAt(int month, int day) {
		try {
			return java.time.LocalDate.of(java.time.LocalDate.now().getYear(), month, day);
		} catch (DateTimeException e) {
			throw new IncorrectCreatedAtException();
		}
	}
}
