package side.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import side.chatting.entity.Member;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);
    Member findByName(String name);

    @Query("select m from Member m join fetch m.auth a where m.username =:username ")
    Member findMemberWithAuth(@Param("username") String username);
}

