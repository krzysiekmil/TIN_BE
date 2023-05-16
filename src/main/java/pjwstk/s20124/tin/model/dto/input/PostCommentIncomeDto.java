package pjwstk.s20124.tin.model.dto.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record PostCommentIncomeDto(@NotBlank String content) {
}
