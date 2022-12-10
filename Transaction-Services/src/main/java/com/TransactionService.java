package com;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service

public  class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    ObjectMapper objectMapper;
    public void CrateTransaction(TransactionRequest transactionRequest){
        Transaction transaction=Transaction.builder().fromUser(transactionRequest.getFromUser()).toUser(transactionRequest.getToUser()).
                             amount(transactionRequest.getAmount()).status("PENDING").transactionId(String.valueOf(UUID.randomUUID())).transactionTime(String.valueOf(new Date())).build();
                
            transactionRepository.save(transaction);
            //send a message to kafka to update the wallet
        JSONObject walletRequest=new JSONObject();
        walletRequest.put("fromUser",transactionRequest.getFromUser());
        walletRequest.put("toUser",transactionRequest.getToUser());
        walletRequest.put("amount",transactionRequest.getAmount());
        walletRequest.put("transactionId",transaction.getTransactionId());
        String message=walletRequest.toString();

        kafkaTemplate.send("updatewallet",message);

    }

    @KafkaListener(topics = {"update_transaction"},groupId="friends1_group")
    public void updateTransaction(String message) throws JsonProcessingException {
        JSONObject transactionRequest= objectMapper.readValue(message,JSONObject.class);
        String transactionstatus= (String) transactionRequest.get("TransactionStatus");
        String transactionId=(String) transactionRequest.get("transactionId");
        Transaction t=transactionRepository.findByTransactionId(transactionId);
        t.setStatus(transactionstatus);
        transactionRepository.save(t);

    }
}
