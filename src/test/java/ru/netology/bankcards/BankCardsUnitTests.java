package ru.netology.bankcards;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.bankcards.model.VerificationRequest;
import ru.netology.bankcards.model.Amount;
import ru.netology.bankcards.model.CreditCardInfoToTransfer;
import ru.netology.bankcards.model.*;
import ru.netology.bankcards.repository.BankCardsRepository;
import ru.netology.bankcards.repository.TransferOperationRepository;
import ru.netology.bankcards.service.OperationService;
import ru.netology.bankcards.utils.ErrorConfirmation;
import ru.netology.bankcards.utils.ErrorInputData;
import ru.netology.bankcards.utils.ErrorTransfer;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class BankCardsUnitTests {
    private CreditCard creditCardFrom;
    private Account account1;
    private CreditCard creditCardTo;
    private Account account2;
    private Amount randomTransferAmount;
    private OperationService operationService;

    private TransferOperation transferOperation;
    private CreditCardInfoToTransfer creditCardInfoToTransfer;
    private VerificationRequest verificationRequest;

    @Mock
    private BankCardsRepository bankCardsRepository;
    @Mock
    private TransferOperationRepository transferOperationRepository;

    @BeforeEach
    public void init() {
        creditCardFrom = new CreditCard();
        creditCardFrom.setCardNumber("1234567891234567");
        creditCardFrom.setCardValidTill("12/24");
        creditCardFrom.setCardCVV("123");
        account1 = new Account(creditCardFrom, new Balance("RUR", 50000));

        creditCardTo = new CreditCard();
        creditCardTo.setCardNumber("0987654321098765");
        creditCardTo.setCardValidTill("01/24");
        creditCardTo.setCardCVV("987");
        account2 = new Account(creditCardTo, new Balance("RUR", 10000));

        operationService = new OperationService(bankCardsRepository, transferOperationRepository);

        creditCardInfoToTransfer = new CreditCardInfoToTransfer(creditCardFrom.getCardNumber(), creditCardTo.getCardNumber(),
                creditCardFrom.getCardValidTill(), creditCardFrom.getCardCVV(),
                randomTransferAmount);
    }

    @Test
    public void successSaveOperationTest() {
        BankCardsRepository bankCardsRepository = Mockito.mock(BankCardsRepository.class);
        TransferOperationRepository transferOperationRepository = Mockito.mock(TransferOperationRepository.class);
        OperationService operationService = new OperationService(bankCardsRepository, transferOperationRepository);

        String uuid = UUID.randomUUID().toString();

        randomTransferAmount = new Amount("RUR", randInt(0, 50000));

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

        randomTransferAmount = new Amount("RUR", randInt(0, 50000));

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

        verificationRequest = new VerificationRequest(uuid, randFourDigitInt());

        Mockito.when(transferOperationRepository.getById(uuid)).thenReturn(transferOperation);

        Assertions.assertThrows(ErrorConfirmation.class, () -> operationService.verifyCode(verificationRequest));
    }

    @Test
    public void successVerifyTest() {
        BankCardsRepository bankCardsRepository = Mockito.mock(BankCardsRepository.class);
        TransferOperationRepository transferOperationRepository = Mockito.mock(TransferOperationRepository.class);
        OperationService operationService = new OperationService(bankCardsRepository, transferOperationRepository);
        String uuid = UUID.randomUUID().toString();

        int transferAmount = randInt(0, 50000);

        transferOperation = new TransferOperation(creditCardFrom, creditCardTo, new Amount("RUR", transferAmount));

        verificationRequest = new VerificationRequest(uuid, "0000");

        Mockito.when(transferOperationRepository.getById(uuid)).thenReturn(transferOperation);
        Mockito.when(bankCardsRepository.getByNumberCard(transferOperation.getCreditCardFrom().getCardNumber())).thenReturn(account1);
        Mockito.when(bankCardsRepository.getByNumberCard(transferOperation.getCreditCardTo().getCardNumber())).thenReturn(account2);

        String saveOperationResult = operationService.verifyCode(verificationRequest);

        Assertions.assertEquals(uuid, saveOperationResult);
    }

    @Test
    public void balanceAccountFromMoreThanOrEqualValueTransferTest() {
        BankCardsRepository bankCardsRepository = Mockito.mock(BankCardsRepository.class);
        TransferOperationRepository transferOperationRepository = Mockito.mock(TransferOperationRepository.class);
        OperationService operationService = new OperationService(bankCardsRepository, transferOperationRepository);
        String uuid = UUID.randomUUID().toString();

        int transferAmount = randInt(0, 50000);

        transferOperation = new TransferOperation(creditCardFrom, creditCardTo, new Amount("RUR", transferAmount));

        Mockito.when(bankCardsRepository.getByNumberCard(transferOperation.getCreditCardFrom().getCardNumber())).thenReturn(account1);
        Mockito.when(bankCardsRepository.getByNumberCard(transferOperation.getCreditCardTo().getCardNumber())).thenReturn(account2);

        int balanceAccount1 = account1.getBalance().getAmount() - transferAmount - transferAmount / 100;
        int balanceAccount2 = account2.getBalance().getAmount() + transferAmount;
        operationService.transfer(transferOperation, uuid);

        Assertions.assertEquals(balanceAccount1, account1.getBalance().getAmount());
        Assertions.assertEquals(balanceAccount2, account2.getBalance().getAmount());
    }

    @Test
    public void balanceAccountFromLessThanValueTransferTest() {
        BankCardsRepository bankCardsRepository = Mockito.mock(BankCardsRepository.class);
        TransferOperationRepository transferOperationRepository = Mockito.mock(TransferOperationRepository.class);
        OperationService operationService = new OperationService(bankCardsRepository, transferOperationRepository);
        String uuid = UUID.randomUUID().toString();

        int transferAmount = randInt(50001, 10_000_000);

        transferOperation = new TransferOperation(creditCardFrom, creditCardTo, new Amount("RUR", transferAmount));

        Mockito.when(bankCardsRepository.getByNumberCard(transferOperation.getCreditCardFrom().getCardNumber())).thenReturn(account1);
        Mockito.when(bankCardsRepository.getByNumberCard(transferOperation.getCreditCardTo().getCardNumber())).thenReturn(account2);

        Assertions.assertThrows(ErrorTransfer.class, () -> operationService.transfer(transferOperation, uuid));
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static String randFourDigitInt() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        String result = "";
        for (int i = 0; i < 4; i++) {
            result += numbers.get(i).toString();
        }
        if (result.equals("0000")) {
            randFourDigitInt();
        }
        return result;
    }
}
