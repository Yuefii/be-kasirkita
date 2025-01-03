package com.yuefii.kasir_kita.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yuefii.kasir_kita.entity.User;
import com.yuefii.kasir_kita.models.RegisterUserRequest;
import com.yuefii.kasir_kita.repositories.UserRepository;
import com.yuefii.kasir_kita.security.Bcrypt;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private Validator validator;

  @Transactional
  public void register(RegisterUserRequest request) {
    Set<ConstraintViolation<RegisterUserRequest>> constraintViolations = validator.validate(request);

    if (constraintViolations.size() != 0) {
      throw new ConstraintViolationException(constraintViolations);
    }

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
