package pjwstk.s20124.tin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.s20124.tin.model.EventMember;
import pjwstk.s20124.tin.model.EventMemberId;

@Repository
public interface EventMemberRepository extends JpaRepository<EventMember, EventMemberId> {
}
