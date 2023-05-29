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
public class CreditCardInfoToTransfer {
    @Size (min = 16, max = 16)
    @NotBlank
    @NotEmpty
    @NotNull
    @Pattern(regexp = "\\d+")
    private String cardFromNumber;

    @Size (min = 16, max = 16)
    @NotBlank
    @NotEmpty
    @NotNull
    @Pattern(regexp = "\\d+")
    private String cardToNumber;

    @NotBlank
    @NotEmpty
    @NotNull
    private String cardFromValidTill;

    @Size (min = 3, max = 3)
    @NotBlank
    @NotEmpty
    @NotNull
    @Pattern(regexp = "\\d+")
    private String cardFromCVV;

    private Amount amount;
}
