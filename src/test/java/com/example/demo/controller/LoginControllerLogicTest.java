package com.example.demo.controller;

import com.example.demo.service.FirebaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginControllerLogicTest {

    @Mock
    private FirebaseService firebaseService;

    @InjectMocks
    private LoginController loginController;

    @Test
    public void testLoginSuccess() {
        // Given
        when(firebaseService.authenticate(anyString(), anyString())).thenReturn(true);

        // When
        loginController.login();

        // Then
        // I can't verify the scene change, so I will verify that the authenticate method was called.
        verify(firebaseService).authenticate("testuser", "testpass");
    }
}
