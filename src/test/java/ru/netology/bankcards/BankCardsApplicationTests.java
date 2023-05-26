package ru.netology.bankcards;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.bankcards.controller.VerificationRequest;
import ru.netology.bankcards.model.Amount;
import ru.netology.bankcards.controller.CreditCardInfoToTransfer;
import ru.netology.bankcards.model.*;
import ru.netology.bankcards.repository.BankCardsRepository;
import ru.netology.bankcards.repository.TransferOperationRepository;
import ru.netology.bankcards.service.OperationService;
import ru.netology.bankcards.utils.ErrorConfirmation;
import ru.netology.bankcards.utils.ErrorInputData;
import ru.netology.bankcards.utils.ErrorTransfer;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class BankCardsApplicationTests {
    private static CreditCard creditCardFrom;
    private static Account account1;
    private static CreditCard creditCardTo;
    private static Account account2;
    private static Amount randomTransferAmount;
    private static OperationService operationService;

    private static TransferOperation transferOperation;
    private static CreditCardInfoToTransfer creditCardInfoToTransfer;
    private static VerificationRequest verificationRequest;

    @Mock
    private static BankCardsRepository bankCardsRepository;
    @Mock
    private static TransferOperationRepository transferOperationRepository;

    @BeforeEach
    public void init() {
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
    public void successSaveOperationTest() {
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

    @Test
    public void failSaveOperationTest() {
        BankCardsRepository bankCardsRepository = Mockito.mock(BankCardsRepository.class);
        TransferOperationRepository transferOperationRepository = Mockito.mock(TransferOperationRepository.class);
        OperationService operationService = new OperationService(bankCardsRepository, transferOperationRepository);
        String uuid = UUID.randomUUID().toString();

        CreditCardInfoToTransfer creditCardInfoToTransfer = new CreditCardInfoToTransfer(
                creditCardFrom.getCardNumber(), creditCardTo.getCardNumber(),
                creditCardFrom.getCardValidTill(), creditCardFrom.getCardCVV(),
                randomTransferAmount);

        Mockito.when(bankCardsRepository.isContainsKeyByMapAccount(creditCardFrom.getCardNumber())).thenReturn(false);
        Mockito.when(bankCardsRepository.isContainsKeyByMapAccount(creditCardTo.getCardNumber())).thenReturn(true);

        Mockito.when(bankCardsRepository.getByNumberCard(creditCardFrom.getCardNumber())).thenReturn(
                new Account(creditCardFrom, new Balance("RUR", 50000)));

        Mockito.when(bankCardsRepository.getByNumberCard(creditCardTo.getCardNumber())).thenReturn(
                new Account(creditCardTo, new Balance("RUR", 10000)));

        Mockito.when(transferOperationRepository.save(any(TransferOperation.class))).thenReturn(uuid);

        Assertions.assertThrows(ErrorInputData.class, () -> operationService.saveOperation(creditCardInfoToTransfer));
    }

    @Test
    public void nullVerifyTest() {
        BankCardsRepository bankCardsRepository = Mockito.mock(BankCardsRepository.class);
        TransferOperationRepository transferOperationRepository = Mockito.mock(TransferOperationRepository.class);
        OperationService operationService = new OperationService(bankCardsRepository, transferOperationRepository);
        String uuid = UUID.randomUUID().toString();

        verificationRequest = new VerificationRequest(uuid, "0000");

        Mockito.when(transferOperationRepository.getById(uuid)).thenReturn(null);

        Assertions.assertThrows(ErrorConfirmation.class, () -> operationService.verifyCode(verificationRequest));
    }

    @Test
    public void notCorrectCodeVerifyTest() {
        BankCardsRepository bankCardsRepository = Mockito.mock(BankCardsRepository.class);
        TransferOperationRepository transferOperationRepository = Mockito.mock(TransferOperationRepository.class);
        OperationService operationService = new OperationService(bankCardsRepository, transferOperationRepository);
        String uuid = UUID.randomUUID().toString();

        verificationRequest = new VerificationRequest(uuid, "1234");

        Mockito.when(transferOperationRepository.getById(uuid)).thenReturn(transferOperation);

        Assertions.assertThrows(ErrorConfirmation.class, () -> operationService.verifyCode(verificationRequest));
    }

    @Test
    public void successVerifyTest() {
        BankCardsRepository bankCardsRepository = Mockito.mock(BankCardsRepository.class);
        TransferOperationRepository transferOperationRepository = Mockito.mock(TransferOperationRepository.class);
        OperationService operationService = new OperationService(bankCardsRepository, transferOperationRepository);
        String uuid = UUID.randomUUID().toString();

        verificationRequest = new VerificationRequest(uuid, "0000");

        Mockito.when(transferOperationRepository.getById(uuid)).thenReturn(transferOperation);
        Mockito.when(bankCardsRepository.getByNumberCard(transferOperation.getCreditCardFrom().getCardNumber())).thenReturn(account1);
        Mockito.when(bankCardsRepository.getByNumberCard(transferOperation.getCreditCardTo().getCardNumber())).thenReturn(account2);

        String saveOperationResult = operationService.verifyCode(verificationRequest);

        Assertions.assertEquals(uuid, saveOperationResult);
    }

    @Test
    public void balanceAccountFromMoreThanValueTransferTransferTest() {
        BankCardsRepository bankCardsRepository = Mockito.mock(BankCardsRepository.class);
        TransferOperationRepository transferOperationRepository = Mockito.mock(TransferOperationRepository.class);
        OperationService operationService = new OperationService(bankCardsRepository, transferOperationRepository);
        String uuid = UUID.randomUUID().toString();
        int transferAmount = 3000;

        transferOperation = new TransferOperation(creditCardFrom, creditCardTo, new Amount("RUR", transferAmount));

        Mockito.when(bankCardsRepository.getByNumberCard(transferOperation.getCreditCardFrom().getCardNumber())).thenReturn(account1);
        Mockito.when(bankCardsRepository.getByNumberCard(transferOperation.getCreditCardTo().getCardNumber())).thenReturn(account2);

        operationService.transfer(transferOperation, uuid);

        Assertions.assertEquals(account1.getBalance().getAmount(), 46970);
        Assertions.assertEquals(account2.getBalance().getAmount(), 13000);
    }

    @Test
    public void balanceAccountFromLessThanValueTransferTransferTest() {
        BankCardsRepository bankCardsRepository = Mockito.mock(BankCardsRepository.class);
        TransferOperationRepository transferOperationRepository = Mockito.mock(TransferOperationRepository.class);
        OperationService operationService = new OperationService(bankCardsRepository, transferOperationRepository);
        String uuid = UUID.randomUUID().toString();
        int transferAmount = 55000;

        transferOperation = new TransferOperation(creditCardFrom, creditCardTo, new Amount("RUR", transferAmount));

        Mockito.when(bankCardsRepository.getByNumberCard(transferOperation.getCreditCardFrom().getCardNumber())).thenReturn(account1);
        Mockito.when(bankCardsRepository.getByNumberCard(transferOperation.getCreditCardTo().getCardNumber())).thenReturn(account2);

        Assertions.assertThrows(ErrorTransfer.class, () -> operationService.transfer(transferOperation, uuid));
    }


}
