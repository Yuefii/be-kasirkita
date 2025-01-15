package com.yuefii.kasir_kita.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuefii.kasir_kita.dto.CreateEmployeRequest;
import com.yuefii.kasir_kita.dto.CreateStoreRequest;
import com.yuefii.kasir_kita.dto.EmployeResponse;
import com.yuefii.kasir_kita.dto.WebResponse;
import com.yuefii.kasir_kita.models.Store;
import com.yuefii.kasir_kita.models.User;
import com.yuefii.kasir_kita.repositories.EmployeRepository;
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

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmployeRepository employeRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    storeRepository.deleteAll();
    employeRepository.deleteAll();
    userRepository.deleteAll();

    User user = new User();
    user.setUsername("jhondoe");
    user.setPassword(Bcrypt.hashpw("rahasia", Bcrypt.gensalt()));
    user.setRole("admin");
    user.setName("Jhon Doe");
    user.setToken("test");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000);
    userRepository.save(user);

    Store store = new Store();
    store.setStoreID("testing");
    store.setStoreName("Jhondoe Store");
    store.setStoreAddress("Jakarta");
    store.setStoreEmail("store@example.com");
    store.setStorePhone("08888121212");
    store.setUser(user);
    storeRepository.save(store);
  }

  @Test
  void createEmployeBadRequest() throws Exception {
    CreateStoreRequest request = new CreateStoreRequest();
    request.setStoreName("");
    request.setStoreEmail("salah");

    mockMvc.perform(
        post("/api/store/testing/employe")
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
  void createEmployeSuccess() throws Exception {
    CreateEmployeRequest request = new CreateEmployeRequest();
    request.setName("Testing Employe");
    request.setUsername("employe");
    request.setPassword(Bcrypt.hashpw("rahasia", Bcrypt.gensalt()));

    mockMvc.perform(
        post("/api/store/testing/employe")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("Authorization", "test"))
        .andExpectAll(
            status().isOk())
        .andDo(result -> {
          WebResponse<EmployeResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<>() {
              });
          assertNull(response.getErrors());
          assertEquals(request.getName(), response.getData().getName());
          assertEquals(request.getUsername(), response.getData().getUsername());
        });
  }

}
