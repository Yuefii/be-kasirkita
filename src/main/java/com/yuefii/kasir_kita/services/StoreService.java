package com.yuefii.kasir_kita.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuefii.kasir_kita.dto.CreateStoreRequest;
import com.yuefii.kasir_kita.dto.StoreResponse;
import com.yuefii.kasir_kita.models.Store;
import com.yuefii.kasir_kita.models.User;
import com.yuefii.kasir_kita.repositories.StoreRepository;

import jakarta.transaction.Transactional;

@Service
public class StoreService {

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private ValidationService validationService;

  private StoreResponse toResponse(Store store) {
    return StoreResponse.builder()
        .storeName(store.getStoreName())
        .storeAddress(store.getStoreAddress())
        .storePhone(store.getStorePhone())
        .storeEmail(store.getStoreEmail())
        .build();
  }

  @Transactional
  public StoreResponse create(User user, CreateStoreRequest request) {

    validationService.validate(request);

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
}
