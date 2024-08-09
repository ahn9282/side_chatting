package side.chatting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import side.chatting.jwt.CustomLogoutFilter;
import side.chatting.jwt.JwtFilter;
import side.chatting.jwt.JwtUtil;
import side.chatting.jwt.LoginFilter;
import side.chatting.repository.AuthRepository;
import side.chatting.repository.RefreshRepository;
import side.chatting.security.CustomAuthenticationEntryPoint;
import side.chatting.security.CustomSuccessHandler;

import java.util.Arrays;
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
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomSuccessHandler customSuccessHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers("/images/**", "/css/**", "/js/**");
    }//해당 경로 FilterChain 맵핑 제외 Filter를 거치지 않도록


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(auth -> auth.disable());
        http
                .httpBasic((auth) -> auth.disable());
        http
                .formLogin(auth -> auth.disable());

        http
                .oauth2Login(auth -> auth
                        .loginPage("/login")
                        .successHandler(customSuccessHandler)
                        .authorizationEndpoint(e -> e.baseUri("/oauth2/authorization")));

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/user","/").authenticated()
                        .anyRequest().permitAll());



        http    //cors : 교차 리소스 공유  설정  securityConfig 랑 WebMvcConfigurer 구현체로 두번 설정해야함
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration corsConfiguration = new CorsConfiguration();
                            //port
                            corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:9282","http://localhost:63342"));
                            corsConfiguration.setAllowedMethods(Collections.singletonList("*"));//http 메서드
                            corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));//Header 허용
                            corsConfiguration.setExposedHeaders(Collections.singletonList("*"));//header key 허용
                            corsConfiguration.setAllowCredentials(true);
                            corsConfiguration.setMaxAge(3600L);//최대 시간
                            corsConfiguration.setExposedHeaders(Collections.singletonList("access"));//header key 허용

                            return corsConfiguration;
                        }));


        http
                .addFilterAt(new LoginFilter(refreshRepository, authenticationManager(authenticationConfiguration), jwtUtil,objectMapper),
                        UsernamePasswordAuthenticationFilter.class);

        http    //LoginFilter 실행 전으로 순서지정하여 이미 로그인 시 처리하여 로그인 필터로 반복 없앰
                .addFilterBefore(new JwtFilter(jwtUtil, authRepository), LoginFilter.class);
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        http
                .exceptionHandling(auth -> auth
                        .authenticationEntryPoint(authenticationEntryPoint));
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
