package com.auth.wow.libre.domain.model.dto;

import java.time.LocalDate;


public record AccountsDetailDto(Long id, String username, String email, String logoExpansion, String expansion,
                                boolean online,
                                String failedLogins, LocalDate joinDate, String lastIp) {
}
