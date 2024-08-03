package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.AccountChangePasswordDto;
import com.auth.wow.libre.domain.model.dto.AccountDetailDto;
import com.auth.wow.libre.domain.model.dto.AccountGameDto;
import com.auth.wow.libre.domain.model.dto.AccountsDetailDto;
import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.auth.wow.libre.domain.model.shared.GenericResponseBuilder;
import com.auth.wow.libre.domain.ports.in.account.AccountPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_EMAIL_JWT;
import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_TRANSACTION_ID;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountPort accountPort;

    public AccountController(AccountPort accountPort) {
        this.accountPort = accountPort;
    }

    @GetMapping(path = "/")
    public ResponseEntity<GenericResponse<List<AccountsDetailDto>>> accounts(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_EMAIL_JWT) final String email) {

        final List<AccountsDetailDto> accountFound = accountPort.availableAccounts(email, transactionId);

        if (accountFound != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponseBuilder<List<AccountsDetailDto>>(transactionId).ok(accountFound).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{account_id}")
    public ResponseEntity<GenericResponse<AccountDetailDto>> account(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_EMAIL_JWT) final String email,
            @PathVariable final Long account_id) {

        final AccountDetailDto account = accountPort.detail(account_id, email, transactionId);

        if (account != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponseBuilder<AccountDetailDto>(transactionId).ok(account).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(path = "/games/create")
    public ResponseEntity<GenericResponse<Void>> game(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid AccountGameDto account,
            @RequestHeader(name = HEADER_EMAIL_JWT) final String email) {
        accountPort.createGame(account, email, transactionId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }

    @PutMapping(path = "/games/change-password")
    public ResponseEntity<GenericResponse<Void>> changePasswordGame(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid AccountChangePasswordDto account,
            @RequestHeader(name = HEADER_EMAIL_JWT) final String email) {

        accountPort.changePasswordAccountGame(account, email, transactionId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }

    @GetMapping(path = "/verify")
    public ResponseEntity<GenericResponse<Boolean>> verify(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = "account_id") final Long accountId,
            @RequestParam(name = "account_web_id") final Long accountWebId) {
            System.out.println(transactionId);
        final boolean existAccount = accountPort.findByAccountIdAndAccountWebId(accountId, accountWebId, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Boolean>(transactionId).ok(existAccount).build());

    }
}
