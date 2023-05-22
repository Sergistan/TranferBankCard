package ru.netology.bankcards.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.netology.bankcards.model.Account;
import ru.netology.bankcards.model.Balance;
import ru.netology.bankcards.model.CreditCard;
import ru.netology.bankcards.model.Currency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Repository
public class BankCardsRepository {
    private static final Map<String, Account> mapAccount = new ConcurrentHashMap<>();

    @PostConstruct
    public void initRepository() {
        CreditCard creditCard1 = new CreditCard();

        creditCard1.setCardNumber("1234567891234567");
        creditCard1.setCardValidTill("1224");
        creditCard1.setCardCVV("123");
        Account account1 = new Account(creditCard1, new Balance(Currency.RUR_CODE, 50000));

        CreditCard creditCard2 = new CreditCard();

        creditCard2.setCardNumber("0987654321098765");
        creditCard2.setCardValidTill("0124");
        creditCard2.setCardCVV("987");
        Account account2 = new Account(creditCard2, new Balance(Currency.RUR_CODE, 10000));

        mapAccount.put(creditCard1.getCardNumber(), account1);
        mapAccount.put(creditCard2.getCardNumber(), account2);
    }

    public boolean isContainsKeyByMapAccount(String id) {
        return mapAccount.containsKey(id);
    }

    public Account getByNumberCard(String cardNumber) {
        return mapAccount.get(cardNumber);
    }

}
