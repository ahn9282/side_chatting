package side.chatting.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.dto.CustomUser;
import side.chatting.entity.RefreshEntity;
import side.chatting.jwt.JwtUtil;
import side.chatting.repository.RefreshRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        String username = customUser.getUsername();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        String email = customUser.getEmail();
        String name = customUser.getName();

        String access = jwtUtil.createJwt(jwtUtil.HEADER_STRING, username, role, name, jwtUtil.ACCESS_EXPIRATION);
        String refresh = jwtUtil.createJwt(jwtUtil.COOKIE_REFRESH, username, role, name, jwtUtil.REFRESH_EXPIRATION);

        addRefreshEntity(username, refresh, 86400000L);

        HttpSession session = request.getSession();
        session.setAttribute("access", access);
        response.addCookie(createRepoCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

        response.sendRedirect("http://localhost:9282/");

    }

    @Transactional
    private void addRefreshEntity(String username, String refresh, Long expireMs) {
        LocalDateTime date = now().plus(expireMs, ChronoUnit.MILLIS);

        Optional<RefreshEntity> exist = refreshRepository.findByUsername(username);
        if (exist.isEmpty()) {

            RefreshEntity refreshEntity = new RefreshEntity();
            refreshEntity.setUsername(username);
            refreshEntity.setRefresh(refresh);
            refreshEntity.setExpiration(date.toString());
            refreshRepository.save(refreshEntity);
        } else {
            RefreshEntity refreshEntity = exist.get();
            refreshEntity.setRefresh(refresh);
            refreshEntity.setExpiration(date.toString());
            refreshRepository.save(refreshEntity);
        }
    }

    private Cookie createRepoCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }
}
