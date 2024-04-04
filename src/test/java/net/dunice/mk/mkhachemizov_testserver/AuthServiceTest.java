package net.dunice.mk.mkhachemizov_testserver;

import java.util.Optional;
import java.util.UUID;

import net.dunice.mk.mkhachemizov_testserver.constants.ServerErrorCodes;
import net.dunice.mk.mkhachemizov_testserver.dto.AuthDto;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.LoginUserDto;
import net.dunice.mk.mkhachemizov_testserver.dto.RegisterUserDto;
import net.dunice.mk.mkhachemizov_testserver.entity.UserEntity;
import net.dunice.mk.mkhachemizov_testserver.exception.CustomException;
import net.dunice.mk.mkhachemizov_testserver.mapper.UserMapper;
import net.dunice.mk.mkhachemizov_testserver.repository.UserRepository;
import net.dunice.mk.mkhachemizov_testserver.security.JwtUtil;
import net.dunice.mk.mkhachemizov_testserver.service.AuthService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private static final UserMapper mockUserMapper = new UserMapperImpl();
    private static final String email = "email@test.ru";
    private static final String password = "password";
    private static final String encodedPassword = "encoded";
    private final String token = "token";
    private static final RegisterUserDto registrationRequest = new RegisterUserDto();
    private static final AuthDto authRequest = new AuthDto();
    private static UserEntity user;

    @BeforeAll
    static void setUpBeforeAll() {
        registrationRequest.setAvatar("avatar.jpg");
        registrationRequest.setEmail(email);
        registrationRequest.setName("user");
        registrationRequest.setPassword(password);
        registrationRequest.setRole("role");
        user = mockUserMapper.registerUserDtoToUserEntity(registrationRequest);
        user.setPassword(encodedPassword);
        user.setId(UUID.randomUUID());
        authRequest.setEmail(email);
        authRequest.setPassword(password);
    }

    @Description(value = "Testing user registration via registering new user")
    @Test
    void testCorrectUserRegistration() {
        LoginUserDto loginUserDto = mockUserMapper.userEntityToLoginUserDto(user);
        loginUserDto.setToken(token);
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString()))
                .thenReturn(encodedPassword);
        assertNotNull(encodedPassword);
        when(userMapper.registerUserDtoToUserEntity(any(RegisterUserDto.class)))
                .thenReturn(user);
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(user);
        assertNotNull(user);
        when(userMapper.userEntityToLoginUserDto(any(UserEntity.class)))
                .thenReturn(loginUserDto);
        assertNotNull(loginUserDto);
        when(jwtUtil.generateToken(any(UUID.class)))
                .thenReturn(token);
        assertNotNull(token);
        CustomSuccessResponse<LoginUserDto> response = authService.register(registrationRequest);
        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(loginUserDto, response.getData());
        assertEquals(1, response.getStatusCode());
        assertNull(response.getCodes());
    }

    @Description(value = "Testing registration via registering existing user")
    @Test
    void testIncorrectUserRegistration() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        assertNotNull(userRepository.findByEmail(email));
        CustomException exception = assertThrows(CustomException.class, () -> authService.register(registrationRequest));
        assertEquals(ServerErrorCodes.USER_ALREADY_EXISTS.getMessage(), exception.getMessage());
    }

    @Description(value = "Testing user authentication via entering correct user data")
    @Test
    void testCorrectUserAuthentication() {
        LoginUserDto loginUserDto = mockUserMapper.userEntityToLoginUserDto(user);
        loginUserDto.setToken(token);
        assertNotNull(token);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        assertNotNull(userRepository.findByEmail(email));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userMapper.userEntityToLoginUserDto(any(UserEntity.class))).thenReturn(mockUserMapper.userEntityToLoginUserDto(user));
        assertNotNull(mockUserMapper.userEntityToLoginUserDto(user));
        when(jwtUtil.generateToken(any(UUID.class))).thenReturn(token);
        assertNotNull(UUID.class);
        CustomSuccessResponse<LoginUserDto> response = authService.login(authRequest);
        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(token, response.getData().getToken());
    }

    @Description(value = "Testing user authentication via entering wrong user password")
    @Test
    void testIncorrectUserAuthentication() {
        authRequest.setEmail(email);
        assertNotNull(authRequest.getEmail());
        authRequest.setPassword("wrong_password");
        assertNotNull(authRequest.getPassword());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        assertNotEquals(authRequest.getPassword(), password);
        CustomException exception = assertThrows(CustomException.class, () -> authService.login(authRequest));
        assertEquals(ServerErrorCodes.PASSWORD_NOT_VALID.getMessage(), exception.getMessage());
    }
}
