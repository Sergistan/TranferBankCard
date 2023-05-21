package ru.netology.bankcards.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Balance {
    private Currency currency;
    private int amount;


}
