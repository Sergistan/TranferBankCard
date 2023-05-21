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

    public void log(Account accountFrom, Account accountTo, int valueTransfer, String operationId) {
        String cardNumberFrom = accountFrom.getCreditCard().getCardNumber();
        String cardNumberTo = accountTo.getCreditCard().getCardNumber();

        int balanceAccountFrom = accountFrom.getBalance().getAmount();
        int commission = valueTransfer / 100;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("log.txt",true))) {
            bw.write("(" + dataTime + ") " + "Перевод с карты: " + cardNumberFrom + " на карту" + cardNumberTo + " . Сумма перевода: " + valueTransfer +
                    " комиссия 1 %: " + commission + " , баланс на карте: " + balanceAccountFrom + " . Идентификационный код: " + operationId + "\n");
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
