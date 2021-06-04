package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.exceptions.BusinessLogicException;
import edu.lysak.railwaytickets.model.Role;
import edu.lysak.railwaytickets.model.User;
import edu.lysak.railwaytickets.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private Supplier<MessageDigest> digestFactory;

    @InjectMocks
    public UserService userService;

    @Test
    @DisplayName("#addUser(User) should return true if new user was successfully added")
    public void addUser_ShouldReturnTrueIfSuccessfullyAddUser() {
        User user = createUser();

        given(userRepository.findByEmail(any())).willReturn(null);
        given(digestFactory.get()).willReturn(createRealMessageDigest("SHA-256"));
        given(userRepository.save(any())).will(invocation -> invocation.getArgument(0));

        boolean result = userService.addUser(user);

        verify(userRepository).findByEmail(user.getEmail());
        verify(digestFactory).get();
        verify(userRepository).save(user);
        assertThat(result).isTrue();
        assertThat(user.getPassword()).isEqualTo("5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8");
    }

    @Test
    @DisplayName("#addUser(User) should return false if new user was not added")
    public void addUser_ShouldReturnFalseIfUserWithThisEmailAlreadyExist() {
        User user = createUser();
        given(userRepository.findByEmail(any())).willReturn(user);

        boolean result = userService.addUser(user);

        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository, never()).save(user);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("#findByEmailAndPassword(String, String) should find user by email and encoded password")
    public void findByEmailAndPassword_ShouldFindUserByEmailAndEncodedPassword() {
        User user = createUser();
        given(digestFactory.get()).willReturn(createRealMessageDigest("SHA-256"));
        given(userRepository.findByEmailAndPassword(any(), any())).willReturn(user);

        userService.findByEmailAndPassword("user@com.ua", "password");

        verify(digestFactory).get();
        verify(userRepository).findByEmailAndPassword("user@com.ua", "5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8");
    }

    private static User createUser() {
        User user = new User();
        user.setEmail("user@com.ua");
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword("password");
        return user;
    }

    private static MessageDigest createRealMessageDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException(exception);
        }
    }
}
