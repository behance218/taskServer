package net.dunice.mk.mkhachemizov_testserver.mapper;

import net.dunice.mk.mkhachemizov_testserver.dto.LoginUserDto;
import net.dunice.mk.mkhachemizov_testserver.dto.PutUserDtoResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.RegisterUserDto;
import net.dunice.mk.mkhachemizov_testserver.entity.UserEntity;
import net.dunice.mk.mkhachemizov_testserver.repository.PublicUserView;
import org.mapstruct.Mapper;

import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity registerUserDtoToUserEntity(RegisterUserDto registrationRequest);

    LoginUserDto userEntityToLoginUserDto(UserEntity user);

    PublicUserView userEntityToPublicUserView(UserEntity user);

    PutUserDtoResponse userEntityToPutUserDtoResponse(UserEntity user);
}
