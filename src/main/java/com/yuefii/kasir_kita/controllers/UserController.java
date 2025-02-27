package com.yuefii.kasir_kita.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yuefii.kasir_kita.dto.ChangePasswordRequest;
import com.yuefii.kasir_kita.dto.RegisterUserRequest;
import com.yuefii.kasir_kita.dto.UpdateUserRequest;
import com.yuefii.kasir_kita.dto.UserResponse;
import com.yuefii.kasir_kita.dto.WebResponse;
import com.yuefii.kasir_kita.models.User;
import com.yuefii.kasir_kita.services.AuthService;
import com.yuefii.kasir_kita.services.UserService;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private AuthService authService;

  @PostMapping(path = "/api/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
    userService.register(request);
    return WebResponse.<String>builder().data("successfull").build();
  }

  @GetMapping(path = "/api/users/current", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserResponse> get(User user) {
    UserResponse userResponse = userService.get(user);
    return WebResponse.<UserResponse>builder().data(userResponse).build();
  }

  @PatchMapping(path = "/api/users/current", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserResponse> update(User user, @RequestBody UpdateUserRequest request) {
    UserResponse userResponse = userService.update(user, request);
    return WebResponse.<UserResponse>builder().data(userResponse).build();
  }

  @PatchMapping(path = "/api/users/change-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> changePassword(User user, @RequestBody ChangePasswordRequest request) {
    userService.changePassword(user, request);
    return WebResponse.<String>builder().data("successfull").build();
  }

  @DeleteMapping(path = "/api/auth/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> logout(User user) {
    authService.logout(user);
    return WebResponse.<String>builder().data("successfull").build();
  }
}
