package com.yuefii.kasir_kita.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStoreRequest {

  private String storeName;

  private String storeAddress;

  private String storePhone;

  @Email
  private String storeEmail;
}
