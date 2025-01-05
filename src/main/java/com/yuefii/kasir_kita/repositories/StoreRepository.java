package com.yuefii.kasir_kita.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuefii.kasir_kita.models.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {
}
