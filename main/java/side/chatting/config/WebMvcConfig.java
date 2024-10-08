package side.chatting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//CORS 설정 해제 - Servlet 단
@ConfigurationProperties
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","http://localhost:9282","http://localhost:63342")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("access")
                .allowCredentials(true)
                .maxAge(3600L);
    }
}