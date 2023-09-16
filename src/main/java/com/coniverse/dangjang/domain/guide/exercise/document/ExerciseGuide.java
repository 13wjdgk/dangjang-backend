package com.coniverse.dangjang.domain.guide.exercise.document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 운동 가이드 Document
 *
 * @author EVE
 * @since 1.0.0
 */
@Setter
@Getter
@Document
@NoArgsConstructor
public class ExerciseGuide {
	@Id
	private String id;
	private String oauthId;
	private int needStepByTTS;
	private int needStepByLastWeek;
	private LocalDate createdAt;
	private String content;
	private String comparedToLastWeek;
	private int stepCount;
	private List<ExerciseCalorie> exerciseCalories = new ArrayList<>();

	@Builder
	private ExerciseGuide(String oauthId, LocalDate createdAt, int needStepByTTS, String content, String comparedToLastWeek,
		int needStepByLastWeek, List<ExerciseCalorie> exerciseCalories, int stepCount) {
		this.oauthId = oauthId;
		this.createdAt = changeDatetoUTC(createdAt);
		this.needStepByTTS = needStepByTTS;
		this.content = content;
		this.comparedToLastWeek = comparedToLastWeek;
		this.needStepByLastWeek = needStepByLastWeek;
		this.exerciseCalories = exerciseCalories;
		this.stepCount = stepCount;
	}

	private LocalDate changeDatetoUTC(LocalDate day) {
		LocalDate date = LocalDate.parse(day.toString());
		date.atTime(9, 0, 0);
		return date;
	}

	/**
	 * 걸음수와 관련된 속성들을 변경한다.
	 *
	 * @param needStepByTTS      만보 대비 필요한 걸음수
	 * @param needStepByLastWeek 지난주 평균 걸음수 대비 필요한 걸음수
	 * @param comparedToLastWeek 지난주 걸음수와 비교한 가이드
	 * @param content            만보 대비 걸음수에 대한 가이드
	 * @since 1.0.0
	 */
	public void changeAboutWalk(int needStepByTTS, int needStepByLastWeek, String comparedToLastWeek, String content, int stepCount) {
		this.needStepByTTS = needStepByTTS;
		this.needStepByLastWeek = needStepByLastWeek;
		this.comparedToLastWeek = comparedToLastWeek;
		this.content = content;
		this.stepCount = stepCount;
	}

	/**
	 * 수정된 운동 칼로리를 업데이트한다.
	 * <p>
	 * 기존에 존재하는 운동 칼로리를 삭제하고, 새로운 운동 칼로리를 추가한다.
	 *
	 * @param updateExerciseCalorie 운동칼로리 객체
	 * @since 1.0.0
	 */
	public void changeExerciseCalories(ExerciseCalorie updateExerciseCalorie) {
		exerciseCalories.stream()
			.filter(existExerciseCalorie -> existExerciseCalorie.type().equals(updateExerciseCalorie.type()))
			.findFirst()
			.ifPresent(existExerciseCalorie -> exerciseCalories.remove(existExerciseCalorie));
		exerciseCalories.add(updateExerciseCalorie);
	}
}
