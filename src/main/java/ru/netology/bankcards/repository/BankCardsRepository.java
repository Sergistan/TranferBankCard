package ru.netology.bankcards.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import ru.netology.bankcards.model.Account;
import ru.netology.bankcards.model.Balance;
import ru.netology.bankcards.model.CreditCard;
import ru.netology.bankcards.model.Currency;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
@Configuration
@PropertySource("classpath:application.properties")
public class BankCardsRepository {
    private static final Map<String, Account> mapAccount = new ConcurrentHashMap<>();

    private final Environment env;

    @Autowired
    public BankCardsRepository(Environment env) {
        this.env = env;
    }

    public void initRepository() {
        CreditCard creditCard1 = new CreditCard();
        creditCard1.setCardNumber(env.getProperty("account1.card.number"));
        creditCard1.setCardValidTill(env.getProperty("account1.card.valid.till"));
        creditCard1.setCardCVV(env.getProperty("account1.card.CVV"));
        Account account1 = new Account(creditCard1, new Balance(Currency.RUR_CODE,
                Integer.parseInt(Objects.requireNonNull(env.getProperty("account1.balance")))));

//        creditCard1.setCardNumber("1234567891234567");
//        creditCard1.setCardValidTill("1224");
//        creditCard1.setCardCVV("123");
//        Account account1 = new Account(creditCard1, new Balance(Currency.RUR_CODE, 50000));

        CreditCard creditCard2 = new CreditCard();
        creditCard2.setCardNumber(env.getProperty("account2.card.number"));
        creditCard2.setCardValidTill(env.getProperty("account2.card.valid.till"));
        creditCard2.setCardCVV(env.getProperty("account2.card.CVV"));
        Account account2 = new Account(creditCard2, new Balance(Currency.RUR_CODE,
                Integer.parseInt(Objects.requireNonNull(env.getProperty("account2.balance")))));

//        creditCard2.setCardNumber("0987654321098765");
//        creditCard2.setCardValidTill("0124");
//        creditCard2.setCardCVV("987");
//        Account account2 = new Account(creditCard2, new Balance(Currency.RUR_CODE, 10000));

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
