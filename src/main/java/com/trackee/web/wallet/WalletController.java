/* Copyright (c) 2026 Trackee */
package com.trackee.web.wallet;

import com.trackee.application.wallet.usecase.CreateWalletUseCase;
import com.trackee.shared.kernel.web.Response;
import com.trackee.web.wallet.request.CreateWalletRequest;
import com.trackee.web.wallet.response.WalletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vandunxg
 */
@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
@Slf4j(topic = "WALLET-CONTROLLER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletController {

    CreateWalletUseCase createWalletUseCase;

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/")
    public Response<WalletResponse> createWallet(@RequestBody @Valid CreateWalletRequest request) {
        log.info("[/wallets]={}", request);

        return Response.of(createWalletUseCase.createWallet(request));
    }
}
