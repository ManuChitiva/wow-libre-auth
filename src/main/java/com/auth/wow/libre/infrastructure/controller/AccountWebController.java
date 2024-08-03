package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.SearchModel;
import com.auth.wow.libre.domain.model.dto.AccountWebDto;
import com.auth.wow.libre.domain.model.security.JwtDto;
import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.auth.wow.libre.domain.model.shared.GenericResponseBuilder;
import com.auth.wow.libre.domain.ports.in.account_web.AccountWebPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_TRANSACTION_ID;

@RestController
@RequestMapping("/api/account/web")
public class AccountWebController {
    private final AccountWebPort accountWebPort;

    public AccountWebController(AccountWebPort accountWebPort) {
        this.accountWebPort = accountWebPort;
    }

    @GetMapping(path = "/search")
    public ResponseEntity<GenericResponse<SearchModel>> email(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = "email") final String email) {

        boolean existEmail = Optional.ofNullable(accountWebPort.findByEmail(email, transactionId)).isPresent();

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<SearchModel>(transactionId).ok(new SearchModel(existEmail)).build());
    }

    @PostMapping(path = "/create")
    public ResponseEntity<GenericResponse<JwtDto>> web(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid AccountWebDto accountWeb) {
        JwtDto jwtDto = accountWebPort.create(accountWeb, transactionId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseBuilder<>(jwtDto, transactionId).created().build());
    }
}
