package pjwstk.s20124.tin.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pjwstk.s20124.tin.model.Event;
import pjwstk.s20124.tin.model.InfoChange;
import pjwstk.s20124.tin.model.InfoChangeType;
import pjwstk.s20124.tin.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Repository
public interface InfoChangeRepository extends JpaRepository<InfoChange, Long> {

    @Query("select i from InfoChange i where i.user = ?1 and i.eventRelatedTo = ?2 and i.type = ?3")
    Optional<InfoChange> findInfoChangeByUserAndEventRelatedToAndType(User user, Event event, InfoChangeType type);
    Stream<InfoChange> findAllByUserIdInOrderByLastModifiedBy(List<Long> users);
}
