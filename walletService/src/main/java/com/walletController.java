package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("wallet")
public class walletController {
@Autowired
    walletService walletService;

//       @PostMapping("/add")
//       public void createWallet(@RequestParam("userName") String userName) {
//           walletService.createWallet(userName);
//       }
}
