package com.auth.wow.libre.application.services.account;

import com.auth.wow.libre.domain.model.AccountWebModel;
import com.auth.wow.libre.domain.model.dto.AccountChangePasswordDto;
import com.auth.wow.libre.domain.model.dto.AccountDetailDto;
import com.auth.wow.libre.domain.model.dto.AccountGameDto;
import com.auth.wow.libre.domain.model.dto.AccountsDetailDto;
import com.auth.wow.libre.domain.model.enums.Expansion;
import com.auth.wow.libre.domain.model.exception.FoundException;
import com.auth.wow.libre.domain.model.exception.GenericErrorException;
import com.auth.wow.libre.domain.model.exception.InternalException;
import com.auth.wow.libre.domain.model.exception.NotFoundException;
import com.auth.wow.libre.domain.ports.in.account.AccountPort;
import com.auth.wow.libre.domain.ports.in.account_banned.AccountBannedPort;
import com.auth.wow.libre.domain.ports.in.account_web.AccountWebPort;
import com.auth.wow.libre.domain.ports.out.account.ObtainAccountPort;
import com.auth.wow.libre.domain.ports.out.account.SaveAccountPort;
import com.auth.wow.libre.infrastructure.entities.AccountEntity;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService implements AccountPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private static final int LIMIT_ACCOUNT = 10;
    private final AccountWebPort accountWebPort;
    private final ObtainAccountPort obtainAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final AccountBannedPort accountBannedPort;
    private final PasswordEncoder passwordEncoder;


    public AccountService(AccountWebPort accountWebPort, PasswordEncoder passwordEncoder,
                          ObtainAccountPort obtainAccountPort,
                          SaveAccountPort saveAccountPort, AccountBannedPort accountBannedPort) {
        this.accountWebPort = accountWebPort;
        this.passwordEncoder = passwordEncoder;
        this.obtainAccountPort = obtainAccountPort;
        this.saveAccountPort = saveAccountPort;
        this.accountBannedPort = accountBannedPort;
    }

    @Override
    public List<AccountsDetailDto> availableAccounts(String email, String transactionId) {

        final AccountWebModel accountWebModel = getAccountWebModel(email, transactionId);

        List<AccountEntity> accounts = obtainAccountPort.findByAccountWebId(accountWebModel.id);
        return accounts.stream().map(account -> {
                    Expansion expansion = Expansion.getById(Integer.parseInt(account.getExpansion()));

                    return new AccountsDetailDto(account.getId(),
                            account.getUsername(),
                            account.getEmail(),
                            expansion.getLogo(),
                            expansion.getDisplayName(),
                            account.isOnline(),
                            account.getFailedLogins(),
                            account.getJoinDate(),
                            account.getLastIp());
                }

        ).collect(Collectors.toList());
    }

    @Override
    public void createGame(AccountGameDto accountGameDto, String email, String transactionId) {

        if (obtainAccountPort.findByUsername(accountGameDto.getUsername()).isPresent()) {
            throw new FoundException("There is already a registered client with this data", transactionId);
        }

        final AccountWebModel accountWebModel = getAccountWebModel(email, transactionId);

        List<AccountEntity> accounts = obtainAccountPort.findByAccountWebId(accountWebModel.id);

        if (accounts.size() >= LIMIT_ACCOUNT) {
            throw new GenericErrorException(
                    transactionId, "You have exceeded the maximum number of accounts created.", HttpStatus.CONFLICT);
        }


        AccountWebModel accountWeb = accountWebPort.findByEmail(email, transactionId);

        if (accountWeb == null) {
            LOGGER.error("You do not have an administrative account or it is not valid, please contact support.  " +
                            "TransactionId: [{}]",
                    transactionId);
            throw new NotFoundException("You do not have an administrative account or it is not valid, please contact" +
                    " support.", transactionId);
        }

        final String username = accountGameDto.getUsername();
        byte[] verifier = hexToByte(accountGameDto.getVerifier(), transactionId);
        byte[] salt = hexToByte(accountGameDto.getSalt(), transactionId);

        AccountEntity account = new AccountEntity();
        account.setSalt(salt);
        account.setVerifier(verifier);
        account.setLocked(false);
        account.setUsername(username);
        account.setEmail(accountWeb.email);
        account.setAccountWeb(AccountWebEntity.fromDomainModel(accountWeb));

        saveAccountPort.save(account);

    }

    @Override
    public AccountDetailDto detail(Long accountId, String email, String transactionId) {
        final AccountWebModel accountWebModel = getAccountWebModel(email, transactionId);


        AccountDetailDto.AccountWeb accountWeb =
                AccountDetailDto.AccountWeb.builder()
                        .id(accountWebModel.id)
                        .cellPhone(accountWebModel.cellPhone)
                        .country(accountWebModel.country)
                        .dateOfBirth(accountWebModel.dateOfBirth)
                        .email(accountWebModel.email)
                        .firstName(accountWebModel.firstName)
                        .lastName(accountWebModel.lastName)
                        .rolName(accountWebModel.rolName)
                        .status(accountWebModel.status)
                        .verified(accountWebModel.verified)
                        .build();


        return obtainAccountPort.findByIdAndAccountWeb(accountId, accountWeb.id, transactionId).map(account ->
                new AccountDetailDto(account.getId(), account.getUsername(), account.getEmail(),
                        account.getExpansion(), account.isOnline(), account.getFailedLogins(),
                        account.getJoinDate(),
                        account.getLastIp(), account.getMuteReason(), account.getMuteBy(),
                        account.getMuteTime() != null && account.getMuteTime() > 0,
                        account.getLastLogin(), account.getOs(), accountWeb,
                        accountBannedPort.getAccountBanned(accountId))
        ).orElseThrow(() -> new NotFoundException("There is no associated account or it is not available.",
                transactionId));
    }

    @Override
    public void changePasswordAccountGame(AccountChangePasswordDto accountChangePasswordDto, String email,
                                          String transactionId) {

        AccountWebModel accountWebModel = getAccountWebModel(email, transactionId);

        if (!passwordEncoder.matches(accountChangePasswordDto.getPassword(), accountWebModel.password)) {
            throw new GenericErrorException(transactionId, "The account password is invalid",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Optional<AccountEntity> account =
                obtainAccountPort.findById(accountChangePasswordDto.getAccountId());

        if (account.isEmpty()) {
            throw new NotFoundException("You do not have an administrative account or it is not valid, please contact" +
                    " support.", transactionId);
        }

        byte[] verifier = hexToByte(accountChangePasswordDto.getVerifier(), transactionId);
        byte[] salt = hexToByte(accountChangePasswordDto.getSalt(), transactionId);

        AccountEntity accountModify = account.get();
        accountModify.setVerifier(verifier);
        accountModify.setSalt(salt);
        saveAccountPort.save(accountModify);
    }


    private AccountWebModel getAccountWebModel(String email, String transactionId) {
        final AccountWebModel accountWebModel = accountWebPort.findByEmail(email, transactionId);

        if (accountWebModel == null) {
            LOGGER.error("You do not have an administrative account or it is not valid, please contact support.  " +
                            "TransactionId: [{}]",
                    transactionId);
            throw new NotFoundException("You do not have an administrative account or it is not valid, please contact" +
                    " support.", transactionId);
        }

        return accountWebModel;
    }

    private byte[] hexToByte(String hex, String transactionId) {
        try {
            return Hex.decodeHex(hex);
        } catch (DecoderException e) {
            LOGGER.error("Decoder Error Password. Message: [{}] -  TransactionId: [{}]",
                    e.getMessage(), transactionId);
            throw new InternalException("An unexpected error has occurred internally, please try again later.",
                    transactionId);
        }
    }

    @Override
    public boolean findByAccountIdAndAccountWebId(Long accountId, Long accountWebId, String transactionId) {
        return obtainAccountPort.findByIdAndAccountWeb(accountId, accountWebId, transactionId).isPresent();
    }

}
