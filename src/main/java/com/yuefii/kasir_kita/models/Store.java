package com.yuefii.kasir_kita.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "stores")
public class Store {

  @Id
  @Column(name = "store_id")
  private String storeID;

  @Column(name = "store_name")
  private String storeName;

  @Column(name = "store_address")
  private String storeAddress;

  @Column(name = "store_phone")
  private String storePhone;

  @Column(name = "store_email")
  private String storeEmail;

  @OneToOne
  @JoinColumn(name = "username", referencedColumnName = "username")
  private User user;
}
