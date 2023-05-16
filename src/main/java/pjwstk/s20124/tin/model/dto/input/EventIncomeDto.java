package pjwstk.s20124.tin.model.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record EventIncomeDto(
    @NotBlank String title,
    String description,
    @NotNull LocalDateTime startDateTime,
    @NotNull LocalDateTime endDateTime,
    List<Long> members
) {
}
