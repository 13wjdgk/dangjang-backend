package com.coniverse.dangjang.domain.user.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * UserAccess Entity
 * <p>
 * 사용자 접속 날짜를 저장한다.
 *
 * @since 1.6.0
 */
@Entity
@Getter
@NoArgsConstructor
public class UserAccess {
	private String oauthId;
	private LocalDate lastAccessDate;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Builder
	public UserAccess(String oauthId, LocalDate lastAccessDate, Long id) {
		this.oauthId = oauthId;
		this.lastAccessDate = lastAccessDate;
		this.id = id;
	}

}
