package com.yuefii.kasir_kita.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yuefii.kasir_kita.entity.User;
import com.yuefii.kasir_kita.models.LoginUserRequest;
import com.yuefii.kasir_kita.models.TokenResponse;
import com.yuefii.kasir_kita.repositories.UserRepository;
import com.yuefii.kasir_kita.security.Bcrypt;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ValidationService validationService;

  @Transactional
  public TokenResponse login(LoginUserRequest request) {
    validationService.validate(request);

    User user = userRepository.findById(request.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password wrong"));

    if (Bcrypt.checkpw(request.getPassword(), user.getPassword())) {
      user.setToken(UUID.randomUUID().toString());
      user.setTokenExpiredAt(monthToMillis());
      userRepository.save(user);

      return TokenResponse.builder()
          .token(user.getToken())
          .ExpiredAt(user.getTokenExpiredAt())
          .build();
    } else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password wrong");
    }
  }

  private Long monthToMillis() {
    return System.currentTimeMillis() + (1000 * 16 * 24 * 30);
  }

}
