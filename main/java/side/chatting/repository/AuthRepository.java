package side.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import side.chatting.entity.Auth;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {

    Auth findByAuth(String role);
}

