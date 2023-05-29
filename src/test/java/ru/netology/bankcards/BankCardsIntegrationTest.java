package ru.netology.bankcards;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import ru.netology.bankcards.model.CreditCardInfoToTransfer;
import ru.netology.bankcards.model.VerificationRequest;
import ru.netology.bankcards.model.Amount;
import ru.netology.bankcards.model.CreditCard;
import ru.netology.bankcards.model.OperationResult;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankCardsIntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;

    private CreditCard creditCardFrom;
    private CreditCard creditCardTo;
    private Amount randomTransferAmount;
    private CreditCardInfoToTransfer creditCardInfoToTransfer;
    private VerificationRequest verificationRequest;
    private final GenericContainer<?> backend = new GenericContainer<>("backend")
            .withExposedPorts(5500);
    private final GenericContainer<?> frontend = new GenericContainer<>("frontend")
            .withExposedPorts(3000);

    @BeforeEach
    public void setUp() {
        backend.start();
        frontend.start();
    }

    @Test
    public void successfulTestWorkOfTheSystem() {
        creditCardFrom = new CreditCard();
        creditCardFrom.setCardNumber("1234567891234567");
        creditCardFrom.setCardValidTill("12/24");
        creditCardFrom.setCardCVV("123");

        creditCardTo = new CreditCard();
        creditCardTo.setCardNumber("0987654321098765");
        creditCardTo.setCardValidTill("01/24");
        creditCardTo.setCardCVV("987");

        randomTransferAmount = new Amount("RUR", 55);

        creditCardInfoToTransfer = new CreditCardInfoToTransfer(creditCardFrom.getCardNumber(), creditCardTo.getCardNumber(),
                creditCardFrom.getCardValidTill(), creditCardFrom.getCardCVV(),
                randomTransferAmount);

        ResponseEntity<OperationResult> backEntity = restTemplate.postForEntity("http://localhost:" +
                backend.getMappedPort(5500) + "/transfer", creditCardInfoToTransfer, OperationResult.class);

        HttpStatusCode statusCode = backEntity.getStatusCode();
        Assertions.assertEquals(HttpStatus.OK, statusCode);

        String operationFromTransfer = backEntity.getBody().getOperationId();

        verificationRequest = new VerificationRequest(backEntity.getBody().getOperationId(), "0000");

        ResponseEntity<OperationResult> frontEntity = restTemplate.postForEntity("http://localhost:" +
                backend.getMappedPort(5500) + "/confirmOperation", verificationRequest, OperationResult.class);

        String operationIdFromConfirmOperation = frontEntity.getBody().getOperationId();

        Assertions.assertEquals(operationIdFromConfirmOperation, operationFromTransfer);
    }

}
