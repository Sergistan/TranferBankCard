package ru.netology.bankcards.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.bankcards.model.CreditCardInfoToTransfer;
import ru.netology.bankcards.model.VerificationRequest;
import ru.netology.bankcards.utils.ErrorConfirmation;
import ru.netology.bankcards.utils.ErrorInputData;
import ru.netology.bankcards.utils.ErrorTransfer;
import ru.netology.bankcards.utils.ErrorResponse;
import ru.netology.bankcards.model.OperationResult;
import ru.netology.bankcards.service.OperationService;

@RestController
@RequestMapping("/")
public class BankCardsController {

    private final OperationService operationService;

    @Autowired
    public BankCardsController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping("transfer")
    public OperationResult transferFromController(@Valid @RequestBody CreditCardInfoToTransfer creditCardInfoToTransfer) {
        return new OperationResult(operationService.saveOperation(creditCardInfoToTransfer));
    }

    @PostMapping("confirmOperation")
    public OperationResult confirmOperation(@RequestBody VerificationRequest verificationRequest) {
        return new OperationResult(operationService.verifyCode(verificationRequest));
    }

    @ExceptionHandler(ErrorInputData.class)
    public ResponseEntity<ErrorResponse> handlerErrorInputData() {
        ErrorResponse errorResponse = new ErrorResponse("Error Input Data", 0);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorTransfer.class)
    public ResponseEntity<ErrorResponse> handleErrorTransfer() {
        ErrorResponse errorResponse = new ErrorResponse("Error transfer", 0);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ErrorConfirmation.class)
    public ResponseEntity<ErrorResponse> handleErrorConfirmation() {
        ErrorResponse errorResponse = new ErrorResponse("Error confirmation", 0);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
