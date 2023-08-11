package com.coniverse.dangjang.domain.feedback.service;

import org.springframework.stereotype.Component;

import com.coniverse.dangjang.domain.analysis.mapper.WeightMapper;
import com.coniverse.dangjang.domain.analysis.vo.AnalysisData.WeightAnalysisData;
import com.coniverse.dangjang.domain.feedback.document.WeightFeedback;
import com.coniverse.dangjang.domain.feedback.repository.WeightFeedbackRepository;

import lombok.RequiredArgsConstructor;

/**
 * 체중 조언서비스
 *
 * @author EVE
 * @see WeightAnalysisData
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class WeightFeedbackService {
	private final WeightFeedbackRepository weightFeedbackRepository;
	private final WeightMapper wieghtMapper;

	public String generateFeedback(WeightAnalysisData weightAnalysisData) {
		//TODO: 혈당 피드백 merge한 후 positive 수정
		String feedback = String.format("%s이에요. 평균 체중에 비해 %dkg %s", weightAnalysisData.getWeightSteps().getTitle(), weightAnalysisData.getWeightDiff(),
			weightAnalysisData.getWeightDiff() > 0 ? "많아요" : "적어요");
		return feedback;
	}

	public void saveFeedback(WeightAnalysisData weightAnalysisData) {
		String feddback = generateFeedback(weightAnalysisData);
		WeightFeedback weightFeedback = wieghtMapper.toDocument(weightAnalysisData, feddback);
		weightFeedbackRepository.save(weightFeedback);
	}
}
