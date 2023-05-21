package ru.netology.bankcards.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Amount {
    private Currency currency;

    @NotBlank
    @NotEmpty
    @NotNull
    @Positive
    private int value;
}
