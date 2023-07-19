package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.UserRepository;
import com.sapred.ordermanagerred.resolver.UserResolver;
import com.sapred.ordermanagerred.security.JwtToken;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;


@ExtendWith({MockitoExtension.class, UserResolver.class})
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtToken jwtToken;

    @BeforeEach
    private void setUp() {
        System.out.print("~~~~~~~~~~~~~~ TEST: ");
    }

    @Test
    public void testLogIn_whenEmailNoFound_throwException() {
        when(this.userRepository.getByAddressEmail("jon@gmail.com")).thenReturn(null);

        assertThatThrownBy(() -> userService.logIn("jon@gmail.com", "1234")).isInstanceOf(ResponseStatusException.class);

        System.out.println("testLogIn_whenEmailNoFound ✅");
    }

    @Test
    public void testDeleteUser_whenDeletingWithoutPermission_ThrowsNoPermissionException(User user) {
        user.setRoleId(Role.builder().name(RoleOptions.CUSTOMER).id("3").build());
        String token = "token";

        when(jwtToken.getRoleIdFromToken(token)).thenReturn(user.getRoleId().getName());
        when(jwtToken.getCompanyIdFromToken(token)).thenReturn(user.getCompanyId().getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.deleteUser(token, user.getId())).isInstanceOf(NoPermissionException.class);

        System.out.println("testDeleteUser_whenDeletingWithoutPermission ✅");
    }

    @Test
    public void testDeleteUser_whenSuccessfullyDeletesUser_finish(User user) {
        user.setRoleId(Role.builder().name(RoleOptions.EMPLOYEE).id("3").build());
        String token = "token";

        when(jwtToken.getRoleIdFromToken(token)).thenReturn(user.getRoleId().getName());
        when(jwtToken.getCompanyIdFromToken(token)).thenReturn(user.getCompanyId().getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUser(token, user.getId());

        verify(jwtToken).getRoleIdFromToken(token);
        verify(jwtToken).getCompanyIdFromToken(token);
        verify(userRepository).findById(user.getId());
        verify(userRepository).deleteById(user.getId());

        System.out.println("testDeleteUser_whenSuccessfullyDeletesUser ✅");
    }

}

