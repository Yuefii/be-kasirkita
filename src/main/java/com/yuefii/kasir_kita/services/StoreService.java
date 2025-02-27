package com.yuefii.kasir_kita.services;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yuefii.kasir_kita.dto.CreateStoreRequest;
import com.yuefii.kasir_kita.dto.StoreResponse;
import com.yuefii.kasir_kita.dto.UpdateStoreRequest;
import com.yuefii.kasir_kita.models.Store;
import com.yuefii.kasir_kita.models.User;
import com.yuefii.kasir_kita.repositories.StoreRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreService {

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private ValidationService validationService;

  private StoreResponse toResponse(Store store) {
    return StoreResponse.builder()
        .storeID(store.getStoreID())
        .storeName(store.getStoreName())
        .storeAddress(store.getStoreAddress())
        .storePhone(store.getStorePhone())
        .storeEmail(store.getStoreEmail())
        .build();
  }

  @Transactional
  public StoreResponse create(User user, CreateStoreRequest request) {

    validationService.validate(request);

    storeRepository.findFirstByUserUsername(user.getUsername())
        .ifPresent(_ -> {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "store already exists");
        });

    Store store = new Store();
    store.setStoreID(UUID.randomUUID().toString());
    store.setStoreName(request.getStoreName());
    store.setStoreAddress(request.getStoreAddress());
    store.setStoreEmail(request.getStoreEmail());
    store.setStorePhone(request.getStorePhone());
    store.setUser(user);

    storeRepository.save(store);

    return toResponse(store);
  }

  @Transactional(readOnly = true)
  public StoreResponse get(User user) {
    Store store = storeRepository.findFirstByUserUsername(user.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "store not found"));

    return toResponse(store);
  }

  @Transactional
  public StoreResponse update(User user, UpdateStoreRequest request) {
    validationService.validate(request);

    Store store = storeRepository.findFirstByUserUsername(user.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "store not found"));

    if (Objects.nonNull(request.getStoreName())) {
      store.setStoreName(request.getStoreName());
    }

    if (Objects.nonNull(request.getStoreAddress())) {
      store.setStoreAddress(request.getStoreAddress());
    }

    if (Objects.nonNull(request.getStoreEmail())) {
      store.setStoreEmail(request.getStoreEmail());
    }

    if (Objects.nonNull(request.getStorePhone())) {
      store.setStorePhone(request.getStorePhone());
    }

    storeRepository.save(store);

    return toResponse(store);
  }

  @Transactional
  public void delete(User user) {
    Store store = storeRepository.findFirstByUserUsername(user.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "store not found"));

    storeRepository.delete(store);
  }

}
