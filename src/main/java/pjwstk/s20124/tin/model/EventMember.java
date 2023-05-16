package pjwstk.s20124.tin.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventMember extends AbstractEntity{

    @EmbeddedId
    public EventMemberId eventMemberId;
    @Enumerated
    private EventMemberAttendingStatus attendingStatus;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId", referencedColumnName = "id")
    public User user;

    @ManyToOne()
    @MapsId("eventId")
    @JoinColumn(name = "eventId", referencedColumnName = "id")
    public Event event;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventMember that = (EventMember) o;
        return getEventMemberId() != null && Objects.equals(getEventMemberId(), that.getEventMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventMemberId);
    }
}

