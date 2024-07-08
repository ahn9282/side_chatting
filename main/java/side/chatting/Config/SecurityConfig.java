package side.chatting.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.annotations.Config;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import side.chatting.jwt.CustomLogoutFilter;
import side.chatting.jwt.JwtFilter;
import side.chatting.jwt.JwtUtil;
import side.chatting.jwt.LoginFilter;
import side.chatting.repository.AuthRepository;
import side.chatting.repository.RefreshRepository;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshRepository refreshRepository;
    private final AuthRepository authRepository;
    private final ObjectMapper objectMapper;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(e -> e.disable());
        http
                .httpBasic((auth) -> auth.disable());
        http
                .formLogin(auth -> auth.disable());

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join", "/reissue").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/user").hasRole("USER")
                        .anyRequest().authenticated());


        http    //cors : 교차 리소스 공유  설정  securityConfig 랑 WebMvcConfigurer 구현체로 두번 설정해야함
                .cors(cors -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration corsConfiguration = new CorsConfiguration();
                                //port
                                corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));//http 메서드
                                corsConfiguration.setAllowCredentials(true);
                                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));//Header 허용
                                corsConfiguration.setMaxAge(3600L);//최대 시간
                                corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));//header key 허용

                                return corsConfiguration;
                            }

                        }));


        http
                .addFilterAt(new LoginFilter(refreshRepository, authenticationManager(authenticationConfiguration), jwtUtil,objectMapper),
                        UsernamePasswordAuthenticationFilter.class);

        http    //LoginFilter 실행 전으로 순서지정하여 이미 로그인 시 처리하여 로그인 필터로 반복 없앰
                .addFilterBefore(new JwtFilter(jwtUtil, authRepository), LoginFilter.class);
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
