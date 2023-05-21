package ru.netology.bankcards.model;

import lombok.*;

@Getter
@Setter
@ToString
public class CreditCard {
    private String cardNumber;
    private String cardValidTill;
    private String cardCVV;

    public CreditCard() {
    }
}
