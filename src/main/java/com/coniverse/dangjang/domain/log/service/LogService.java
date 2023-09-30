package com.coniverse.dangjang.domain.log.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.coniverse.dangjang.domain.log.dto.app.AppLog;
import com.coniverse.dangjang.domain.log.dto.request.LogRequest;
import com.coniverse.dangjang.domain.log.mapper.LogMapper;
import com.coniverse.dangjang.domain.user.entity.User;
import com.coniverse.dangjang.domain.user.service.UserSearchService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * log service
 *
 * @author TEO
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {
	private final RestTemplate restTemplate;
	@Value("${fluentbit.host}")
	private String host;
	@Value("${fluentbit.app-port}")
	private String port;
	private String url;
	private final UserSearchService userSearchService;
	private final LogMapper logMapper;

	@PostConstruct
	private void setUrl() {
		this.url = "http://" + host + ":" + port + "/app.log";
	}

	/**
	 * send app log to ETL
	 *
	 * @since 1.0.0
	 */
	public void sendLog(LogRequest request, String oauthId) {
		User user = userSearchService.findUserByOauthId(oauthId);
		AppLog appLog = logMapper.toAppLog(request, user);
		try {
			restTemplate.postForEntity(url, appLog, String.class);
		} catch (ResourceAccessException e) {
			log.error("fluentbit app.log port dead");
		}
	}
}
