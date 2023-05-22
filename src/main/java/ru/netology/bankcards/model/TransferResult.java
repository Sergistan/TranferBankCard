package ru.netology.bankcards.model;

public enum TransferResult {

    SUCCESS("Success"),

    FAIL("Fail");

    private final String code;

     TransferResult(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
