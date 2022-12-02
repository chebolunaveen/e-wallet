package com;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.XGroupCreateArgs;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class walletService {


    @Autowired
    walletRepository walletRepository;
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @KafkaListener(topics ={"create_wallet"},groupId = "friends_group")
    public void createWallet(String message) throws JsonProcessingException {
        JSONObject walletRequest=objectMapper.readValue(message,JSONObject.class);//convert message from sender to object

        String userName= (String) walletRequest.get("userName");
        //creating wallet
     wallet wallet1=wallet.builder().userName(userName).amount(0).build();

     //we are saving the wallet repository
        walletRepository.save(wallet1);
    }
//    wallet increment(String userName,int amount){
//        //method 1:
//        wallet wallet1=walletRepository.findByuserName(userName);
//        int newAmount=wallet1.getAmount()+amount;
//        int originalId=wallet1.getId();
//        wallet updateWallet=wallet.builder().id(originalId).userName(userName).amount(newAmount).build();
//        walletRepository.save(updateWallet);
//        return updateWallet;
//
//        //method 2 by using query
//        //int success=walletRepository.updatewallet(userName,amount);
//
//    }
//    wallet decrement(String userName,int amount){
//        wallet wallet1=walletRepository.findByuserName(userName);
//        int newAmount=wallet1.getAmount()-amount;
//        int originalId=wallet1.getId();
//        wallet updateWallet=wallet.builder().id(originalId).userName(userName).amount(newAmount).build();
//        walletRepository.save(updateWallet);
//        return updateWallet;
//    }

}
