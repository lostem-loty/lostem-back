package loty.lostem.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private final long tokenValidityInMilliseconds = jwtProperties.getTokenValidityInMilliseconds();

    // authentication 객체에 포함된 권한정보를 이용해서 토큰 생성
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date validity = new Date(now.getTime() + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (헤더) type: JWT
                .setIssuer(jwtProperties.getIssuer()) // (내용) iss : test@test.com
                .setIssuedAt(now)
                .setSubject(authentication.getName())  // (내용) sub : user 이름
                .claim(AUTHORITIES_KEY, authorities) // (클레임) auth : 권한들
                // 서명
                .signWith(jwtProperties.getKey(), SignatureAlgorithm.HS512) // 암호화된 비밀키 값 + 해시를 HS512 방식으로 암호화
                .setExpiration(validity)
                .compact();
    }

    // 토큰 기반으로 인증 정보 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token); // 토큰을 파라미터로 받아 토큰을 클레임으로 만들고

        // 클레임에서 권한 정보 빼내서
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 권한 정보로 유저 객체 만들기(userDetails 의 user)
        User principal = new User(claims.getSubject(), "", authorities);

        // 유저 객체, 토큰, 권한 정보 이용하여 authentication 객체 리턴
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰을 파라미터로 받아 파싱해보고 발생하는 익셉션들 캐치, 문제 없으면 true (jwt 토큰 유효성 검사)
    public boolean validateToken(String token) {
        try { // setSigningKey : 비밀 값으로 복호화
            Jwts.parserBuilder().setSigningKey(jwtProperties.getKey()).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // 클레임 조회
    private Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(jwtProperties.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // refresh 토큰 발급
    public String createRefreshToken(String username) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidityInMilliseconds);
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(validity)
                .signWith(jwtProperties.getKey(), SignatureAlgorithm.HS512)
                .compact();
    }
}