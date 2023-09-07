package com.coniverse.dangjang.domain.guide.bloodsugar.document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.coniverse.dangjang.domain.analysis.enums.Alert;
import com.coniverse.dangjang.domain.code.enums.CommonCode;
import com.coniverse.dangjang.domain.guide.common.document.Guide;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 혈당 가이드 entity
 *
 * @author TEO
 * @since 1.0.0
 */
@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BloodSugarGuide implements Guide {
	@Id
	private String id;
	private String oauthId;
	private LocalDate createdAt;
	private List<SubGuide> subGuides;
	private Alert todayAlert;
	private String todayContent;
	private String summary;

	@Builder
	private BloodSugarGuide(String oauthId, LocalDate createdAt, Alert todayAlert, String todayContent, String summary) {
		this.oauthId = oauthId;
		this.createdAt = createdAt;
		this.subGuides = new ArrayList<>();
		this.todayAlert = todayAlert;
		this.todayContent = todayContent;
		this.summary = summary;
	}

	/**
	 * 오늘의 경보, 오늘의 가이드 내용을 업데이트한다.
	 *
	 * @param todayAlert   오늘의 경보
	 * @param todayContent 오늘의 가이드 내용
	 * @since 1.0.0
	 */
	public void updateToday(Alert todayAlert, String todayContent) { // TODO 22시마다 수행
		this.todayAlert = todayAlert;
		this.todayContent = todayContent;
	}

	/**
	 * 서브 가이드를 추가한다.
	 *
	 * @param subGuide 서브 가이드
	 * @throws IllegalArgumentException 이미 해당 가이드가 존재할 경우 발생한다.
	 * @since 1.0.0
	 */
	public void add(SubGuide subGuide) {
		if (verifySubGuideExists(subGuide.getType())) {
			throw new IllegalArgumentException("이미 해당 가이드가 존재합니다."); // TODO
		}
		this.subGuides.add(subGuide);
	}

	/**
	 * 서브 가이드를 업데이트한다.
	 *
	 * @param subGuide 서브 가이드
	 * @since 1.0.0
	 */
	public void update(SubGuide subGuide) {
		SubGuide newSubGuide = getSubGuide(subGuide.getType());
		newSubGuide.update(subGuide.getContent(), subGuide.getAlert());
	}

	/**
	 * 타입이 변경된 서브 가이드를 업데이트한다.
	 *
	 * @param subGuide 서브 가이드
	 * @param prevType 변경할 타입
	 * @since 1.0.0
	 */
	public void update(SubGuide subGuide, CommonCode prevType) {
		SubGuide newSubGuide = getSubGuide(prevType);
		newSubGuide.update(subGuide.getType(), subGuide.getContent(), subGuide.getAlert());
	}

	/**
	 * 서브 가이드가 존재하는지 확인한다.
	 *
	 * @param type 서브 가이드 타입
	 * @return 서브 가이드가 존재하면 true, 존재하지 않으면 false
	 * @since 1.0.0
	 */
	private boolean verifySubGuideExists(CommonCode type) {
		return this.subGuides.stream().anyMatch(guide -> guide.isSameType(type));
	}

	/**
	 * 서브 가이드를 가져온다.
	 *
	 * @param type 서브 가이드 타입
	 * @return 서브 가이드
	 * @throws IllegalArgumentException 해당 타입의 서브 가이드가 존재하지 않을 경우 발생한다.
	 * @since 1.0.0
	 */
	private SubGuide getSubGuide(CommonCode type) {
		return this.subGuides.stream()
			.filter(guide -> guide.isSameType(type))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new); // TODO
	}
}
