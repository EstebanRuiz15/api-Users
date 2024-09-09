package com.emazon.api_users.Auxiliar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.emazon.api_users.domain.exceptions.ErrorExceptionParam;
import com.emazon.api_users.domain.interfaces.IUserRepositoryPort;
import com.emazon.api_users.domain.model.RoleEnum;
import com.emazon.api_users.domain.model.User;
import com.emazon.api_users.domain.services.UserServiceImpl;
import com.emazon.api_users.domain.util.ConstantsDomain;

@SpringBootTest
 class UserServiceTest {
    @Mock
    private IUserRepositoryPort repositoryPort;

    @InjectMocks
    private UserServiceImpl userService;

    private User existingUser = new User(1L, "John", "Doe", "12345678", "555-1234", LocalDate.of(2000, 1, 1), 
    "john@example.com", "password123", RoleEnum.AUX_BODEGA);

    private User newUser = new User(1L, "John", "Doe", "12345678", "555-1234", LocalDate.of(2000, 1, 1), 
    "john@example.com", "password123", RoleEnum.AUX_BODEGA);
    @BeforeEach
    void setUp() {
        repositoryPort=mock(IUserRepositoryPort.class);
        userService=new UserServiceImpl(repositoryPort);
    }

    @Test
    void createUserAuxBod_whenEmailExists_throwsErrorExceptionParam() {
        existingUser.setEmail("existing@example.com");
        when(repositoryPort.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));
        newUser.setEmail("existing@example.com");
        newUser.setDateOfBirth(LocalDate.of(2000, 1, 1));
        newUser.setRole(RoleEnum.AUX_BODEGA);
        ErrorExceptionParam exception = assertThrows(ErrorExceptionParam.class, () -> userService.createUserAuxBod(newUser));
        assertEquals(ConstantsDomain.EMAIL_AL_READY_EXIST_ERROR_MESSAGE, exception.getMessage());

        verify(repositoryPort, never()).saveUser(any());
    }

    @Test
    void createUserAuxBod_whenUserIsUnderage_throwsErrorExceptionParam() {
        newUser.setEmail("newuser@example.com");
        newUser.setDateOfBirth(LocalDate.now().minusYears(17)); 
        newUser.setRole(RoleEnum.AUX_BODEGA);

        when(repositoryPort.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        ErrorExceptionParam exception = assertThrows(ErrorExceptionParam.class, () -> userService.createUserAuxBod(newUser));
        assertEquals(ConstantsDomain.AGE_MAY_ERROR, exception.getMessage());

        verify(repositoryPort, never()).saveUser(any());
    }

    @Test
    void createUserAuxBod_whenRoleIsInvalid_throwsErrorExceptionParam() {
        newUser.setEmail("newuser@example.com");
        newUser.setDateOfBirth(LocalDate.of(2000, 1, 1));
        newUser.setRole(RoleEnum.OTHER); 

        when(repositoryPort.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());

        
        ErrorExceptionParam exception = assertThrows(ErrorExceptionParam.class, () -> userService.createUserAuxBod(newUser));
        assertEquals(ConstantsDomain.ROLE_ERROR_MESSAGE, exception.getMessage());

        verify(repositoryPort, never()).saveUser(any());
    }

    @Test
    void createUserAuxBod_whenAllValidationsPass_savesUser() {
        newUser.setEmail("newuser@example.com");
        newUser.setDateOfBirth(LocalDate.of(2000, 1, 1)); 
        newUser.setRole(RoleEnum.AUX_BODEGA); 

        when(repositoryPort.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());

        // Act
        userService.createUserAuxBod(newUser);

        // Assert
        verify(repositoryPort, times(1)).saveUser(newUser);
    }
}

