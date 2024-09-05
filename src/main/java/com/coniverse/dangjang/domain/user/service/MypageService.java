package com.coniverse.dangjang.domain.user.service;

import org.springframework.stereotype.Service;

import com.coniverse.dangjang.domain.point.dto.response.UserPointResponse;
import com.coniverse.dangjang.domain.point.service.PointSearchService;
import com.coniverse.dangjang.domain.user.dto.response.MypageResponse;
import com.coniverse.dangjang.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

/**
 * Mypage Service
 *
 * @author EVE
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class MypageService {
	private final UserSearchService userSearchService;
	private final PointSearchService pointSearchService;

	/**
	 * Mypage에 필요한 정보를 조회한다.
	 *
	 * @param oauthId 사용자 아이디
	 * @return MypageResponse 사용자 닉네임과 포인트
	 * @since 1.0.0
	 */
	public MypageResponse getMypage(String oauthId) {
		User user = userSearchService.findUserByOauthId(oauthId);
		UserPointResponse userPoint = pointSearchService.findUserPoint(oauthId);
		return new MypageResponse(user.getNickname(), userPoint.getTotalPoint());
	}
}
