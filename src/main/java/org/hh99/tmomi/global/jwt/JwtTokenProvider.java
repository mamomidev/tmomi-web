package org.hh99.tmomi.global.jwt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.hh99.tmomi.global.redis.RefreshToken;
import org.hh99.tmomi.global.redis.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

	private final Key key;
	private final RefreshTokenRepository refreshTokenRepository;

	public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey,
		RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public JwtToken generateToken(Authentication authentication) {

		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		String accessToken = createAccessToken(authentication.getName(), authorities);
		String refreshToken = createRefreshToken(authentication.getName());

		refreshTokenRepository.save(RefreshToken.builder()
			.email(authentication.getName())
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build());

		return JwtToken.builder()
			.grantType("Bearer")
			.accessToken(accessToken)
			.build();
	}

	public String createAccessToken(String email, String auth) {
		return Jwts.builder()
			.setSubject(email)
			.claim("auth", auth)
			// .setExpiration(new Date((new Date()).getTime() + 86400000))
			.setExpiration(new Date((new Date()).getTime() + 5000))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String createRefreshToken(String email) {
		return Jwts.builder()
			.setSubject(email)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String createNewAccessToken(String accessToken) {
		Claims claims = parseClaims(accessToken);
		return createAccessToken((String)claims.get("sub"), (String)claims.get("auth"));
	}

	public Authentication getAuthentication(String accessToken) {

		Claims claims = parseClaims(accessToken);

		if (claims.get("auth") == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}

		Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		UserDetails principal = new User(claims.getSubject(), "", authorities);
		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	public void validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.", e);
		}
	}

	public void validateRefreshToken(String accessToken) {
		String refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
			.orElseThrow(() -> new ExpiredJwtException(null, null, ""))
			.getRefreshToken();

		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(refreshToken);
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.", e);
		}
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(accessToken)
				.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	public void createCookieAccessToken(String accessToken, HttpServletResponse httpServletResponse) throws
		UnsupportedEncodingException {
		Cookie cookie = new Cookie("Authorization",
			URLEncoder.encode("Bearer " + accessToken, "utf-8").replaceAll("\\+", "%20"));
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60);  // 쿠키 유효 시간 : 1시간
		httpServletResponse.addCookie(cookie);
	}
}
