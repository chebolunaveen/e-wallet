package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transaction")
public class TransactionController {
 @Autowired
    TransactionService transactionService;

     @PostMapping("/create")
    public  void createTransaction(@RequestBody() TransactionRequest transactionRequest){
         transactionService.CrateTransaction(transactionRequest);
     }
}
