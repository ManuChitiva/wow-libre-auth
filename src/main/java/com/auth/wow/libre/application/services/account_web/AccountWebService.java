package com.auth.wow.libre.application.services.account_web;

import com.auth.wow.libre.domain.mapper.MapToModel;
import com.auth.wow.libre.domain.model.AccountWebModel;
import com.auth.wow.libre.domain.model.RolModel;
import com.auth.wow.libre.domain.model.comunication.MailSenderVars;
import com.auth.wow.libre.domain.model.dto.AccountWebDto;
import com.auth.wow.libre.domain.model.exception.FoundException;
import com.auth.wow.libre.domain.model.exception.NotFoundException;
import com.auth.wow.libre.domain.model.security.CustomUserDetails;
import com.auth.wow.libre.domain.model.security.JwtDto;
import com.auth.wow.libre.domain.ports.in.account_web.AccountWebPort;
import com.auth.wow.libre.domain.ports.in.jwt.JwtPort;
import com.auth.wow.libre.domain.ports.in.rol.RolPort;
import com.auth.wow.libre.domain.ports.out.account_web.ObtainAccountWebPort;
import com.auth.wow.libre.domain.ports.out.account_web.SaveAccountWebPort;
import com.auth.wow.libre.infrastructure.conf.comunication.EmailSend;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import com.auth.wow.libre.infrastructure.entities.RolEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AccountWebService implements AccountWebPort {
    private static final String PICTURE_DEFAULT_PROFILE_WEB = "https://i.ibb.co/M8Kfq9X/icon-Default.png";
    private static final String ROL_CLIENT_DEFAULT = "CLIENT";
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountWebService.class);

    private final SaveAccountWebPort saveAccountWebPort;
    private final ObtainAccountWebPort obtainAccountWebPort;
    private final PasswordEncoder passwordEncoder;
    private final RolPort rolPort;
    private final JwtPort jwtPort;
    private final EmailSend emailSend;

    public AccountWebService(SaveAccountWebPort saveAccountWebPort,
                             ObtainAccountWebPort obtainAccountWebPort, PasswordEncoder passwordEncoder,
                             RolPort rolPort, JwtPort jwtPort, EmailSend emailSend) {
        this.saveAccountWebPort = saveAccountWebPort;
        this.obtainAccountWebPort = obtainAccountWebPort;
        this.passwordEncoder = passwordEncoder;
        this.rolPort = rolPort;
        this.jwtPort = jwtPort;
        this.emailSend = emailSend;
    }


    @Override
    public AccountWebModel save(AccountWebModel accountWebModel, RolModel rol, String transactionId) {
        final AccountWebEntity accountWebEntity = AccountWebEntity.create(accountWebModel,
                RolEntity.mapToAccountRolEntity(rol));

        return MapToModel.accountWebService(saveAccountWebPort.save(accountWebEntity, transactionId));
    }

    @Override
    public AccountWebModel findByEmail(String email, String transactionId) {
        return obtainAccountWebPort.findByEmailAndStatusIsTrue(email).map(MapToModel::accountWebService).orElse(null);
    }

    @Override
    public JwtDto create(AccountWebDto accountWebDto, String transactionId) {

        if (findByEmail(accountWebDto.getEmail(), transactionId) != null) {
            throw new FoundException("There is already a registered client with this data", transactionId);
        }

        final String passwordEncode = passwordEncoder.encode(accountWebDto.getPassword());

        AccountWebModel accountWebModel = new AccountWebModel(accountWebDto.getCountry(),
                accountWebDto.getDateOfBirth(), accountWebDto.getFirstName(), accountWebDto.getLastName(),
                accountWebDto.getCellPhone(), accountWebDto.getEmail(), passwordEncode, true, false,
                PICTURE_DEFAULT_PROFILE_WEB);

        final RolModel rolModel = rolPort.findByName(ROL_CLIENT_DEFAULT, transactionId);

        if (rolModel == null) {
            LOGGER.error("An error occurred while assigning a role.  TransactionId: [{}]",
                    transactionId);
            throw new NotFoundException("An error occurred while assigning a role.", transactionId);
        }

        final Long id = save(accountWebModel, rolModel, transactionId).id;

        CustomUserDetails customUserDetails = new CustomUserDetails(
                List.of(rolModel),
                passwordEncode,
                accountWebModel.email,
                true,
                true,
                true,
                true,
                id,
                PICTURE_DEFAULT_PROFILE_WEB
        );

        final String token = jwtPort.generateToken(customUserDetails);
        final Date expiration = jwtPort.extractExpiration(token);
        final String refreshToken = jwtPort.generateRefreshToken(customUserDetails);
        final String email = accountWebDto.getEmail();

        emailSend.sendHTMLEmail(email, "Bienvenido, Su cuenta ha sido creada exitosamente, " +
                        "Por favor verifique su correo",
                MailSenderVars.builder().email(email).transactionId(transactionId).build());

        return new JwtDto(token, refreshToken, expiration, PICTURE_DEFAULT_PROFILE_WEB);
    }


}
