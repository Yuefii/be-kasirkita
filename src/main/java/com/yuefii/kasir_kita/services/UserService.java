package com.yuefii.kasir_kita.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yuefii.kasir_kita.dto.RegisterUserRequest;
import com.yuefii.kasir_kita.dto.UpdateUserRequest;
import com.yuefii.kasir_kita.dto.UserResponse;
import com.yuefii.kasir_kita.models.User;
import com.yuefii.kasir_kita.repositories.UserRepository;
import com.yuefii.kasir_kita.security.Bcrypt;

import jakarta.transaction.Transactional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ValidationService validationService;

  @Transactional
  public void register(RegisterUserRequest request) {

    validationService.validate(request);

    if (userRepository.existsById(request.getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already registered.");
    }

    String role = (request.getRole() != null) ? request.getRole() : "cashier";

    User user = new User();
    user.setName(request.getName());
    user.setUsername(request.getUsername());
    user.setPassword(Bcrypt.hashpw(request.getPassword(), Bcrypt.gensalt()));
    user.setRole(role);

    userRepository.save(user);
  }

  public UserResponse get(User user) {
    return UserResponse.builder()
        .name(user.getName())
        .username(user.getUsername())
        .role(user.getRole())
        .build();
  }

  @Transactional
  public UserResponse update(User user, UpdateUserRequest request) {

    validationService.validate(request);

    if (Objects.nonNull(request.getName())) {
      user.setName(request.getName());
    }

    if (Objects.nonNull(request.getRole())) {
      user.setRole(request.getRole());
    }

    userRepository.save(user);

    return UserResponse.builder()
        .name(user.getName())
        .username(user.getUsername())
        .role(user.getRole())
        .build();
  }
}
