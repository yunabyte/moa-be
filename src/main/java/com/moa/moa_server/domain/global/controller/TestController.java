package com.moa.moa_server.domain.global.controller;

import com.moa.moa_server.domain.ai.client.AiModerationClient;
import com.moa.moa_server.domain.ai.dto.ModerationRequest;
import com.moa.moa_server.domain.ai.dto.ModerationResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test", description = "테스트용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

  private final AiModerationClient aiModerationClient;

  // 누구나 호출 가능한 헬스체크용
  @Hidden
  @Operation(summary = " 서버 헬스 체크", description = "누구나 호출 가능한 ping API")
  @GetMapping("/ping")
  public ResponseEntity<String> ping() {
    return ResponseEntity.ok("pong");
  }

  // AI 서버 연동 테스트
  @Hidden
  @Operation(
      summary = "AI 서버 연동 테스트",
      description = "AI 서버에 문장을 보내 검열 결과를 받는 테스트용 API",
      responses = {
        @ApiResponse(responseCode = "200", description = "연동 성공"),
        @ApiResponse(responseCode = "502", description = "AI 서버 연동 실패")
      })
  @GetMapping("/ping-ai")
  public ResponseEntity<String> pingAi() {
    try {
      ModerationRequest request = new ModerationRequest(1L, "테스트 문장입니다.");
      ModerationResponse response = aiModerationClient.requestModeration(request);
      return ResponseEntity.ok(response.toString());
    } catch (Exception e) {
      return ResponseEntity.status(502) // Bad Gateway: 외부 연동 실패
          .body("AI 서버 요청 실패: " + e.getMessage());
    }
  }

  // 토큰이 있어야 호출 가능한 테스트용
  @Hidden
  @Operation(
      summary = "토큰 인증 테스트",
      description = "인증된 사용자만 호출 가능한 테스트용 API",
      security = @SecurityRequirement(name = "bearer-key"))
  @PostMapping("/auth")
  public ResponseEntity<String> authTest(@AuthenticationPrincipal Long userId) {
    return ResponseEntity.ok("Authenticated user ID: " + userId);
  }
}
