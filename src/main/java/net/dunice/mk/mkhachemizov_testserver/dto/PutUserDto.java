package net.dunice.mk.mkhachemizov_testserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import net.dunice.mk.mkhachemizov_testserver.constants.ValidationConstants;

@Getter
@Setter
public class PutUserDto {
    @NotBlank(message = ValidationConstants.USER_AVATAR_NOT_NULL)
    private String avatar;
    @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_NULL)
    private String email;
    @NotBlank(message = ValidationConstants.USER_NAME_HAS_TO_BE_PRESENT)
    @Size(min = 3, max = 100, message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    private String name;
    @NotBlank(message = ValidationConstants.USER_ROLE_NOT_NULL)
    @Size(min = 3, max = 25, message = ValidationConstants.ROLE_SIZE_NOT_VALID)
    private String role;
}