package net.dunice.mk.mkhachemizov_testserver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.dunice.mk.mkhachemizov_testserver.constants.ServerErrorCodes;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.exception.CustomException;
import net.dunice.mk.mkhachemizov_testserver.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.context.annotation.Description;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {
    @InjectMocks
    private FileService fileService;
    private MultipartFile file;
    private byte[] content;

    @Description(value = "Testing a request to upload file correctly")
    @Test
    void testCorrectFileUpload() {
        String SOURCE = "src/test/resources/uploads/";
        assertNotNull(SOURCE);
        String FILE = "ne_ponyal.jpeg";
        assertNotNull(FILE);
        try {
            content = Files.readAllBytes(Path.of(SOURCE + FILE));
            assertNotNull(content);
        }
        catch (IOException e) {
            throw new CustomException("There is some issues with your file");
        }
        try {
            file = new MockMultipartFile(
                    "file",
                    FILE,
                    "image/jpeg",
                    new ByteArrayInputStream(content)
            );
            assertNotNull(file);
        }
        catch (IOException e) {
            throw new CustomException("There is some issues with your file");
        }
        CustomSuccessResponse<String> response = fileService.uploadFile(file);
        assertNotNull(response);
        assertNotNull(response.getData());
        String DEFAULT_LINK = "http://localhost:8080/v1/file";
        assertThat(response.getData().equals(DEFAULT_LINK + SOURCE + FILE));
        assertEquals(1, response.getStatusCode());
        assertNull(response.getCodes());
    }

    @Description(value = "Testing a request to upload file incorrectly via trying to download empty file")
    @Test
    void testIncorrectFileUpload() {
        file = null;
        assertNull(file);
        CustomException exception = assertThrows(CustomException.class, () -> fileService.uploadFile(file));
        assertEquals(ServerErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED.getMessage(), exception.getMessage());
    }

    @Description(value = "Testing an incorrect request to get a file by receiving a non-existent file")
    @Test
    void testIncorrectGetFile() {
        String SOURCE = "src/test/resources/uploads/";
        assertNotNull(SOURCE);
        String FILE = "ne_ponyal.jpeg";
        assertNotNull(FILE);
        try {
            content = Files.readAllBytes(Path.of(SOURCE + FILE));
            assertNotNull(content);
        }
        catch (IOException e) {
            throw new CustomException("There is some issues with your file");
        }
        try {
            file = new MockMultipartFile(
                    "file",
                    FILE,
                    "image/jpeg",
                    new ByteArrayInputStream(content)
            );
            assertNotNull(file);
        }
        catch (IOException e) {
            throw new CustomException("There is some issues with your file");
        }
        CustomException exception = assertThrows(CustomException.class, () -> fileService.getFile(String.valueOf(file)));
        assertEquals(ServerErrorCodes.EXCEPTION_HANDLER_NOT_PROVIDED.getMessage(), exception.getMessage());
    }
}