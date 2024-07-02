package com.auth.wow.libre.domain.model.security;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class JwtDto {

    public String jwt;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("expiration_date")
    public Date expirationDate;
    @JsonProperty("avatar_url")
    public String avatarUrl;

    public JwtDto(String jwt, String refreshToken, Date expirationDate, String avatarUrl) {
        this.jwt = jwt;
        this.refreshToken = refreshToken;
        this.expirationDate = expirationDate;
        this.avatarUrl = avatarUrl;
    }
}
