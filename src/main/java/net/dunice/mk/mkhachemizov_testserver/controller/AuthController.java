package net.dunice.mk.mkhachemizov_testserver.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.dunice.mk.mkhachemizov_testserver.dto.AuthDto;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.LoginUserDto;
import net.dunice.mk.mkhachemizov_testserver.dto.RegisterUserDto;
import net.dunice.mk.mkhachemizov_testserver.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Validated
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CustomSuccessResponse<LoginUserDto>> register(
            @RequestBody @Valid RegisterUserDto registerUserDto) {
        return new ResponseEntity<>(authService.register(registerUserDto), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomSuccessResponse<LoginUserDto>> login(@RequestBody @Valid AuthDto authDto) {
        return new ResponseEntity<>(authService.login(authDto), HttpStatus.OK);
    }
}
