package net.dunice.mk.mkhachemizov_testserver.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import net.dunice.mk.mkhachemizov_testserver.constants.ValidationConstants;
import net.dunice.mk.mkhachemizov_testserver.dto.BaseSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.PutUserDto;
import net.dunice.mk.mkhachemizov_testserver.dto.PutUserDtoResponse;
import net.dunice.mk.mkhachemizov_testserver.repository.PublicUserView;
import net.dunice.mk.mkhachemizov_testserver.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@Validated
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<PublicUserView>>> all() {
        return ResponseEntity.ok(userService.all());
    }

    @GetMapping("/info")
    public ResponseEntity<CustomSuccessResponse<PublicUserView>> getUserInfo() {
        String id = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok(userService.getUserInfo(UUID.fromString(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PublicUserView>> getUserInfoById(
            @PathVariable @NotNull(message = ValidationConstants.ID_MUST_BE_POSITIVE)
            @Size(min = 36, max = 36, message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) String id) {
        return ResponseEntity.ok(userService.getUserInfoById(UUID.fromString(id)));
    }

    @PutMapping
    public ResponseEntity<CustomSuccessResponse<PutUserDtoResponse>> replaceUser(
            @RequestBody @Valid PutUserDto userNewData) {
        String id = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok(userService.replaceUser(userNewData, UUID.fromString(id)));
    }

    @DeleteMapping
    public ResponseEntity<BaseSuccessResponse> deleteUser() {
        String id = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok(userService.deleteUser(UUID.fromString(id)));
    }
}