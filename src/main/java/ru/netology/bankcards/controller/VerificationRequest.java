package ru.netology.bankcards.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VerificationRequest {
    private String operationId;
    private String code;

    public VerificationRequest(String operationId, String code) {
        this.operationId = operationId;
        this.code = code;
    }
}
