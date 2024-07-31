package side.chatting.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.dto.CustomUser;
import side.chatting.entity.RefreshEntity;
import side.chatting.repository.RefreshRepository;
import side.chatting.security.CustomAuthenticationFailureHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;
@Transactional
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final RefreshRepository refreshRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public LoginFilter(RefreshRepository refreshRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil, ObjectMapper objectMapper ) {
        this.refreshRepository = refreshRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;

        this.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authRequest = null;

        try {
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);
            String username = credentials.get("username");
            String password = credentials.get("password");

            authRequest = new UsernamePasswordAuthenticationToken(username, password);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        setDetails(request, authRequest);
        setDetails(request, authRequest);

        return this.authenticationManager.authenticate(authRequest);
    }

    //로그인 성공 시 실행하는 메서드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws ServletException {

        CustomUser user = (CustomUser)authentication.getPrincipal();
        String username = user.isOauth() ? user.getEmail() : user.getUsername();
        String name = user.getName();
        String email = user.getEmail();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt(jwtUtil.HEADER_STRING, username, role,name, jwtUtil.ACCESS_EXPIRATION);
        String refresh = jwtUtil.createJwt(jwtUtil.COOKIE_REFRESH, username, role,name, jwtUtil.REFRESH_EXPIRATION);

        addRefreshEntity(username, refresh, jwtUtil.REFRESH_EXPIRATION);

        response.addHeader(jwtUtil.HEADER_STRING, access);
        response.addCookie(createRepoCookie(jwtUtil.COOKIE_REFRESH, refresh));
        response.setStatus(HttpStatus.OK.value());
        log.info("response Header {}", response.getHeaders(jwtUtil.HEADER_STRING));
    }

    //로그인 실패 시 실행하는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws ServletException {
        response.setStatus(401);

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

        return cookie;
    }
}
