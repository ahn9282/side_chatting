package side.chatting.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;
import side.chatting.dto.CustomUser;
import side.chatting.entity.Auth;
import side.chatting.entity.Member;
import side.chatting.repository.AuthRepository;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j//jwt 검증필터 구현
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthRepository authRepository;

    @Transactional(readOnly = true)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader("access");

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            log.info("category != access");
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try{
            jwtUtil.isExpired(accessToken);
        }catch(ExpiredJwtException e){
            String refresh = null;
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {

                if (cookie.getName().equals("refresh")) {

                    refresh = cookie.getValue();
                }
            }
            if (refresh == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            try{
                jwtUtil.isExpired(refresh);
            }catch(ExpiredJwtException  e2){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

        }



        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        String name = jwtUtil.getName(accessToken);

      Auth auth = new Auth();
      auth.setAuth(role);

        Member member = new Member();
        member.setUsername(username);
        member.setAuth(auth);
        member.setName(name);

        CustomUser customUser = new CustomUser(member, false);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        response.setHeader(jwtUtil.HEADER_STRING, accessToken);
        filterChain.doFilter(request,response);
    }
}
