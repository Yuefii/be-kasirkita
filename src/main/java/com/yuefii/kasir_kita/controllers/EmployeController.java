package com.yuefii.kasir_kita.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yuefii.kasir_kita.dto.CreateEmployeRequest;
import com.yuefii.kasir_kita.dto.EmployeResponse;
import com.yuefii.kasir_kita.dto.WebResponse;
import com.yuefii.kasir_kita.models.User;
import com.yuefii.kasir_kita.services.EmployeService;

@RestController
public class EmployeController {

  @Autowired
  private EmployeService employeService;

  @PostMapping(path = "/api/store/{storeId}/employe", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<EmployeResponse> create(User user, @RequestBody CreateEmployeRequest request,
      @PathVariable("storeId") String storeID) {
    request.setStoreID(storeID);
    EmployeResponse response = employeService.create(user, request);
    return WebResponse.<EmployeResponse>builder().data(response).build();
  }

}
