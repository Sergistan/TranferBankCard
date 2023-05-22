package ru.netology.bankcards.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netology.bankcards.logger.Logger;
import ru.netology.bankcards.utils.ErrorConfirmation;
import ru.netology.bankcards.utils.ErrorInputData;
import ru.netology.bankcards.utils.ErrorTransfer;
import ru.netology.bankcards.model.*;
import ru.netology.bankcards.repository.BankCardsRepository;
import ru.netology.bankcards.repository.TransferOperationRepository;

@Service
public class OperationService {

    private final Logger logger = new Logger();
    private final BankCardsRepository bankCardsRepository;
    private final TransferOperationRepository transferOperationRepository;

    @Autowired
    public OperationService(BankCardsRepository bankCardsRepository, TransferOperationRepository transferOperationRepository) {
        this.bankCardsRepository = bankCardsRepository;
        this.transferOperationRepository = transferOperationRepository;
    }

    public String saveOperation(CreditCardInfoToTransfer creditCardInfoToTransfer) {
        CreditCard creditCardFrom = createCreditCardFrom(creditCardInfoToTransfer);
        CreditCard creditCardTo = createCreditCardTo(creditCardInfoToTransfer);

        if (isNumbersCreditCardExist(creditCardFrom, creditCardTo) && isCardFromValidTill(creditCardFrom) && isCardFromValidCVV(creditCardFrom)) {
            TransferOperation transferOperation = new TransferOperation(creditCardFrom, creditCardTo, creditCardInfoToTransfer.getAmount());
            return transferOperationRepository.save(transferOperation);
        }
        throw new ErrorInputData();
    }

    public String verifyCode(VerificationRequest verificationRequest) {
        String operationId = verificationRequest.getOperationId();
        TransferOperation operation = transferOperationRepository.getById(operationId);

        if (operation == null) {
            throw new ErrorTransfer();
        }

        if (!verificationRequest.getCode().equals("0000")) {
            throw new ErrorConfirmation();
        }

        transfer(operation, operationId);

        return operationId;

    }

    public void transfer(TransferOperation transferOperation, String operationId) {
        Account accountFrom = bankCardsRepository.getByNumberCard(transferOperation.getCreditCardFrom().getCardNumber());
        Account accountTo = bankCardsRepository.getByNumberCard(transferOperation.getCreditCardTo().getCardNumber());

        int balanceAccountFrom = accountFrom.getBalance().getAmount();
        int balanceAccountTo = accountTo.getBalance().getAmount();
        int valueTransfer = transferOperation.getAmount().getValue();

        if (balanceAccountFrom > valueTransfer) {
            accountFrom.setBalance(new Balance(Currency.RUR_CODE, balanceAccountFrom - valueTransfer));
            accountTo.setBalance(new Balance(Currency.RUR_CODE, balanceAccountTo + valueTransfer));

            logger.log(accountFrom, accountTo, valueTransfer, operationId, TransferResult.SUCCESS);
        } else {
            logger.log(accountFrom, accountTo, valueTransfer, operationId, TransferResult.FAIL);
            throw new ErrorTransfer();
        }
    }

    private static CreditCard createCreditCardFrom(CreditCardInfoToTransfer creditCardInfoToTransfer) {
        CreditCard creditCardFrom = new CreditCard();
        creditCardFrom.setCardNumber(creditCardInfoToTransfer.getCardFromNumber());
        creditCardFrom.setCardValidTill(creditCardInfoToTransfer.getCardFromValidTill());
        creditCardFrom.setCardCVV(creditCardInfoToTransfer.getCardFromCVV());
        return creditCardFrom;
    }

    private static CreditCard createCreditCardTo(CreditCardInfoToTransfer creditCardInfoToTransfer) {
        CreditCard creditCardTo = new CreditCard();
        creditCardTo.setCardNumber(creditCardInfoToTransfer.getCardToNumber());
        return creditCardTo;
    }

    public boolean isNumbersCreditCardExist(CreditCard from, CreditCard to) {
        return bankCardsRepository.isContainsKeyByMapAccount(from.getCardNumber())
                && bankCardsRepository.isContainsKeyByMapAccount(to.getCardNumber());
    }

    public boolean isCardFromValidTill(CreditCard creditCardFrom) {
        Account byNumberCard = bankCardsRepository.getByNumberCard(creditCardFrom.getCardNumber());
        return creditCardFrom.getCardValidTill().equals(byNumberCard.getCreditCard().getCardValidTill());
    }

    public boolean isCardFromValidCVV(CreditCard creditCardFrom) {
        Account byNumberCard = bankCardsRepository.getByNumberCard(creditCardFrom.getCardNumber());
        return creditCardFrom.getCardCVV().equals(byNumberCard.getCreditCard().getCardCVV());
    }
}
