package com.auth.wow.libre.infrastructure.filter;

import com.auth.wow.libre.domain.model.constant.Constants;
import com.auth.wow.libre.domain.model.exception.UnauthorizedException;
import com.auth.wow.libre.domain.model.security.CustomUserDetails;
import com.auth.wow.libre.domain.model.security.JwtDto;
import com.auth.wow.libre.domain.model.security.UserModel;
import com.auth.wow.libre.domain.ports.in.jwt.JwtPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.auth.wow.libre.domain.model.constant.Constants.CONSTANTS_UNIQUE_ID;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String PATH_AUTHENTICATION_FILTER = "/api/auth/login";
    private final AuthenticationProvider authenticationProvider;
    private final JwtPort jwtPort;

    public AuthenticationFilter(AuthenticationProvider authenticationProvider,
                                JwtPort jwtPort) {
        this.authenticationProvider = authenticationProvider;
        this.jwtPort = jwtPort;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(buildPattern(), "POST"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        String transactionId = request.getHeader(Constants.HEADER_TRANSACTION_ID);
        ThreadContext.put(CONSTANTS_UNIQUE_ID, transactionId);
        UserModel user = getParamsUser(request, transactionId);
        return authenticationProvider
                .authenticate(new UsernamePasswordAuthenticationToken(user.username, user.password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException {

        String transactionId = request.getHeader(Constants.HEADER_TRANSACTION_ID);

        Map<String, Object> body = new HashMap<>();
        ThreadContext.put(CONSTANTS_UNIQUE_ID, transactionId);

        try {
            JwtDto jwtGenerate = generateToken(authResult);
            body.put("message", "ok");
            body.put("code", 200);
            body.put("data", jwtGenerate);
            body.put(Constants.HEADER_TRANSACTION_ID, transactionId);

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(200);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        } catch (Exception e) {
            body.put("error", "oauth_invalid");
            body.put("message",
                    "An unexpected error has occurred and it was not possible to authenticate to the system, please " +
                            "try " +
                            "again later.");
            body.put("message_trace", e.getMessage());
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Please verify the information provided.");
        body.put("message",
                "An unexpected error has occurred and it was not possible to authenticate to the system, please try " +
                        "again later.");
        body.put("message_trace", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }


    private String buildPattern() {
        return PATH_AUTHENTICATION_FILTER;
    }

    private UserModel getParamsUser(HttpServletRequest request, String transactionId) {
        try {
            return new ObjectMapper().readValue(request.getInputStream(), UserModel.class);
        } catch (java.io.IOException e) {
            logger.error(
                    "Unable to authenticate user. because your data was not sent correctly, please validate the " +
                            "information" +
                            " provided.");
            throw new UnauthorizedException(e.getMessage(), transactionId);
        }
    }

    private JwtDto generateToken(Authentication authResult) {
        CustomUserDetails customUserDetails = ((CustomUserDetails) authResult.getPrincipal());
        String token = jwtPort.generateToken(customUserDetails);
        Date expiration = jwtPort.extractExpiration(token);
        String refreshToken = jwtPort.generateRefreshToken(customUserDetails);
        return new JwtDto(token, refreshToken, expiration, customUserDetails.getAvatarUrl());
    }
}
