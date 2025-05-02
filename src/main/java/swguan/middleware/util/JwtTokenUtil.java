package swguan.middleware.util;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import swguan.middleware.support.header.HeaderTokenDTO;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.token.expiration.minutes}")
	private int jwtExpiration;
	private long expiration;

	private ObjectMapper objectMapper = new ObjectMapper();

	@PostConstruct
	public void postConstruct() {
		expiration = jwtExpiration * 60 * 1000;
	}

	public String generateToken(Integer userSeqNbr, Integer loginSeqNbr) {

		Claims claims = Jwts.claims();

		claims.put("userSeqNbr", userSeqNbr);
		claims.put("loginSeqNbr", loginSeqNbr);

		return doGenerateToken(claims, "DEN-DEN-USER");
	}

	private String doGenerateToken(Claims claims, String subject) {
		return Jwts.builder().setSubject(subject).setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public HeaderTokenDTO getHeaderTokenDTO(String token) {
		final Claims claims = getClaimsFromToken(token);
		return objectMapper.convertValue(claims, HeaderTokenDTO.class);
	}

	private Claims getClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}

}
