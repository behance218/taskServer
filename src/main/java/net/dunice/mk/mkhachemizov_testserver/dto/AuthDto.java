package net.dunice.mk.mkhachemizov_testserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import net.dunice.mk.mkhachemizov_testserver.constants.ValidationConstants;

@Getter
@Setter
public class AuthDto {
    @Pattern(regexp = ValidationConstants.EMAIL_PATTERN, message = ValidationConstants.USER_EMAIL_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_NULL)
    @Size(min = 3, max = 100, message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    private String email;
    @NotBlank(message = ValidationConstants.PASSWORD_NOT_VALID)
    private String password;
}