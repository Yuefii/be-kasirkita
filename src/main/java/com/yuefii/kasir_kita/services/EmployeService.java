package com.yuefii.kasir_kita.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yuefii.kasir_kita.dto.CreateEmployeRequest;
import com.yuefii.kasir_kita.dto.EmployeResponse;
import com.yuefii.kasir_kita.models.Employe;
import com.yuefii.kasir_kita.models.Store;
import com.yuefii.kasir_kita.models.User;
import com.yuefii.kasir_kita.repositories.EmployeRepository;
import com.yuefii.kasir_kita.repositories.StoreRepository;
import com.yuefii.kasir_kita.security.Bcrypt;

import jakarta.transaction.Transactional;

@Service
public class EmployeService {

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private EmployeRepository employeRepository;

  @Autowired
  private ValidationService validationService;

  private EmployeResponse toResponse(Employe employe) {
    return EmployeResponse.builder()
        .name(employe.getName())
        .username(employe.getUsername())
        .hireDate(employe.getHireDate())
        .build();
  }

  @Transactional
  public EmployeResponse create(User user, CreateEmployeRequest request) {
    validationService.validate(request);

    Store store = storeRepository.findFirstByUserUsernameAndStoreID(user
        .getUsername(), request.getStoreID())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "store not found"));

    Employe employe = new Employe();

    employe.setStore(store);

    employe.setName(request.getName());

    employe.setUsername(request.getUsername());

    employe.setPassword(Bcrypt.hashpw(request.getPassword(), Bcrypt.gensalt()));

    employe.setHireDateFromLocalDateTime(LocalDateTime.now());

    employeRepository.save(employe);

    return toResponse(employe);
  }

}
