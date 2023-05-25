package ru.netology.bankcards;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.bankcards.model.*;
import ru.netology.bankcards.repository.BankCardsRepository;
import ru.netology.bankcards.repository.TransferOperationRepository;
import ru.netology.bankcards.service.OperationService;

import java.util.Random;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class BankCardsApplicationTests {
    private static Random rand;
    private static CreditCard creditCardFrom;
    private static Account account1;
    private static CreditCard creditCardTo;
    private static Account account2;
    private static Amount randomTransferAmount;
    private static OperationService operationService;

    private static TransferOperation transferOperation;
    private static CreditCardInfoToTransfer creditCardInfoToTransfer;

    @Mock
    private static BankCardsRepository bankCardsRepository;
    @Mock
    private static TransferOperationRepository transferOperationRepository;

    @BeforeAll
    public static void init() {
        creditCardFrom = new CreditCard();
        creditCardFrom.setCardNumber("1234567891234567");
        creditCardFrom.setCardValidTill("12/24");
        creditCardFrom.setCardCVV("123");
        account1 = new Account(creditCardFrom, new Balance("RUR", 50000));

        creditCardTo = new CreditCard();
        creditCardTo.setCardNumber("0987654321098765");
        account2 = new Account(creditCardTo, new Balance("RUR", 10000));

        randomTransferAmount = new Amount("RUR", 55);

        operationService = new OperationService(bankCardsRepository, transferOperationRepository);

        creditCardInfoToTransfer = new CreditCardInfoToTransfer(creditCardFrom.getCardNumber(), creditCardTo.getCardNumber(),
                creditCardFrom.getCardValidTill(), creditCardFrom.getCardCVV(),
                randomTransferAmount);

        transferOperation = new TransferOperation(creditCardFrom, creditCardTo, randomTransferAmount);
    }


    @Test
    public void saveOperationTest() {
        BankCardsRepository bankCardsRepository = Mockito.mock(BankCardsRepository.class);
        TransferOperationRepository transferOperationRepository = Mockito.mock(TransferOperationRepository.class);
        OperationService operationService = new OperationService(bankCardsRepository, transferOperationRepository);
        String uuid = UUID.randomUUID().toString();

        CreditCardInfoToTransfer creditCardInfoToTransfer = new CreditCardInfoToTransfer(
                creditCardFrom.getCardNumber(), creditCardTo.getCardNumber(),
                creditCardFrom.getCardValidTill(), creditCardFrom.getCardCVV(),
                randomTransferAmount);

        Mockito.when(bankCardsRepository.isContainsKeyByMapAccount(creditCardFrom.getCardNumber())).thenReturn(true);
        Mockito.when(bankCardsRepository.isContainsKeyByMapAccount(creditCardTo.getCardNumber())).thenReturn(true);

        Mockito.when(bankCardsRepository.getByNumberCard(creditCardFrom.getCardNumber())).thenReturn(
                new Account(creditCardFrom, new Balance("RUR", 50000)));

        Mockito.when(bankCardsRepository.getByNumberCard(creditCardTo.getCardNumber())).thenReturn(
                new Account(creditCardTo, new Balance("RUR", 10000)));

        Mockito.when(transferOperationRepository.save(any(TransferOperation.class))).thenReturn(uuid);

        String saveOperationResult = operationService.saveOperation(creditCardInfoToTransfer);

        Assertions.assertEquals(uuid, saveOperationResult);
    }

}
