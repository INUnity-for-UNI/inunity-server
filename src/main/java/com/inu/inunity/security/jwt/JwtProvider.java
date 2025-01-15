package com.inu.inunity.security.jwt;

import com.inu.inunity.security.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final CustomUserDetailsService customUserDetailsService;
    @Value("${spring.jwt.secret}")
    private String secretKey = "secretKey";

    @Value("${spring.jwt.refresh-token-valid-time}")
    private Long refreshTokenValidTime;

    @Value("${spring.jwt.access-token-valid-time}")
    private Long accessTokenValidTime;

    private static final String COOKIE_NAME = "accessToken";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        log.info("[init] 시크릿키 초기화 성공");
    }

    public String createRefreshToken(Long id, Long studentNum, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(studentNum));
        claims.put("userId", id);
        claims.put("roles", roles);
        claims.put("type", "refresh");
        Date now = new Date();
        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public String createAccessToken(Long id, Long studentNum, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(studentNum));
        claims.put("roles", roles);
        claims.put("memberId", id);
        claims.put("type", "access");

        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        log.info("[createAccessToken] 토큰 생성 완료");
        return token;
    }


    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(this.getMemberEmail(token));
        log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails UserName : {}", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    private String getMemberEmail(String token) {
        String email = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        log.info("[getMemberEmail] 토큰 기반 회원 구별 정보 추출 완료, info : {}", email);
        return email;
    }

    public Long getMemberId(String token) {
        Long memberId = Long.valueOf(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("memberId").toString());
        log.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", memberId);
        return memberId;
    }

    public Boolean validAccessToken(String token) {
        if (Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("type").equals("access")) {
            return true;
        } else {
            throw new RuntimeException("타입이 틀렸습니다.");
        }
    }

    public Boolean validRefreshToken(String token) {
        if (Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("type").equals("refresh")) {
            return true;
        } else {
            throw new RuntimeException("타입이 틀렸습니다.");
        }
    }

    public String getAuthorizationToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String resolveServiceToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public boolean validDateToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

        if (!claims.getBody().getExpiration().before(new Date())) {
            log.info("[validDateToken] 토큰 유효성 체크 성공");
            return true;
        } else {
            log.info("[validDateToken] 토큰 유효성 체크 실패");
            return false;
        }
    }

    public void setTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        // Access Token 쿠키
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(convertMilliSecondsToSeconds(accessTokenValidTime))
                .build();

        // Refresh Token 쿠키
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(convertMilliSecondsToSeconds(refreshTokenValidTime))
                .build();

        // 응답에 쿠키 추가
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    public Long convertMilliSecondsToSeconds(Long time){
        return time / 1000;
    }
}