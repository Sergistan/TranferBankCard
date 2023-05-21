package ru.netology.bankcards.utils;

public class ErrorConfirmation extends RuntimeException {

    public ErrorConfirmation() {
        super("Error confirmation");
    }
}
