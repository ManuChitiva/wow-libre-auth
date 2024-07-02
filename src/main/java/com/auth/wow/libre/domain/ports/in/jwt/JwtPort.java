package com.auth.wow.libre.domain.ports.in.jwt;


import com.auth.wow.libre.domain.model.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

public interface JwtPort {
  String generateToken(CustomUserDetails userDetails);

  String extractUsername(String token);

  boolean isTokenValid(String token);

  String generateRefreshToken(CustomUserDetails userDetails);

  Date extractExpiration(String token);

  Collection<GrantedAuthority>  extractRoles(String token);

}
