package pjwstk.s20124.tin.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoChange extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InfoChangeType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Animal animalRelatedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    private User userRelatedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event eventRelatedTo;

    @BatchSize(size = 5)
    @OneToMany(mappedBy = "infoChange", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InfoChange that = (InfoChange) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    public static InfoChange buildNewFriendChange(User user, User friend){
        return InfoChange.builder()
            .user(user)
            .userRelatedTo(friend)
            .type(InfoChangeType.NEW_FRIEND)
            .build();
    }

    public static InfoChange buildNewAnimalChange(User user, Animal animal){
        return InfoChange.builder()
            .user(user)
            .animalRelatedTo(animal)
            .type(InfoChangeType.NEW_ANIMAL)
            .build();
    }

    public static InfoChange buildEventCreatedChange(User user, Event event){
        return InfoChange.builder()
            .user(user)
            .eventRelatedTo(event)
            .type(InfoChangeType.EVENT_CREATED)
            .build();
    }

    public static InfoChange buildEventAttendingStatusChange(User user, Event event){
        return InfoChange.builder()
            .user(user)
            .eventRelatedTo(event)
            .type(InfoChangeType.EVENT_ATTENDING_STATUS_CHANGE)
            .build();
    }

}
