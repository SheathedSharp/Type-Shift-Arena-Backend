package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.user.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FriendServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendService friendService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetFriends() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        User friend1 = new User();
        friend1.setId(2L);
        friend1.setUsername("friend1");
        User friend2 = new User();
        friend2.setId(3L);
        friend2.setUsername("friend2");
        user.setFriends(Set.of(friend1, friend2));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Set<User> friends = friendService.getFriends(userId);

        // Assert
        assertNotNull(friends);
        assertEquals(2, friends.size());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenGettingFriendsForNonExistentUser() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendService.getFriends(userId);
        });
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldSearchFriends() {
        // Arrange
        String username = "john";
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("john_doe");
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("john_smith");
        List<User> users = List.of(user1, user2);

        when(userRepository.findByUsernameContaining(username)).thenReturn(users);

        // Act
        List<User> result = friendService.searchFriends(username);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findByUsernameContaining(username);
    }

    @Test
    void shouldReturnEmptyListWhenSearchingFriendsWithNoMatch() {
        // Arrange
        String username = "nonexistent";
        when(userRepository.findByUsernameContaining(username)).thenReturn(Collections.emptyList());

        // Act
        List<User> result = friendService.searchFriends(username);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByUsernameContaining(username);
    }

    @Test
    void shouldAddFriend() {
        // Arrange
        Long userId = 1L;
        Long friendId = 2L;
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");
        User friend = new User();
        friend.setId(friendId);
        friend.setUsername("friend1");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friend));

        // Act
        friendService.addFriend(userId, friendId);

        // Assert
        assertTrue(user.getFriends().contains(friend));
        assertTrue(friend.getFriends().contains(user));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).findById(friendId);
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingFriendForNonExistentUser() {
        // Arrange
        Long userId = 1L;
        Long friendId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendService.addFriend(userId, friendId);
        });
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).findById(friendId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingNonExistentFriend() {
        // Arrange
        Long userId = 1L;
        Long friendId = 2L;
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendService.addFriend(userId, friendId);
        });
        assertEquals("Friend not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).findById(friendId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldRemoveFriend() {
        // Arrange
        Long userId = 1L;
        Long friendId = 2L;
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");
        User friend = new User();
        friend.setId(friendId);
        friend.setUsername("friend1");
        user.getFriends().add(friend);
        friend.getFriends().add(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friend));

        // Act
        friendService.removeFriend(userId, friendId);

        // Assert
        assertFalse(user.getFriends().contains(friend));
        assertFalse(friend.getFriends().contains(user));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).findById(friendId);
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenRemovingFriendForNonExistentUser() {
        // Arrange
        Long userId = 1L;
        Long friendId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendService.removeFriend(userId, friendId);
        });
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).findById(friendId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentFriend() {
        // Arrange
        Long userId = 1L;
        Long friendId = 2L;
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendService.removeFriend(userId, friendId);
        });
        assertEquals("Friend not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).findById(friendId);
        verify(userRepository, never()).save(any(User.class));
    }
}
