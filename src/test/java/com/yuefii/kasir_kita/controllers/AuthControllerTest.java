package com.yuefii.kasir_kita.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuefii.kasir_kita.dto.LoginUserRequest;
import com.yuefii.kasir_kita.dto.TokenResponse;
import com.yuefii.kasir_kita.dto.WebResponse;
import com.yuefii.kasir_kita.models.User;
import com.yuefii.kasir_kita.repositories.UserRepository;
import com.yuefii.kasir_kita.security.Bcrypt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void failedToLoginUserNotFound() throws Exception {
    LoginUserRequest request = new LoginUserRequest();
    request.setUsername("jhondoe");
    request.setPassword("rahasia");

    mockMvc.perform(
        post("/api/auth/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpectAll(
            status().isUnauthorized())
        .andDo(result -> {
          WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<>() {
              });
          assertNotNull(response.getErrors());
        });
  }

  @Test
  void failedToLoginWrongPassword() throws Exception {
    User user = new User();
    user.setName("jhondoe");
    user.setUsername("jhondoe");
    user.setPassword(Bcrypt.hashpw("rahasia", Bcrypt.gensalt()));
    userRepository.save(user);

    LoginUserRequest request = new LoginUserRequest();
    request.setUsername("jhondoe");
    request.setPassword("wrongpassword");

    mockMvc.perform(
        post("/api/auth/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpectAll(
            status().isUnauthorized())
        .andDo(result -> {
          WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<>() {
              });
          assertNotNull(response.getErrors());
        });
  }

  @Test
  void loginSuccess() throws Exception {
    User user = new User();
    user.setName("Jhon Doe");
    user.setUsername("jhondoe");
    user.setPassword(Bcrypt.hashpw("rahasia", Bcrypt.gensalt()));
    userRepository.save(user);

    LoginUserRequest request = new LoginUserRequest();
    request.setUsername("jhondoe");
    request.setPassword("rahasia");

    mockMvc.perform(
        post("/api/auth/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpectAll(
            status().isOk())
        .andDo(result -> {
          WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<>() {
              });
          assertNull(response.getErrors());
          assertNotNull(response.getData().getToken());
          assertNotNull(response.getData().getExpiredAt());

          User userDb = userRepository.findById("jhondoe").orElse(null);
          assertNotNull(userDb);
          assertEquals(userDb.getToken(), response.getData().getToken());
          assertEquals(userDb.getTokenExpiredAt(), response.getData().getExpiredAt());
        });
  }

  @Test
  void logoutFailed() throws Exception {
    mockMvc.perform(
        delete("/api/auth/logout")
            .accept(MediaType.APPLICATION_JSON))
        .andExpectAll(
            status().isUnauthorized())
        .andDo(result -> {
          WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<>() {
              });
          assertNotNull(response.getErrors());
        });
  }

  @Test
  void logoutSuccess() throws Exception {
    User user = new User();
    user.setName("Jhon Doe");
    user.setUsername("jhondoe");
    user.setPassword(Bcrypt.hashpw("rahasia", Bcrypt.gensalt()));
    user.setToken("test");
    user.setTokenExpiredAt(System.currentTimeMillis() + 10000000L);
    userRepository.save(user);

    mockMvc.perform(
        delete("/api/auth/logout")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "test"))
        .andExpectAll(
            status().isOk())
        .andDo(result -> {
          WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<>() {
              });
          assertNull(response.getErrors());
          assertEquals("successfull", response.getData());

          User userDb = userRepository.findById("jhondoe").orElse(null);
          assertNotNull(userDb);
          assertNull(userDb.getTokenExpiredAt());
          assertNull(userDb.getToken());
        });
  }

}
