package side.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import side.chatting.entity.Member;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
    Optional<Member> findByName(String name);

    Optional<Member> findByEmail(String email);

    @Query("select m from Member m join fetch m.auth a where m.username =:username ")
    Optional<Member> findMemberWithAuth(@Param("username") String username);


}

