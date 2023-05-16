package pjwstk.s20124.tin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjwstk.s20124.tin.model.Event;
import pjwstk.s20124.tin.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, PagingAndSortingRepository<Event, Long> {

    Optional<Event> findEventById(Long id);

    @Query("Select event from Event event left join EventMember member on event = member.event where" +
        "                        (:startDate is null OR event.startDateTime > :startDate) AND" +
        "                        (:endDate is null OR event.endDateTime > :endDate) AND" +
        "                        (:user is null OR event.host = :user OR member.user = :user) AND" +
        "                        (:title is null OR event.title = :title)" +
        "                        ORDER BY event.startDateTime DESC")
    Page<Event> findEventByTitleAndStartDateTimeAndEndDateTimeAndUserId(@Param("startDate") LocalDateTime startDate,
                                                                        @Param("endDate") LocalDateTime endDate,
                                                                        @Param("user") User user,
                                                                        @Param("title") String title,
                                                                        Pageable pageable);
}
