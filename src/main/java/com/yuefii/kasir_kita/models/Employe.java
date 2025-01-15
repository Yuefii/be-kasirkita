package com.yuefii.kasir_kita.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
@Table(name = "employes")
public class Employe {

  private String name;

  @Id
  private String username;

  private String password;

  private String token;

  @Column(name = "token_expired_at")
  private Timestamp TokenExpiredAt;

  @Column(name = "hire_date")
  private Timestamp hireDate;

  @ManyToOne
  @JoinColumn(name = "store_id", referencedColumnName = "store_id")
  private Store store;

  public void setHireDateFromLocalDateTime(LocalDateTime localDateTime) {
    if (localDateTime != null) {
      this.hireDate = Timestamp.valueOf(localDateTime);
    }
  }

  @PrePersist
  public void setDefaultHireDate() {
    if (hireDate == null) {
      hireDate = Timestamp.valueOf(LocalDateTime.now());
    }
  }
}
