package com.auth.wow.libre.application.services.auth;

import com.auth.wow.libre.domain.model.security.JwtDto;
import com.auth.wow.libre.domain.model.security.UserDetailsServiceCustom;
import com.auth.wow.libre.domain.ports.in.account.AuthPort;
import com.auth.wow.libre.domain.ports.in.jwt.JwtPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService implements AuthPort {

  private final JwtPort jwtPort;
  private final UserDetailsServiceCustom userDetailsServiceCustom;
  private final PasswordEncoder passwordEncoder;

  public AuthService(JwtPort jwtPort, UserDetailsServiceCustom userDetailsServiceCustom, PasswordEncoder passwordEncoder) {

    this.jwtPort = jwtPort;
    this.userDetailsServiceCustom = userDetailsServiceCustom;
    this.passwordEncoder = passwordEncoder;
  }


  @Override
  public JwtDto login(String username, String password) {
    UserDetails userDetails = userDetailsServiceCustom.loadUserByUsername(username);

    if (!passwordEncoder.matches(password, userDetails.getPassword())) {
      throw new RuntimeException("La contrasena son invalidas");
    }

    String token = jwtPort.generateToken(userDetails);
    Date expiration = jwtPort.extractExpiration(token);
    return new JwtDto(token, "", expiration);
  }
}
