package com.auth.wow.libre.infrastructure.filter;

import com.auth.wow.libre.application.services.jwt.JwtPortService;
import com.auth.wow.libre.domain.model.constant.Constants;
import com.auth.wow.libre.domain.model.exception.GenericErrorException;
import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

import static com.auth.wow.libre.domain.model.constant.Constants.CONSTANTS_UNIQUE_ID;
import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_EMAIL_JWT;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final com.auth.wow.libre.domain.ports.in.jwt.JwtPort jwtPort;

    public JwtAuthenticationFilter(JwtPortService jwtPort) {
        this.jwtPort = jwtPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        GenericResponse<Void> responseBody = new GenericResponse<>();

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String transactionId = request.getHeader(Constants.HEADER_TRANSACTION_ID);
        ThreadContext.put(CONSTANTS_UNIQUE_ID, transactionId);

        try {

            if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            RequestWrapper requestWrapper = new RequestWrapper(request);
            ResponseWrapper responseWrapper = new ResponseWrapper(response);

            final String jwt = authHeader.substring(7);
            final String email = jwtPort.extractUsername(jwt);

            requestWrapper.setHeader(HEADER_EMAIL_JWT, email);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtPort.isTokenValid(jwt)) {
                    Collection<GrantedAuthority> authority = jwtPort.extractRoles(jwt);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(email, null,
                                    authority);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(requestWrapper, responseWrapper);
            response.setStatus(responseWrapper.getStatus());
            response.setContentType(request.getContentType());
            response.getOutputStream().write(responseWrapper.getByteArray());
        } catch (GenericErrorException e) {
            responseBody.setMessage(e.getMessage());
            responseBody.setTransactionId(e.transactionId);
            response.setStatus(e.httpStatus.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(responseBody));
        } catch ( ExpiredJwtException e) {
            responseBody.setMessage("Invalid JWT, has expired");
            responseBody.setCode(401);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(responseBody));
        }
    }
}
