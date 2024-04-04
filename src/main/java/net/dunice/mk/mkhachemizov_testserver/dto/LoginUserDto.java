package net.dunice.mk.mkhachemizov_testserver.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {
    private String avatar;
    private String email;
    private UUID id;
    private String name;
    private String role;
    private String token;
}

