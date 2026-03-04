package com.example.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="account_details")
@Getter
@Setter
@Serdeable
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;
    @NotBlank(message = "Account name shouldn't be empty")
    @Size(message = "Name should not be exceed more than 50 letters",max=50)
    private String account_Holder_Name;
    @NotBlank(message = "Account type shouldn't be empty")
    private String account_Type;
    private double balance;
    @Column(unique = true, nullable = false)
    private String accountNumber;
    public AccountEntity(String accountNumber,double balance,String account_Holder_Name, String account_Type){
        this.accountNumber=accountNumber;
        this.account_Type=account_Type;
        this.account_Holder_Name=account_Holder_Name;
        this.balance=balance;
    }
    public AccountEntity(){
        //default
    }
}

