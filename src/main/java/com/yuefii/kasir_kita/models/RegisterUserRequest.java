package com.yuefii.kasir_kita.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {

  @NotBlank
  private String name;

  @NotBlank
  private String username;

  @NotBlank
  private String password;
}
