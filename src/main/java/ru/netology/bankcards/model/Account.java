package ru.netology.bankcards.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Account {
    private CreditCard creditCard;
    private Balance balance;
}
