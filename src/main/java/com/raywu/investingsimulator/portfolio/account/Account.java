package com.raywu.investingsimulator.portfolio.account;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "fund")
    private double fund;

    @Column(name = "joined_at")
    private long joinedAt;

    // ------ Note ------ //
//    @Transient
//    private double shortSellingDeposit;
//
//    @Transient
//    private double totalRealizedGainLoss;
//
//    @Transient
//    private double totalUnrealizedGainLoss;

    // no-arg constructor is required by Hibernate
    public Account() {}
 }

 /*
 ------ Note ------
 I can use @Transient to add variable which is not a column of the table inside an entity

 I need to return the user account in the "/get-portfolio", since the Account entity contains
 the password, I cannot return Account directly. I need to use the AccountDTO instead.

 * */