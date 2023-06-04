package pjwstk.s20124.tin.model.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentIncomeDto(@NotBlank String content, @NotNull Long id, @NotNull CommentType type) {
}
