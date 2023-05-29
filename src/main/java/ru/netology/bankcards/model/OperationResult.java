package ru.netology.bankcards.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OperationResult {

    private String operationId;

    public OperationResult(String operationId) {
        this.operationId = operationId;
    }
    public OperationResult() {
    }

}
