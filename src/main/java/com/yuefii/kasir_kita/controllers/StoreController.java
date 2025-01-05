package com.yuefii.kasir_kita.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yuefii.kasir_kita.dto.CreateStoreRequest;
import com.yuefii.kasir_kita.dto.StoreResponse;
import com.yuefii.kasir_kita.dto.WebResponse;
import com.yuefii.kasir_kita.models.User;
import com.yuefii.kasir_kita.services.StoreService;

@RestController
public class StoreController {

  @Autowired
  private StoreService storeService;

  @PostMapping(path = "/api/store", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<StoreResponse> create(User user, @RequestBody CreateStoreRequest request) {
    StoreResponse storeResponse = storeService.create(user, request);
    return WebResponse.<StoreResponse>builder().data(storeResponse).build();
  }

}
