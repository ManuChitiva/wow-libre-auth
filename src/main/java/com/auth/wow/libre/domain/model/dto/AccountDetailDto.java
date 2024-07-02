package com.auth.wow.libre.domain.model.dto;

import com.auth.wow.libre.domain.model.AccountBanned;
import lombok.Builder;

import java.time.LocalDate;

public record AccountDetailDto(
        Long id,
        String username,
        String email,
        String expansion,
        boolean online,
        String failedLogins,
        LocalDate joinDate,
        String lastIp,
        String muteReason,
        String muteBy,
        boolean mute,
        LocalDate lastLogin,
        String os,
        AccountWeb accountWeb,
        AccountBanned accountBanned
) {
    @Builder
    public static class AccountWeb {
        public Long id;
        public String country;
        public LocalDate dateOfBirth;
        public String firstName;
        public String lastName;
        public String cellPhone;
        public String email;
        public String rolName;
        public boolean status;
        public boolean verified;
    }


}
