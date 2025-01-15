package com.yuefii.kasir_kita.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeResponse {

  private String name;

  private String username;

  private Timestamp hireDate;
}
