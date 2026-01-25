/* Copyright (c) 2026 Trackee */
package com.trackee.web.wallet;

import com.trackee.application.wallet.usecase.CreateWalletUseCase;
import com.trackee.application.wallet.usecase.GetAllWalletUseCase;
import com.trackee.application.wallet.usecase.GetWalletUseCase;
import com.trackee.application.wallet.usecase.UpdateWalletUseCase;
import com.trackee.shared.kernel.web.Response;
import com.trackee.web.wallet.request.CreateWalletRequest;
import com.trackee.web.wallet.request.UpdateWalletRequest;
import com.trackee.web.wallet.response.WalletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    UpdateWalletUseCase updateWalletUseCase;
    GetWalletUseCase getWalletUseCase;
    GetAllWalletUseCase getAllWalletUseCase;

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/")
    public Response<WalletResponse> createWallet(@RequestBody @Valid CreateWalletRequest request) {
        log.info("[/wallets]={}", request);

        return Response.of(createWalletUseCase.create(request));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/{walletId}")
    public Response<WalletResponse> updateWallet(
            @RequestBody @Valid UpdateWalletRequest request, @PathVariable UUID walletId) {
        log.info("[/wallets/{}]={}", walletId, request);

        return Response.of(updateWalletUseCase.update(walletId, request));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{walletId}")
    public Response<WalletResponse> getWallet(@PathVariable UUID walletId) {
        log.info("[/wallets/{}]", walletId);

        return Response.of(getWalletUseCase.getById(walletId));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/")
    public Response<List<WalletResponse>> getWallets() {
        log.info("[/wallets/]");

        return Response.of(getAllWalletUseCase.getAll());
    }
}
