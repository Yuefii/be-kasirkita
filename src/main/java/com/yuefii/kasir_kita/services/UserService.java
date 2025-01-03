package com.yuefii.kasir_kita.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yuefii.kasir_kita.dto.RegisterUserRequest;
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

    User user = new User();
    user.setName(request.getName());
    user.setUsername(request.getUsername());
    user.setPassword(Bcrypt.hashpw(request.getPassword(), Bcrypt.gensalt()));
    user.setRole("cashier");

    userRepository.save(user);
  }
}
