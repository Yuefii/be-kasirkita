package com.yuefii.kasir_kita.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserRequest {

  @NotBlank
  private String username;

  @NotBlank
  private String password;

}
