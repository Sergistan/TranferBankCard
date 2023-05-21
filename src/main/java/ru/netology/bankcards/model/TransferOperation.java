package ru.netology.bankcards.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TransferOperation {
    private CreditCard creditCardFrom;
    private CreditCard creditCardTo;
    private Amount amount;
}
