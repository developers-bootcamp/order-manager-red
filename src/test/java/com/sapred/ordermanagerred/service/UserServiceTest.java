package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.UserNameDTO;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.UserRepository;
import com.sapred.ordermanagerred.resolver.UserResolver;
import com.sapred.ordermanagerred.security.JwtToken;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, UserResolver.class})
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtToken jwtToken;

    @Mock
    private MongoTemplate mongoTemplate;


    @BeforeEach
    private void setUp() {
        System.out.print("~~~~~~~~~~~~~~ TEST: ");
    }

    @Test
    public void testLogIn_whenEmailNoFound_throwException() {
        when(this.userRepository.getByAddressEmail("jon@gmail.com")).thenReturn(null);

        assertThatThrownBy(() -> userService.logIn("jon@gmail.com", "1234")).isInstanceOf(ResponseStatusException.class);

        System.out.println("✅ testLogIn_whenEmailNoFound");
    }

    @Test
    public void testDeleteUser_whenDeletingWithoutPermission_ThrowsNoPermissionException(User user) {
        user.setRoleId(Role.builder().name(RoleOptions.EMPLOYEE).id("2").build());
        String token = "token";

        User userToDelete = User.builder().id("2")
                .companyId(Company.builder().id(user.getId()).build())
                .roleId(Role.builder().name(RoleOptions.ADMIN).id("1").build()).build();

        when(jwtToken.getRoleIdFromToken(token)).thenReturn(user.getRoleId().getName());
        when(jwtToken.getCompanyIdFromToken(token)).thenReturn(user.getCompanyId().getId());
        when(userRepository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));

        assertThatThrownBy(() -> userService.deleteUser(token, userToDelete.getId())).isInstanceOf(NoPermissionException.class);

        System.out.println("✅ testDeleteUser_whenDeletingWithoutPermission");
    }

    @Test
    public void testDeleteUser_whenSuccessfullyDeletesUser(User user) {
        user.setRoleId(Role.builder().name(RoleOptions.EMPLOYEE).id("2").build());
        String token = "token";

        User userToDelete = User.builder().id("2")
                .companyId(Company.builder().id(user.getId()).build())
                .roleId(Role.builder().name(RoleOptions.CUSTOMER).id("2").build()).build();

        when(jwtToken.getRoleIdFromToken(token)).thenReturn(user.getRoleId().getName());
        when(jwtToken.getCompanyIdFromToken(token)).thenReturn(user.getCompanyId().getId());
        when(userRepository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));

        userService.deleteUser(token, userToDelete.getId());
        verify(userRepository).deleteById(userToDelete.getId());

        System.out.println("✅ testDeleteUser_whenSuccessfullyDeletesUser");
    }


    @Test
    public void testGetNamesOfCustomersByPrefix_WhenPrefixIsEmpty_ThenEmptyListIsReturned() {
        String token = "token";
        String prefix = "";

        List<UserNameDTO> namesOfCustomersByPrefix = userService.getNamesOfCustomersByPrefix(token, prefix);

        assertTrue(namesOfCustomersByPrefix.isEmpty());

        System.out.println("✅ testGetNamesOfCustomersByPrefix_WhenPrefixIsEmpty");
    }

    @Test
    public void testUpdateUser_WhenUserFoundAndHasPermissionAndEmailNotExists_ThenReturnUpdatedUser() {
        String token = "token";
        String userId = "userId";
        User userToEdit = User.builder().address(Address.builder().email("email").build()).build();

        User findUser = User.builder().companyId(Company.builder().id("companyId").build()).build();

        when(jwtToken.getRoleIdFromToken(anyString())).thenReturn(RoleOptions.ADMIN);
        when(jwtToken.getCompanyIdFromToken(anyString())).thenReturn("companyId");
        when(userRepository.findById(anyString())).thenReturn(Optional.of(findUser));
        when(userRepository.existsByAddressEmail(anyString())).thenReturn(false);
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(User.class)))
                .thenReturn(new User());

        User result = userService.updateUser(token, userId, userToEdit);

        assertNotNull(result);

        System.out.println("✅ testUpdateUser_WhenUserFoundAndHasPermissionAndEmailNotExists");
    }

    @Test
    public void testUpdateUser_WhenUserNotFound_ThenThrowNotFoundException() {
        String token = "token";
        String userId = "userId";
        User userToEdit = new User();

        when(jwtToken.getRoleIdFromToken(anyString())).thenReturn(RoleOptions.ADMIN);
        when(jwtToken.getCompanyIdFromToken(anyString())).thenReturn("companyId");
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.updateUser(token, userId, userToEdit);
        });

        System.out.println("✅ testUpdateUser_WhenUserNotFound");
    }

}

