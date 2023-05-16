package pjwstk.s20124.tin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.s20124.tin.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
