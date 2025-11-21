package com.vandunxg.trackee.auth.application.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vandunxg.trackee.auth.api.dto.RegisterRequest;
import com.vandunxg.trackee.auth.api.dto.RegisterResponse;
import com.vandunxg.trackee.common.error.ErrorCode;
import com.vandunxg.trackee.common.exception.BusinessException;
import com.vandunxg.trackee.users.application.adapter.UserAdapter;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock UserAdapter userAdapter;

    @InjectMocks AuthServiceImpl authService;

    @Test
    void shouldReturnUserId_whenRegisterSuccess() {
        // Arrange
        RegisterRequest req =
                new RegisterRequest(
                        "Nguyen Van Dung", "vandunxg@gmail.com", "Password@123", "WEB", "ABC_XYZ");
        UUID fakeId = UUID.randomUUID();

        when(userAdapter.createdUser(req)).thenReturn(fakeId);

        // Act
        RegisterResponse res = authService.register(req);

        // Assert
        assertNotNull(res);
        assertEquals(fakeId, res.userId());

        // Verify interaction
        verify(userAdapter).createdUser(req);
    }

    @Test
    void shouldPropagateException_whenAdapterThrows() {
        // Arrange
        RegisterRequest req =
                new RegisterRequest(
                        "Nguyen Van Dung", "vandunxg@gmail.com", "Password@123", "WEB", "ABC_XYZ");

        when(userAdapter.createdUser(req))
                .thenThrow(new BusinessException(ErrorCode.USER_EMAIL_EXISTS));

        // Act + Assert
        BusinessException ex =
                assertThrows(BusinessException.class, () -> authService.register(req));

        assertEquals(ErrorCode.USER_EMAIL_EXISTS, ex.getErrorCode());

        verify(userAdapter).createdUser(req);
    }
}
