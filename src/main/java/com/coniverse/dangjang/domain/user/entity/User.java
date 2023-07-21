package com.coniverse.dangjang.domain.user.entity;

import java.util.Date;

import com.coniverse.dangjang.domain.auth.dto.OauthProvider;
import com.coniverse.dangjang.domain.user.entity.enums.ActivityAmount;
import com.coniverse.dangjang.domain.user.entity.enums.Gender;
import com.coniverse.dangjang.domain.user.entity.enums.Status;
import com.coniverse.dangjang.global.support.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author EVE
 * @since 1.0
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
	@EmbeddedId
	private UserId userId;
	@Column(nullable = false, unique = true, length = 15)
	private String nickname;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 1)
	private Gender gender;
	@Column(nullable = false)
	private Date birthday;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ActivityAmount activityAmount;
	@Column(nullable = false, length = 3)
	private int height;
	@Column(nullable = false)
	private int recommendedCalorie;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	private String profileImagePath;

	@Builder
	private User(String oauthId, OauthProvider oauthProvider, String nickname, Gender gender, Date birthday, ActivityAmount activityAmount, int height,
		int recommendedCalorie, Status status,
		String profileImagePath) {
		this.userId = new UserId(oauthId, oauthProvider);
		this.nickname = nickname;
		this.gender = gender;
		this.birthday = birthday;
		this.activityAmount = activityAmount;
		this.height = height;
		this.recommendedCalorie = recommendedCalorie;
		this.status = status;
		this.profileImagePath = profileImagePath;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			return this.userId.equals(((User)obj).userId);
		}
		return false;
	}

	public String getOauthId() {
		return this.userId.getOauthId();
	}

	public OauthProvider getOauthProvider() {
		return this.userId.getOauthProvider();
	}
}
