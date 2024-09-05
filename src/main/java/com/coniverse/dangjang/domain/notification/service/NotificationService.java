package com.coniverse.dangjang.domain.notification.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coniverse.dangjang.domain.notification.dto.message.FcmMessage;
import com.coniverse.dangjang.domain.notification.dto.request.CheckNotificationIdRequest;
import com.coniverse.dangjang.domain.notification.dto.response.NotificationResponse;
import com.coniverse.dangjang.domain.notification.entity.Notification;
import com.coniverse.dangjang.domain.notification.entity.NotificationType;
import com.coniverse.dangjang.domain.notification.entity.UserFcmToken;
import com.coniverse.dangjang.domain.notification.enums.FCMContent;
import com.coniverse.dangjang.domain.notification.exception.InvalidFcmTokenException;
import com.coniverse.dangjang.domain.notification.mapper.NotificationMapper;
import com.coniverse.dangjang.domain.notification.repository.NotificationRepository;
import com.coniverse.dangjang.domain.notification.repository.UserFcmTokenRepository;
import com.coniverse.dangjang.domain.user.dto.request.PostFcmTokenRequest;
import com.coniverse.dangjang.domain.user.entity.User;
import com.coniverse.dangjang.domain.user.service.UserSearchService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Notification 관련 Service
 *
 * @author EVE
 * @since 1.1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
	private final UserFcmTokenRepository userFcmTokenRepository;
	private final NotificationMapper notificationMapper;
	private final UserSearchService userSearchService;
	private final NotificationRepository notificationRepository;
	private final NotificationSearchService notificationSearchService;

	/**
	 * 유저의 알림 목록을 조회한다
	 *
	 * @param oauthId 사용자 oauthId
	 * @return List<NotificationResponse> 알림 목록
	 * @since 1.1.0
	 */
	public List<NotificationResponse> getNotificationList(String oauthId) {
		return notificationRepository.findAllByOauthIdAndRead(oauthId).stream()
			.map(notificationMapper::toResponse).collect(Collectors.toList());
	}

	/**
	 * 알림 확인 여부(isRead)를 true로 변경한다
	 *
	 * @param notificationIdList 알림Id 목록
	 * @since 1.1.0
	 */
	public void updateNotificationIsRead(CheckNotificationIdRequest notificationIdList) {
		//Todo : update 로직 수정 필요
		notificationIdList.notificationIdList().forEach(notificationId -> {
			Optional<Notification> notification = notificationRepository.findById(notificationId);
			if (notification.isPresent() && !notification.get().isRead()) {
				notification.get().setRead();
				notificationRepository.save(notification.get());
			}
		});
	}

	/**
	 * 확인 안한 알림이 있는지 조회한다.
	 *
	 * @param oauthId 사용자 oauthId
	 * @return Boolean 읽지 않은 알림이 있으면 true, 없으면 false 반환
	 * @since 1.1.0
	 */
	public Boolean isExistsNotReadNotification(String oauthId) {
		return notificationRepository.isExistsNotReadNotification(oauthId);
	}

	/**
	 * fcmToken 저장
	 * <p>
	 * 기존에 있는 fcm 토큰이라면 업데이트하고, 없다면 새로 저장한다.
	 *
	 * @param request fcmToken 요청
	 * @param oauthId 사용자 아이디
	 * @since 1.1.0
	 */
	public void saveOrUpdateFcmToken(PostFcmTokenRequest request, String oauthId) {
		Optional<UserFcmToken> existFcmToken = userFcmTokenRepository.findUserFcmTokenByFcmId(oauthId, request.deviceId());
		if (!existFcmToken.isEmpty()) {
			updateFcmToken(existFcmToken.get(), request.fcmToken());
			return;
		}
		saveFcmToken(request, oauthId);
	}

	/**
	 * fcmToken 업데이트
	 *
	 * @param userFcmToken 기존의 userFcmToken
	 * @param newFcmToken  새로운 fcmToken
	 * @since 1.3.0
	 */
	private void updateFcmToken(UserFcmToken userFcmToken, String newFcmToken) {
		userFcmToken.setFcmToken(newFcmToken);
	}

	/**
	 * fcmToken 저장
	 *
	 * @param request 요청
	 * @param oauthId 사용자 아이디
	 * @since 1.3.0
	 */
	private void saveFcmToken(PostFcmTokenRequest request, String oauthId) {
		User user = userSearchService.findUserByOauthId(oauthId);
		UserFcmToken userFcmToken = notificationMapper.toEntity(user, request.fcmToken(), request.deviceId());
		userFcmTokenRepository.save(userFcmToken);
	}

	/**
	 * fcmToken 제거
	 *
	 * @param fcmToken fcmToken
	 * @throws InvalidFcmTokenException fcmToken이 존재하지 않을 경우 발생하는 예외
	 * @since 1.1.0
	 */
	public void deleteFcmToken(String fcmToken) {
		userFcmTokenRepository.deleteByFcmToken(fcmToken);
	}

	/**
	 * accessFcmMessage 생성
	 *
	 * @return FcmMessage
	 * @since 1.1.0
	 */
	public List<FcmMessage> makeAccessFcmMessage() {

		LocalDate date = LocalDate.now();
		List<UserFcmToken> userFcmTokens = notificationSearchService.findNotAccessUserFcmToken(date);
		NotificationType notificationType = notificationSearchService.findNotificationType("접속");
		notificationRepository.saveAll(generateNotifications(userFcmTokens, date, notificationType));
		return generateFcmMessage(userFcmTokens, date);
	}

	/**
	 * notification Entity 생성
	 *
	 * @param userFcmTokens : 토큰 리스트 , date : 보내는 날짜 , type : notification 타입
	 * @return List<Notification> Notification Entity 리스트
	 * @since 1.7.0
	 */
	public List<Notification> generateNotifications(List<UserFcmToken> userFcmTokens, LocalDate date, NotificationType type) {
		return userFcmTokens.stream()
			.map(fcmToken -> {
				//int compareDate = (int)ChronoUnit.DAYS.between(fcmToken.getUser().getAccessedAt(), date);
				// TODO : compareDate 수정 필요
				int compareDate = 0;
				return notificationMapper.toEntity(fcmToken.getUser(), FCMContent.TITLE.getTitle(compareDate), FCMContent.BODY.getBody(compareDate), date,
					type);
			})
			.collect(Collectors.toList());
	}

	/**
	 * 전달할 FcmMessage 생성
	 *
	 * @param userFcmTokens : 토큰 리스트 , date : 보내는 날짜
	 * @return List<FcmMessage> FcmMessage 리스트
	 * @since 1.7.0
	 */
	public List<FcmMessage> generateFcmMessage(List<UserFcmToken> userFcmTokens, LocalDate date) {
		return userFcmTokens.stream()
			.map(userFcmToken -> {
				// int compareDate = (int)ChronoUnit.DAYS.between(userFcmToken.getUser().getAccessedAt(), date);
				// TODO : compareDate 수정 필요
				int compareDate = 0;
				return new FcmMessage(userFcmToken.getFcmToken(), FCMContent.TITLE.getTitle(compareDate), FCMContent.BODY.getBody(compareDate));
			})
			.collect(Collectors.toList());
	}
}
