package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class walletService {

    @Autowired
    walletRepository walletRepository;
    public void createWallet(String userName){
     wallet wallet1=wallet.builder().userName(userName).amount(0).build();
        walletRepository.save(wallet1);
    }
    wallet increment(String userName,int amount){
        //method 1:
        wallet wallet1=walletRepository.findByuserName(userName);
        int newAmount=wallet1.getAmount()+amount;
        int originalId=wallet1.getId();
        wallet updateWallet=wallet.builder().id(originalId).userName(userName).amount(newAmount).build();
        walletRepository.save(updateWallet);
        return updateWallet;

    }
    wallet decrement(String userName,int amount){
        wallet wallet1=walletRepository.findByuserName(userName);
        int newAmount=wallet1.getAmount()-amount;
        int originalId=wallet1.getId();
        wallet updateWallet=wallet.builder().id(originalId).userName(userName).amount(newAmount).build();
        walletRepository.save(updateWallet);
        return updateWallet;
    }
}
