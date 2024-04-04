package net.dunice.mk.mkhachemizov_testserver;

import java.util.Optional;
import java.util.UUID;

import net.dunice.mk.mkhachemizov_testserver.constants.ServerErrorCodes;
import net.dunice.mk.mkhachemizov_testserver.dto.*;
import net.dunice.mk.mkhachemizov_testserver.entity.UserEntity;
import net.dunice.mk.mkhachemizov_testserver.exception.CustomException;
import net.dunice.mk.mkhachemizov_testserver.mapper.UserMapper;
import net.dunice.mk.mkhachemizov_testserver.repository.PublicUserView;
import net.dunice.mk.mkhachemizov_testserver.repository.UserRepository;
import net.dunice.mk.mkhachemizov_testserver.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    private final String ID = "fd385c23-c236-4d63-9143-0e409c4a54e7";

    private static final UserMapper mockUserMapper = new UserMapperImpl();
    private static UserEntity user;

    @BeforeEach
    void setUp() {
        RegisterUserDto registrationRequest = new RegisterUserDto();
        String avatar = "avatar.jpg";
        registrationRequest.setAvatar(avatar);
        String email = "email@test.ru";
        registrationRequest.setEmail(email);
        String name = "name";
        registrationRequest.setName(name);
        String password = "password";
        registrationRequest.setPassword(password);
        String role = "user";
        registrationRequest.setRole(role);
        user = mockUserMapper.registerUserDtoToUserEntity(registrationRequest);
        user.setPassword(new BCryptPasswordEncoder().encode(registrationRequest.getPassword()));
        user.setId(UUID.fromString(ID));
    }

    @Description(value = "Testing request of getting user info via correct id")
    @Test
    void testCorrectGetUserInfo() {
        when(userRepository.findById(UUID.fromString(ID))).thenReturn(Optional.of(user));
        assertNotNull(userRepository.findById(UUID.fromString(ID)));
        PublicUserView publicUserView;
        publicUserView = mockUserMapper.userEntityToPublicUserView(user);
        assertNotNull(mockUserMapper.userEntityToPublicUserView(user));
        when(userMapper.userEntityToPublicUserView(any(UserEntity.class))).thenReturn(publicUserView);
        assertNotNull(userMapper.userEntityToPublicUserView(user));
        CustomSuccessResponse<PublicUserView> response = userService.getUserInfoById(UUID.fromString(ID));
        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(publicUserView, response.getData());
        assertEquals(1, response.getStatusCode());
        assertNull(response.getCodes());
    }

    @Description(value = "Testing request of getting user info via incorrect id")
    @Test
    void testIncorrectGetUserInfo() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        CustomException exception = assertThrows(CustomException.class, () -> userService.getUserInfoById(UUID.fromString(ID)));
        assertEquals(ServerErrorCodes.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Description(value = "Testing request of deleting user via correct id")
    @Test
    void testCorrectDeleteUser() {
        when(userRepository.findById(UUID.fromString(ID))).thenReturn(Optional.of(user));
        assertNotNull(userRepository.findById(UUID.fromString(ID)));
        BaseSuccessResponse response = userService.deleteUser(UUID.fromString(ID));
        assertNotNull(response);
        assertEquals(1, response.getStatusCode());
    }

    @Description(value = "Testing request of deleting user via incorrect id")
    @Test
    void testIncorrectDeleteUser() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        CustomException exception = assertThrows(CustomException.class, () -> userService.deleteUser(UUID.fromString(ID)));
        assertEquals(ServerErrorCodes.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Description(value = "Testing request of replacing user via correct id")
    @Test
    void testCorrectReplaceUser() {
        when(userRepository.findById(UUID.fromString(ID))).thenReturn(Optional.of(user));
        assertNotNull(userRepository.findById(UUID.fromString(ID)));
        PutUserDto putUserDto = new PutUserDto();
        PutUserDtoResponse putUserDtoResponse = new PutUserDtoResponse();
        putUserDto.setAvatar("avatar1.jpg");
        assertNotNull(putUserDto.getAvatar());
        putUserDto.setEmail("email1@test.ru");
        assertNotNull(putUserDto.getEmail());
        putUserDto.setName("name1");
        assertNotNull(putUserDto.getName());
        putUserDto.setRole("user1");
        assertNotNull(putUserDto.getRole());
        when(userMapper.userEntityToPutUserDtoResponse(any(UserEntity.class))).thenReturn(putUserDtoResponse);
        assertNotNull(userMapper.userEntityToPutUserDtoResponse(user));
        CustomSuccessResponse<PutUserDtoResponse> response = userService.replaceUser(putUserDto, UUID.fromString(ID));
        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(putUserDtoResponse, response.getData());
        assertEquals(1, response.getStatusCode());
        assertNull(response.getCodes());
    }

    @Description(value = "Testing request of replacing user via incorrect id")
    @Test
    void testIncorrectReplaceUser() {
        PutUserDto putUserDto = new PutUserDto();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        CustomException exception = assertThrows(CustomException.class, () -> userService.replaceUser(putUserDto, UUID.fromString(ID)));
        assertEquals(ServerErrorCodes.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }
}