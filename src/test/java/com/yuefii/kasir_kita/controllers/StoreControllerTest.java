package com.yuefii.kasir_kita.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuefii.kasir_kita.dto.CreateStoreRequest;
import com.yuefii.kasir_kita.dto.StoreResponse;
import com.yuefii.kasir_kita.dto.WebResponse;
import com.yuefii.kasir_kita.models.Store;
import com.yuefii.kasir_kita.models.User;
import com.yuefii.kasir_kita.repositories.StoreRepository;
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

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    storeRepository.deleteAll();
    userRepository.deleteAll();

    User user = new User();
    user.setUsername("test");
    user.setPassword(Bcrypt.hashpw("test", Bcrypt.gensalt()));
    user.setRole("admin");
    user.setName("Test");
    user.setToken("test");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000);
    userRepository.save(user);
  }

  @Test
  void createContactBadRequest() throws Exception {
    CreateStoreRequest request = new CreateStoreRequest();
    request.setStoreName("");
    request.setStoreEmail("salah");

    mockMvc.perform(
        post("/api/store")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("Authorization", "test"))
        .andExpectAll(
            status().isBadRequest())
        .andDo(result -> {
          WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<WebResponse<String>>() {
              });
          assertNotNull(response.getErrors());
        });
  }

  @Test
  void createStoreSuccess() throws Exception {
    CreateStoreRequest request = new CreateStoreRequest();
    request.setStoreName("Test Store");
    request.setStoreAddress("Tangerang");
    request.setStoreEmail("test@example.com");
    request.setStorePhone("42342342344");

    mockMvc.perform(
        post("/api/store")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("Authorization", "test"))
        .andExpectAll(
            status().isOk())
        .andDo(result -> {
          WebResponse<StoreResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<>() {
              });
          assertNull(response.getErrors());
          assertEquals("Test Store", response.getData().getStoreName());
          assertEquals("Tangerang", response.getData().getStoreAddress());
          assertEquals("test@example.com", response.getData().getStoreEmail());
          assertEquals("42342342344", response.getData().getStorePhone());
        });
  }

  @Test
  void getStoreNotFound() throws Exception {
    mockMvc.perform(
        get("/api/store")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "test"))
        .andExpectAll(
            status().isNotFound())
        .andDo(result -> {
          WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<WebResponse<String>>() {
              });
          assertNotNull(response.getErrors());
        });
  }

  @Test
  void getStoreSuccess() throws Exception {
    User user = userRepository.findById("test").orElseThrow();

    Store store = new Store();
    store.setStoreID(UUID.randomUUID().toString());
    store.setUser(user);
    store.setStoreName("Test Store");
    store.setStoreAddress("Jakarta");
    store.setStoreEmail("test@example.com");
    store.setStorePhone("08888121212");
    storeRepository.save(store);

    mockMvc.perform(
        get("/api/store")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "test"))
        .andExpectAll(
            status().isOk())
        .andDo(result -> {
          WebResponse<StoreResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<>() {
              });
          assertNull(response.getErrors());

          assertEquals(store.getStoreID(), response.getData().getStoreID());
          assertEquals(store.getStoreName(), response.getData().getStoreName());
          assertEquals(store.getStoreAddress(), response.getData().getStoreAddress());
          assertEquals(store.getStoreEmail(), response.getData().getStoreEmail());
          assertEquals(store.getStorePhone(), response.getData().getStorePhone());
        });
  }
}
