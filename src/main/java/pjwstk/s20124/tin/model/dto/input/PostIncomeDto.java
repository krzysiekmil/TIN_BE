package pjwstk.s20124.tin.model.dto.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostIncomeDto(
    @NotNull String title,
    @Size(max = 1024) String content,

    String image
) {
}
