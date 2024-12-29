package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.PlayerProfile;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.PasswordMismatchException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.dto.UpdatePasswordDTO;
import com.example.demo.model.dto.UpdateUserDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.PlayerProfileRepository;
import com.example.demo.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PlayerProfileRepository playerProfileRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllUsers() {
        // Arrange
        User user1 = new User(1L, "user1", "user1@example.com", "password", "avatar1");
        User user2 = new User(2L, "user2", "user2@example.com", "password", "avatar2");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldGetUserById() {
        // Arrange
        Long userId = 1L;
        User user = new User(userId, "user1", "user1@example.com", "password", "avatar1");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundById() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUserById(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldGetUserByUsername() {
        // Arrange
        String username = "user1";
        User user = new User(1L, username, "user1@example.com", "password", "avatar1");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserByUsername(username);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundByUsername() {
        // Arrange
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUserByUsername(username);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void shouldSaveUser() {
        // Arrange
        User user = new User(1L, "user1", "user1@example.com", "password", "avatar1");
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(playerProfileRepository, times(1)).save(any(PlayerProfile.class));
    }

    @Test
    void shouldDeleteUser() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void shouldCheckIfUsernameExists() {
        // Arrange
        String username = "user1";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean exists = userService.existsByUsername(username);

        // Assert
        assertTrue(exists);
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void shouldCheckIfEmailExists() {
        // Arrange
        String email = "user1@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean exists = userService.existsByEmail(email);

        // Assert
        assertTrue(exists);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void shouldUpdateUserById() {
        // Arrange
        Long userId = 1L;
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("newUser", "newEmail@example.com", "newAvatar");
        User user = new User(userId, "oldUser", "oldEmail@example.com", "password", "oldAvatar");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User updatedUser = userService.updateUserById(userId, updateUserDTO);

        // Assert
        assertNotNull(updatedUser);
        assertEquals("newUser", updatedUser.getUsername());
        assertEquals("newEmail@example.com", updatedUser.getEmail());
        assertEquals("newAvatar", updatedUser.getImgSrc());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        // Arrange
        Long userId = 1L;
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("newUser", "newEmail@example.com", "newAvatar");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUserById(userId, updateUserDTO);
        });
        assertEquals("User not found with id " + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldUpdatePassword() {
        // Arrange
        Long userId = 1L;
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setOldPassword("oldPassword");
        updatePasswordDTO.setNewPassword("newPassword");
        updatePasswordDTO.setConfirmPassword("newPassword");
        User user = new User(userId, "user1", "user1@example.com", "encodedOldPassword", "avatar1");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // Act
        userService.updatePassword(userId, updatePasswordDTO);

        // Assert
        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).matches("oldPassword", "encodedOldPassword");
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowExceptionWhenOldPasswordIsIncorrect() {
        // Arrange
        Long userId = 1L;
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setOldPassword("wrongPassword");
        updatePasswordDTO.setNewPassword("newPassword");
        updatePasswordDTO.setConfirmPassword("newPassword");
        User user = new User(userId, "user1", "user1@example.com", "encodedOldPassword", "avatar1");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedOldPassword")).thenReturn(false);

        // Act & Assert
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> {
            userService.updatePassword(userId, updatePasswordDTO);
        });
        assertEquals("旧密码不正确", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).matches("wrongPassword", "encodedOldPassword");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenNewPasswordsDoNotMatch() {
        // Arrange
        Long userId = 1L;
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setOldPassword("oldPassword");
        updatePasswordDTO.setNewPassword("newPassword");
        updatePasswordDTO.setConfirmPassword("differentPassword");
        User user = new User(userId, "user1", "user1@example.com", "encodedOldPassword", "avatar1");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);

        // Act & Assert
        PasswordMismatchException exception = assertThrows(PasswordMismatchException.class, () -> {
            userService.updatePassword(userId, updatePasswordDTO);
        });
        assertEquals("两次新密码不一致", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).matches("oldPassword", "encodedOldPassword");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
