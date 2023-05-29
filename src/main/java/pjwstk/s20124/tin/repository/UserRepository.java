package pjwstk.s20124.tin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjwstk.s20124.tin.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u.id from User u  where u.username = :username")
    Optional<Long> findIdByUsername(String username);

    @Query("SELECT u FROM User u where u.username ilike concat('%',:searchQuery,'%') or concat(u.firstName,u.lastName) ilike concat('%',:searchQuery,'%')")
    Set<User> findAllBySearchQuery(@Param("searchQuery") String searchQuery);
    
    boolean existsByUsername(String username);

    User getReferenceByUsername(String username);
}
