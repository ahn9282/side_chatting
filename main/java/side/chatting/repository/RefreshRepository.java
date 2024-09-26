package side.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.entity.Refresh;
import side.chatting.entity.RefreshEntity;

import java.util.Optional;

public interface RefreshRepository extends CrudRepository<Refresh, String> {

    Boolean existsByToken(String token);


    @Transactional
    void deleteByToken(String token);


    Optional<Refresh> findByUsername(String username);

}

