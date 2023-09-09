package com.coniverse.dangjang.domain.guide.exercise.document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.coniverse.dangjang.domain.guide.common.document.Guide;
import com.coniverse.dangjang.domain.guide.exercise.dto.ExerciseCalorie;

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
public class ExerciseGuide implements Guide {
	@Id
	private String id;
	private String oauthId;
	private int needStepByTTS;
	private int needStepByLastWeek;
	private LocalDate createdAt;
	private String content;
	private String comparedToLastWeek;
	private List<ExerciseCalorie> exerciseCalories = new ArrayList<>();

	@Builder(toBuilder = true)
	private ExerciseGuide(String oauthId, LocalDate createdAt, int needStepByTTS, String content, String comparedToLastWeek,
		int needStepByLastWeek, List<ExerciseCalorie> exerciseCalories) {
		this.oauthId = oauthId;
		this.createdAt = createdAt;
		this.needStepByTTS = needStepByTTS;
		this.content = content;
		this.comparedToLastWeek = comparedToLastWeek;
		this.needStepByLastWeek = needStepByLastWeek;
		this.exerciseCalories = exerciseCalories;
	}

	/**
	 * 가이드 내용을 전달한다.
	 *
	 * @return 가이드 내용
	 * @since 1.0.0
	 */
	@Override
	public String getTodayContent() {
		return this.content;
	}

	/**
	 * 가이드 ID를 설정한다.
	 *
	 * @since 1.0.0
	 */
	public void setId(String id) {
		this.id = id;
	}
}
