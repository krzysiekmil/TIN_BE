package pjwstk.s20124.tin.model.dto.output;

import lombok.Builder;
import lombok.Data;
import pjwstk.s20124.tin.model.EventMemberAttendingStatus;

@Data
@Builder
public class EventMemberOutputDto {
    private String attendingStatus;
    private String user;
    private Long userId;


}
