package com.auth.wow.libre.domain.model.security;

import com.auth.wow.libre.domain.model.AccountWebModel;
import com.auth.wow.libre.domain.model.exception.BadRequestException;
import com.auth.wow.libre.domain.ports.in.account_web.AccountWebPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserDetailsServiceCustom implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceCustom.class);

    private final AccountWebPort accountWebPort;


    public UserDetailsServiceCustom(AccountWebPort accountWebPort) {
        this.accountWebPort = accountWebPort;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AccountWebModel account = accountWebPort.findByEmail(username, "");

        if (account == null) {
            LOGGER.error("There is no associated user [{}]", username);
            throw new BadRequestException("There is no data with the information sent.", "");
        }

        return new CustomUserDetails(assignRol(account.rolName), account.password,
                account.email,
                true,
                true,
                true,
                true,
                account.id,
                account.avatarUrl
        );
    }


    private List<GrantedAuthority> assignRol(String name) {
        return Collections.singletonList(new SimpleGrantedAuthority(name));
    }
}
