package com.yuefii.kasir_kita.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yuefii.kasir_kita.dto.LoginUserRequest;
import com.yuefii.kasir_kita.dto.TokenResponse;
import com.yuefii.kasir_kita.dto.WebResponse;
import com.yuefii.kasir_kita.services.AuthService;

@RestController
public class AuthController {
  @Autowired
  private AuthService authService;

  @PostMapping(path = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
    TokenResponse tokenResponse = authService.login(request);
    return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
  }
}
