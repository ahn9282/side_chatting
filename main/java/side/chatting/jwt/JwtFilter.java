package side.chatting.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
        try{
            jwtUtil.isExpired(accessToken);
        }catch(ExpiredJwtException e){
            log.info("jwtUtil.isExpired(accessToken);");
            PrintWriter writer = response.getWriter();
            writer.print("access : token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        Member member = new Member();
        member.setUsername(username);
        Auth auth = authRepository.findByAuth(role);
        member.setAuth(auth);
        CustomUser customUser = new CustomUser(member);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);
    }
}
