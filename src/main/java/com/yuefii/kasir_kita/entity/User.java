package com.yuefii.kasir_kita.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

public class User {

  private String name;
  @Id
  private String username;
  private String password;
  private String role;
  private String token;
  @Column(name = "token_expired_at")
  private Long TokenExpiredAt;

}
