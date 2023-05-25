package ru.netology.bankcards.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TransferOperation {
    private CreditCard creditCardFrom;
    private CreditCard creditCardTo;
    private Amount amount;
}
