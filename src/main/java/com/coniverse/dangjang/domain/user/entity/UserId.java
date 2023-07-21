package com.coniverse.dangjang.domain.user.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserId implements Serializable {
	@Column(name = "oauth_id")
	private String oauthId;
	private String provider;
}
