package pjwstk.s20124.tin.model.dto.input;

import jakarta.validation.constraints.NotNull;

public record UserPasswordIncomeDto(
    @NotNull String password,
    @NotNull String newPassword,
    @NotNull String repeatPassword
) {
}
