package side.chatting.entity;


import com.nimbusds.oauth2.sdk.token.RefreshToken;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash("refresh_token")
public class Refresh {
;

    @Id
    private String id;

    @Indexed
    private String username;


    private String token;

    @TimeToLive
    private Long ttl;

    private String expiration;

    public Refresh update(String token, long ttl) {
        this.token = token;
        this.ttl = ttl;
        return this;
    }
}
