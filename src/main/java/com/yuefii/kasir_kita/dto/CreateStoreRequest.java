package com.yuefii.kasir_kita.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStoreRequest {

  @NotBlank
  private String storeName;

  private String storeAddress;

  private String storePhone;

  @Email
  private String storeEmail;
}
