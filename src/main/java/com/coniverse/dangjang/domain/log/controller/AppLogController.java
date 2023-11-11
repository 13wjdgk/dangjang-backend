package com.coniverse.dangjang.domain.log.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coniverse.dangjang.domain.log.dto.request.LogRequest;
import com.coniverse.dangjang.domain.log.service.LogService;
import com.coniverse.dangjang.global.dto.SuccessSingleResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * app log controller
 *
 * @author TEO
 * @since 1.0.0
 */
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class AppLogController {
	private final LogService logService;

	/**
	 * 앱 로그를 post 요청한다.
	 *
	 * @since 1.0.0
	 */
	@PostMapping
	public ResponseEntity<SuccessSingleResponse<?>> post(@Valid @RequestBody LogRequest request) {
		logService.sendLog(request);
		return ResponseEntity.ok(new SuccessSingleResponse<>(HttpStatus.OK.getReasonPhrase(), null));
	}
}
