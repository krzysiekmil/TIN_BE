package pjwstk.s20124.tin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjwstk.s20124.tin.model.Event;
import pjwstk.s20124.tin.model.Post;
import pjwstk.s20124.tin.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    Stream<Post> findPostByAuthor_IdOrderByCreatedBy(Long author_id);

    Stream<Post> findAllByAuthorIdInOrderByCreateDateDesc(Collection<Long> authorId);

    @Query("Select post from Post post where" +
        "                        (:user is null OR post.author = :user )" +
        "                        ORDER BY post.createDate DESC")
    Page<Post> findPostByUser(
                                                                        @Param("user") User user,
                                                                        Pageable pageable);



}
