package com;

import org.springframework.data.jpa.repository.JpaRepository;

public interface walletRepository extends JpaRepository<wallet,Integer> {
       wallet findByuserName(String userName);
}
