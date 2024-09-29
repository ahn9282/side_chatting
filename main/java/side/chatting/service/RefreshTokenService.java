package side.chatting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import side.chatting.entity.Refresh;

@Service
public class RefreshTokenService {

    @Autowired
    private final CacheManager refreshCacheManager;

    public RefreshTokenService(CacheManager refreshCacheManager) {
        this.refreshCacheManager = refreshCacheManager;
    }

    @CachePut(value = "refreshCache", key = "#refresh.username")
    public Refresh saveRefreshToken(Refresh refresh) {
        return refresh;
    }

    @Cacheable(value = "refreshCache", key = "#username")
    public Refresh getRefreshToken(String username) {
        return null;
    }
}
