package dangjang.challenge.domain.intro.controller;

import dangjang.challenge.domain.intro.service.IntroService;
import dangjang.challenge.global.dto.SuccessResponse;
import dangjang.challenge.global.dto.content.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/intro")
@RequiredArgsConstructor
public class IntroController {
	private final IntroService introService;

	@GetMapping
	public ResponseEntity<SuccessResponse> getIntro() {
		Content content = introService.getIntro();
		return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK.getReasonPhrase(), content));
	}
}
