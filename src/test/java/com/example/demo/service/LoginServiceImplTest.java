package com.example.demo.service;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {

    @Mock
    private OkHttpClient httpClient;

    @Mock
    private Call call;

    @Mock
    private Response response;

    @InjectMocks
    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() throws IOException {
        when(httpClient.newCall(any(Request.class))).thenReturn(call);
    }

    @Test
    void testAuthenticate_Success() throws IOException {
        when(call.execute()).thenReturn(response);
        when(response.isSuccessful()).thenReturn(true);

        boolean result = loginService.authenticate("testuser", "testpass");

        assertTrue(result);
    }

    @Test
    void testAuthenticate_Failure() throws IOException {
        when(call.execute()).thenReturn(response);
        when(response.isSuccessful()).thenReturn(false);

        boolean result = loginService.authenticate("testuser", "testpass");

        assertFalse(result);
    }

    @Test
    void testAuthenticate_IOException() throws IOException {
        when(call.execute()).thenThrow(new IOException());

        boolean result = loginService.authenticate("testuser", "testpass");

        assertFalse(result);
    }
}
