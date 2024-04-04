package net.dunice.mk.mkhachemizov_testserver.service;

import lombok.RequiredArgsConstructor;
import net.dunice.mk.mkhachemizov_testserver.constants.ServerErrorCodes;
import net.dunice.mk.mkhachemizov_testserver.constants.ValidationConstants;
import net.dunice.mk.mkhachemizov_testserver.dto.AuthDto;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.LoginUserDto;
import net.dunice.mk.mkhachemizov_testserver.dto.RegisterUserDto;
import net.dunice.mk.mkhachemizov_testserver.entity.UserEntity;
import net.dunice.mk.mkhachemizov_testserver.exception.CustomException;
import net.dunice.mk.mkhachemizov_testserver.mapper.UserMapper;
import net.dunice.mk.mkhachemizov_testserver.repository.UserRepository;
import net.dunice.mk.mkhachemizov_testserver.security.JwtUtil;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public CustomSuccessResponse<LoginUserDto> register(RegisterUserDto registrationRequest) {
        userRepository
                .findByEmail(registrationRequest.getEmail())
                .ifPresent(users -> {
                    throw new CustomException(ServerErrorCodes.USER_ALREADY_EXISTS.getMessage());
                });
        registrationRequest.setPassword(passwordEncoder
                .encode(registrationRequest.getPassword()));
        UserEntity user = userMapper.registerUserDtoToUserEntity(registrationRequest);
        user = userRepository.save(user);
        LoginUserDto loginUserDto = userMapper.userEntityToLoginUserDto(user);
        loginUserDto.setToken(jwtUtil.generateToken(user.getId()));
        return new CustomSuccessResponse<>(loginUserDto);
    }

    public CustomSuccessResponse<LoginUserDto> login(AuthDto authRequest) {
        UserEntity userEntity =
                userRepository
                        .findByEmail(authRequest.getEmail())
                        .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND.getMessage()));

        if (!passwordEncoder.matches(authRequest.getPassword(), userEntity.getPassword())) {
            throw new CustomException(ValidationConstants.PASSWORD_NOT_VALID);
        }
        LoginUserDto loginUserDto = userMapper.userEntityToLoginUserDto(userEntity);
        loginUserDto.setToken(jwtUtil.generateToken(userEntity.getId()));
        return new CustomSuccessResponse<>(loginUserDto);
    }
}
