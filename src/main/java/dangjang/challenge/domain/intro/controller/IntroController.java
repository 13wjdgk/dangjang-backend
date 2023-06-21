package dangjang.challenge.domain.intro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dangjang.challenge.domain.intro.service.IntroService;
import dangjang.challenge.global.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/intro")
@RequiredArgsConstructor
public class IntroController {
	private final IntroService introService;

	@GetMapping
	public ResponseEntity<SuccessResponse> getIntro() {
		return ResponseEntity.ok().body(introService.getIntro());
	}
}
