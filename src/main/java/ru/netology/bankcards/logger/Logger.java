package ru.netology.bankcards.logger;

import ru.netology.bankcards.model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private final Date currentTime = new Date();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
    private final String dataTime = dateFormat.format(currentTime);

    public void log(Account accountFrom,
                    Account accountTo,
                    int valueTransfer,
                    String operationId,
                    TransferResult result) {
        String cardNumberFrom = accountFrom.getCreditCard().getCardNumber();
        String cardNumberTo = accountTo.getCreditCard().getCardNumber();

        int balanceAccountFrom = accountFrom.getBalance().getAmount();
        int commission = valueTransfer / 100;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("log.txt",true))) {
            bw.write("(" + dataTime + ") " +
                    //TODO: скрыть карты
                    "Перевод с карты: " + encryptionCardNumber(cardNumberFrom) +
                    " на карту" + encryptionCardNumber(cardNumberTo) +
                    ". " + "\n" +
                    "Сумма перевода: " + valueTransfer +
                    " Комиссия 1 %: " + commission +
                    ", баланс на карте: " + balanceAccountFrom +
                    ". Идентификатор операции: " + operationId +
                    ". Статус операции: " + result.getCode() +
                    "\n");
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String encryptionCardNumber (String cardNumber){
        return "*" + cardNumber.substring(cardNumber.length()-4);
    }
}
