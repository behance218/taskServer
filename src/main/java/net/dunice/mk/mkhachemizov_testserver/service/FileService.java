package net.dunice.mk.mkhachemizov_testserver.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import net.dunice.mk.mkhachemizov_testserver.constants.ServerErrorCodes;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.exception.CustomException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${url.file}")
    private String URL_OF_FILE;

    public CustomSuccessResponse<String> uploadFile(MultipartFile file) {
        final String UPLOADS_DIRECTORY = "src/main/resources/uploads";
        try {
            Optional
                    .ofNullable(file)
                    .orElseThrow(() -> new CustomException(
                            ServerErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED.getMessage()));
            final String FILE_NAME = UUID.randomUUID() + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths
                    .get(UPLOADS_DIRECTORY)
                    .resolve(FILE_NAME), StandardCopyOption.REPLACE_EXISTING);
            return new CustomSuccessResponse<>(URL_OF_FILE + FILE_NAME);
        }
        catch (IOException e) {
            throw new CustomException(ServerErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED.getMessage());
        }
    }

    public UrlResource getFile(String name) {
        final String UPLOADS_DIRECTORY = "src/main/resources/uploads";

        try {
            try {
                URL file = ResourceUtils.getFile(UPLOADS_DIRECTORY + name).getAbsoluteFile().toURL();
                if (!Files.exists(Path.of(file.getPath()))) {
                    throw new CustomException(ServerErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED.getMessage());
                }
                return new UrlResource(file);
            }
            catch (MalformedURLException e) {
                throw new CustomException(ServerErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED.getMessage());
            }

        }
        catch (FileNotFoundException exception) {
            throw new CustomException(ServerErrorCodes.UNKNOWN.getMessage());
        }
    }
}