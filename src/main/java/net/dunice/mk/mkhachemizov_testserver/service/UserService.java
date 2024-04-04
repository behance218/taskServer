package net.dunice.mk.mkhachemizov_testserver.service;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import net.dunice.mk.mkhachemizov_testserver.constants.ServerErrorCodes;
import net.dunice.mk.mkhachemizov_testserver.dto.BaseSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.PutUserDto;
import net.dunice.mk.mkhachemizov_testserver.dto.PutUserDtoResponse;
import net.dunice.mk.mkhachemizov_testserver.entity.UserEntity;
import net.dunice.mk.mkhachemizov_testserver.exception.CustomException;
import net.dunice.mk.mkhachemizov_testserver.mapper.UserMapper;
import net.dunice.mk.mkhachemizov_testserver.repository.PublicUserView;
import net.dunice.mk.mkhachemizov_testserver.repository.UserRepository;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public CustomSuccessResponse<List<PublicUserView>> all() {
        return new CustomSuccessResponse<>(userRepository
                .findAll()
                .stream()
                .map(userMapper::userEntityToPublicUserView)
                .toList());
    }

    public CustomSuccessResponse<PublicUserView> getUserInfo(UUID id) {
        PublicUserView publicUserView = userMapper.userEntityToPublicUserView(userRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND.getMessage())));
        return new CustomSuccessResponse<>(publicUserView);
    }

    public CustomSuccessResponse<PublicUserView> getUserInfoById(UUID id) {
        PublicUserView publicUserView = userMapper.userEntityToPublicUserView(userRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND.getMessage())));
        return new CustomSuccessResponse<>(publicUserView);
    }

    public CustomSuccessResponse<PutUserDtoResponse> replaceUser(PutUserDto userNewData, UUID id) {
        UserEntity userEntity = userRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND.getMessage()));
        userRepository
                .findByEmail(userNewData.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(ServerErrorCodes.USER_WITH_THIS_EMAIL_ALREADY_EXIST.getMessage());
                });
        userEntity.setAvatar(userNewData.getAvatar());
        userEntity.setEmail(userNewData.getEmail());
        userEntity.setName(userNewData.getName());
        userEntity.setRole(userNewData.getRole());
        userRepository.save(userEntity);
        PutUserDtoResponse putUserDtoResponse = userMapper.userEntityToPutUserDtoResponse(userEntity);
        return new CustomSuccessResponse<>(putUserDtoResponse);
    }

    public BaseSuccessResponse deleteUser(UUID id) {
        userRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND.getMessage()));
        userRepository.deleteById(id);
        return new BaseSuccessResponse();
    }
}