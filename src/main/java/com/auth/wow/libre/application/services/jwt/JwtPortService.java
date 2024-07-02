package com.auth.wow.libre.application.services.jwt;

import com.auth.wow.libre.domain.model.security.CustomUserDetails;
import com.auth.wow.libre.domain.ports.in.jwt.JwtPort;
import com.auth.wow.libre.infrastructure.conf.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.auth.wow.libre.domain.model.constant.Constants.CONSTANT_ACCOUNT_WEB_ID_JWT;
import static com.auth.wow.libre.domain.model.constant.Constants.CONSTANT_ROL_JWT_PROP;

@Component
@Slf4j
public class JwtPortService implements JwtPort {

    private final JwtProperties jwtProperties;

    public JwtPortService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(CONSTANT_ROL_JWT_PROP, userDetails.getAuthorities());
        extraClaims.put(CONSTANT_ACCOUNT_WEB_ID_JWT, userDetails.getAccountWebId());

        return generateToken(extraClaims, userDetails);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtProperties.getJwtExpiration());
    }

    @Override
    public String generateRefreshToken(CustomUserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtProperties.getRefreshExpiration());
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        return isSignatureValid(token) && !isTokenExpired(token);
    }

    private boolean isSignatureValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Error al validar la firma del token: {}", e.getMessage());
            return false; // La firma no es válida
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public Collection<GrantedAuthority> extractRoles(String token) {
        Claims claims = extractAllClaims(token);

        Collection<Map<String, String>> rolesAsMap = Optional.ofNullable(claims.get(CONSTANT_ROL_JWT_PROP))
                .filter(Collection.class::isInstance)
                .map(Collection.class::cast)
                .orElseGet(Collections::emptyList);

        return rolesAsMap.stream()
                .map(roleMap -> roleMap.get("authority"))
                .filter(Objects::nonNull)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
