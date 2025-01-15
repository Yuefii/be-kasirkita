package com.yuefii.kasir_kita.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuefii.kasir_kita.models.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {

  Optional<Store> findFirstByUserUsername(String username);

  Optional<Store> findFirstByUserUsernameAndStoreID(String username, String storeID);

}
