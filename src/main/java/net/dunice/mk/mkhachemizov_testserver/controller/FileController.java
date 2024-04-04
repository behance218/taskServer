package net.dunice.mk.mkhachemizov_testserver.controller;

import lombok.RequiredArgsConstructor;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.service.FileService;

import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/v1/file")
@Validated
@CrossOrigin
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomSuccessResponse<String>> uploadFile(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadFile(file));
    }

    @GetMapping("/{filename}")
    public ResponseEntity<UrlResource> getFile(@PathVariable("filename") String filename) {
        return ResponseEntity.ok(fileService.getFile(filename));
    }
}