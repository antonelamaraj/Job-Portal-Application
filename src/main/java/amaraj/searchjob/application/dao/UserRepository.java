package amaraj.searchjob.application.dao;

import amaraj.searchjob.application.entity.Role;
import amaraj.searchjob.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = ?1")
   Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.userName = ?1")
    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);

    boolean existsByRolesNameIgnoreCase(String roleName);

}
