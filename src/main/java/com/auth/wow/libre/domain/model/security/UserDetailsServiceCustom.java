package com.auth.wow.libre.domain.model.security;

import com.auth.wow.libre.domain.mapper.MapToModel;
import com.auth.wow.libre.domain.model.AccountWebModel;
import com.auth.wow.libre.domain.model.exception.BadRequestException;
import com.auth.wow.libre.domain.ports.out.account_web.ObtainAccountWebPort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserDetailsServiceCustom implements UserDetailsService {
    private final ObtainAccountWebPort obtainAccountWebPort;

    public UserDetailsServiceCustom(ObtainAccountWebPort obtainAccountWebPort) {
        this.obtainAccountWebPort = obtainAccountWebPort;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AccountWebModel account = obtainAccountWebPort.findByEmailAndStatusIsTrue(username)
                .map(MapToModel::accountWebService)
                .orElseThrow(() -> new BadRequestException("Account not found or is inactive: " + username, ""));


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
