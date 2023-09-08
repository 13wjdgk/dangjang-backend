package com.coniverse.dangjang.domain.guide.weight.service;

import org.springframework.stereotype.Component;

import com.coniverse.dangjang.domain.analysis.dto.AnalysisData;
import com.coniverse.dangjang.domain.analysis.dto.healthMetric.WeightAnalysisData;
import com.coniverse.dangjang.domain.code.enums.GroupCode;
import com.coniverse.dangjang.domain.guide.common.dto.GuideResponse;
import com.coniverse.dangjang.domain.guide.common.service.GuideGenerateService;
import com.coniverse.dangjang.domain.guide.weight.document.WeightGuide;
import com.coniverse.dangjang.domain.guide.weight.mapper.WeightMapper;
import com.coniverse.dangjang.domain.guide.weight.repository.WeightGuideRepository;

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
public class WeightGuideGenerateService implements GuideGenerateService {
	private final WeightGuideRepository weightGuideRepository;
	private final WeightMapper weightMapper;

	@Override
	public GuideResponse generateGuide(AnalysisData weightAnalysisData) {
		WeightAnalysisData data = (WeightAnalysisData)weightAnalysisData;
		String content = String.format("%s이에요. 평균 체중에 비해 %dkg %s", data.getAlert().getTitle(), data.getWeightDiff(),
			data.getWeightDiff() > 0 ? "많아요" : "적어요");
		WeightGuide weightGuide = weightMapper.toDocument(data, content);
		return weightMapper.toResponse(weightGuideRepository.save(weightGuide));
	}

	@Override
	public GroupCode getGroupCode() {
		return GroupCode.WEIGHT;
	}
}
