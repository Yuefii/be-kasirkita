package com.yuefii.kasir_kita.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreResponse {

  private String storeID;

  private String storeName;

  private String storeAddress;

  private String storePhone;

  private String storeEmail;

}
