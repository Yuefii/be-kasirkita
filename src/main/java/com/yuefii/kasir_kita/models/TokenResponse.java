package com.yuefii.kasir_kita.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TokenResponse {

  private String token;

  private Long ExpiredAt;

}
