package ru.netology.bankcards.utils;

public class ErrorTransfer extends RuntimeException {

    public ErrorTransfer() {
        super("Error transfer");
    }
}
