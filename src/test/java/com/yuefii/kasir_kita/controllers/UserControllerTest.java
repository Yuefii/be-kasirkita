package com.yuefii.kasir_kita.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuefii.kasir_kita.dto.ChangePasswordRequest;
import com.yuefii.kasir_kita.dto.RegisterUserRequest;
import com.yuefii.kasir_kita.dto.UpdateUserRequest;
import com.yuefii.kasir_kita.dto.UserResponse;
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
public class UserControllerTest {

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
    void testRegisterSuccess() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("Jhon Doe");
        request.setUsername("jhondoe");
        request.setPassword("rahasia");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertEquals("successfull", response.getData());
                });
    }

    @Test
    void testRegisterDuplicate() throws Exception {
        User user = new User();
        user.setName("Jhon Doe");
        user.setUsername("jhondoe");
        user.setPassword(Bcrypt.hashpw("rahasia", Bcrypt.gensalt()));
        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("Jhon Doe");
        request.setUsername("jhondoe");
        request.setPassword("rahasia");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void getUserUnauthorized() throws Exception {
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "notfound"))
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
    void getUserUnauthorizedTokenNotSend() throws Exception {
        mockMvc.perform(
                get("/api/users/current")
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
    void getUserTokenExpired() throws Exception {
        User user = new User();
        user.setName("Jhon Doe");
        user.setUsername("jhondoe");
        user.setPassword(Bcrypt.hashpw("rahasia", Bcrypt.gensalt()));
        user.setRole("cashier");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() - 10000000);
        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "test"))
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
    void getUserSuccess() throws Exception {
        User user = new User();
        user.setName("Jhon Doe");
        user.setUsername("jhondoe");
        user.setPassword(Bcrypt.hashpw("rahasia", Bcrypt.gensalt()));
        user.setRole("cashier");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<UserResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals("Jhon Doe", response.getData().getName());
                    assertEquals("jhondoe", response.getData().getUsername());
                    assertEquals("cashier", response.getData().getRole());
                });
    }

    @Test
    void updateUserUnauthorized() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();

        mockMvc.perform(
                patch("/api/users/current")
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
    void updateUserSuccess() throws Exception {
        User user = new User();
        user.setName("Jhon Doe");
        user.setUsername("jhondoe");
        user.setPassword(Bcrypt.hashpw("rahasia", Bcrypt.gensalt()));
        user.setRole("cashier");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000000000L);
        userRepository.save(user);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Jhon Doe Update");
        request.setRole("admin");

        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<UserResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals("Jhon Doe Update", response.getData().getName());
                    assertEquals("jhondoe", response.getData().getUsername());
                    assertEquals("admin", response.getData().getRole());

                    User userDb = userRepository.findById("jhondoe").orElse(null);
                    assertNotNull(userDb);
                });
    }

    @Test
    void changePasswordSuccess() throws Exception {
        User user = new User();
        user.setName("Jhon Doe");
        user.setUsername("jhondoe");
        user.setPassword(Bcrypt.hashpw("rahasia", Bcrypt.gensalt()));
        user.setRole("cashier");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000000000L);
        userRepository.save(user);

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("rahasia");
        request.setNewPassword("rahasia2323");
        request.setConfirmPassword("rahasia2323");

        mockMvc.perform(
                patch("/api/users/change-password")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                        WebResponse<String> response = objectMapper.readValue(
                                        result.getResponse().getContentAsString(),
                                        new TypeReference<>() {
                                        });
                    assertEquals("successfull", response.getData());
                });
    }

}
