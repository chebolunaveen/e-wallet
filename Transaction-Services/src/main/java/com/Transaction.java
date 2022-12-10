package com;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="Transactions")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id;

    private String transactionId= UUID.randomUUID().toString();
    private String fromUser;
    private String toUser;
    private int amount;
    private String status;
    private String transactionTime;

}
