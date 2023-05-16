package pjwstk.s20124.tin.model.dto.input;

import jakarta.validation.constraints.NotNull;
import pjwstk.s20124.tin.model.EventMemberAttendingStatus;

public record EventMemberIncomeDto(@NotNull EventMemberAttendingStatus status) {
}
