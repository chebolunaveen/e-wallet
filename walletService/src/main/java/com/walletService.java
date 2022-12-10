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
  @KafkaListener(topics={"updatewallet"},groupId = "friends_group")
    public void  updateWallet(String message) throws JsonProcessingException {
        JSONObject updatewallet=objectMapper.readValue(message,JSONObject.class);
        String fromUser= (String) updatewallet.get("fromUser");
        String toUser= (String) updatewallet.get("toUser");
        int transactionAmount=(Integer)updatewallet.get("amount");
        String transactionId= (String) updatewallet.get("transactionId");
        /*check fromUser balance if greater than transaction amount
          if block executes otherwise
                else block exectes
              */
        wallet senderwallet=walletRepository.findByuserName(fromUser);
        if(senderwallet.getAmount()>=transactionAmount){
            wallet sender_wallet=walletRepository.findByuserName(fromUser);
            //updatewallet
            sender_wallet.setAmount(sender_wallet.getAmount()-transactionAmount);
            walletRepository.save(sender_wallet);

            wallet toUserWallet=walletRepository.findByuserName(toUser);
            toUserWallet.setAmount(toUserWallet.getAmount()+transactionAmount);
            walletRepository.save(toUserWallet);

            //push to kafka
            JSONObject sendToTransaction=new JSONObject();
            sendToTransaction.put("transactionId",transactionId);
            sendToTransaction.put("TransactionStatus","SUCCESS");
            String sendMessage=sendToTransaction.toString();
            kafkaTemplate.send("update_transaction",sendMessage);


        }else{
            JSONObject sendToTransaction=new JSONObject();
            sendToTransaction.put("TransactionStatus","FAILED");
            sendToTransaction.put("transactionId",transactionId);
            String sendmessage=sendToTransaction.toString();
            kafkaTemplate.send("update_transaction",sendmessage);
        }

        //

  }
}

