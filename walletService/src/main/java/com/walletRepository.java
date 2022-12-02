package com;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface walletRepository extends JpaRepository<wallet,Integer> {
       wallet findByuserName(String userName);

       //@Modifying
       //@Query("Select wallet w from wallets set w.amount=w.amount+ :amount where w.userName= :userName")
      // int updatewallet(String userName,int amount);
}



