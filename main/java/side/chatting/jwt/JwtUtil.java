package side.chatting.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

//로인 실행 로직 토큰 발급, 인코딩
@Component
public class JwtUtil {

    private Key key;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(token).getBody().get("usernmae", String.class);
    }

    public String getRole(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(token).getBody().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    public String getCategory(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("category", String.class);
    }



    public String createJwt(String category, String username, String role, Long expireMs) {
        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireMs))
                .signWith(key)
                .compact();
    }
}
