package pjwstk.s20124.tin.model.dto.output;

import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import pjwstk.s20124.tin.model.EventMember;
import pjwstk.s20124.tin.model.User;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@Builder
public class EventOutputDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String host;
    private Long hostId;
    private boolean owner;

    private Set<EventMemberOutputDto> members;
}
