package pjwstk.s20124.tin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;
import pjwstk.s20124.tin.model.InfoChange;

import java.util.stream.Stream;

@Repository
public interface InfoChangeRepository extends JpaRepository<InfoChange, Long> {

    Stream<InfoChange> findAllByUser_Id_OrderByCreateDate(Long user_id);
}
