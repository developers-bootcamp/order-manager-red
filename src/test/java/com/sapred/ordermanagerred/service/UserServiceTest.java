package com.sapred.ordermanagerred.service;

import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.sapred.ordermanagerred.model.User;
import com.sapred.ordermanagerred.repository.UserRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtToken jwtToken;

    private UserService userService;

//    @BeforeEach
//    void setUp() {
//        userService = new UserService(userRepository, jwtToken);
//    }

    @Test
    void logIn_validEmailAndPassword_returnsJWTToken() throws Exception {
        // Arrange
        String email = "test@example.com";
        String password = "testpassword";
        User user = new User();
        user.getAddress().setEmail(email);
        user.setPassword(password);
        Mockito.when(userRepository.getByAddressEmail(email)).thenReturn(user);

        // Act
        String token = userService.logIn(email, password);

        // Assert
        assertNotNull(token);
        assertEquals("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIiwiZXhwIjoxNjI4NDI0MDE1LCJpYXQiOjE2MjgwMjYwMTUsImp0aSI6IjEiLCJleHAiOjE2MjgwNDUwMTEsInVzZXJfaWQiOiIiLCJyb2xlIjoiQWRtaW4iLCJlbWFpbCI6ImpvaG5AZ21haWwuY29tIn0.dRnM7rW35ms1yV89aR2v_k48t4z06t62I4m68q99hL0", token);
    }

    @Test
    void logIn_invalidEmail_throwsResponseStatusException() throws Exception {
        // Arrange
        String email = "invalidemail@example.com";
        Mockito.when(userRepository.getByAddressEmail(email)).thenReturn(null);

        // Act
        assertThrows(ResponseStatusException.class, () -> userService.logIn(email, "testpassword"));
    }

    @Test
    void logIn_invalidPassword_throwsResponseStatusException() throws Exception {
        // Arrange
        String email = "test@example.com";
        String password = "invalidpassword";
        User user = new User();
        user.getAddress().setEmail(email);
        user.setPassword("testpassword");
        Mockito.when(userRepository.getByAddressEmail(email)).thenReturn(user);

        // Act
        assertThrows(ResponseStatusException.class, () -> userService.logIn(email, password));
    }
}
