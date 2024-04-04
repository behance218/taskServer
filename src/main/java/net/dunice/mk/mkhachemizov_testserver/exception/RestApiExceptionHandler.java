package net.dunice.mk.mkhachemizov_testserver.exception;

import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import net.dunice.mk.mkhachemizov_testserver.constants.ServerErrorCodes;
import net.dunice.mk.mkhachemizov_testserver.constants.ValidationConstants;
import net.dunice.mk.mkhachemizov_testserver.dto.BaseSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomSuccessResponse;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomSuccessResponse<int[]>> handlerMethodArgumentNotValid(
            MethodArgumentNotValidException exception) {
        int[] codes = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .mapToInt(error -> ServerErrorCodes.getCodeByMessage(error.getDefaultMessage()))
                .toArray();
        CustomSuccessResponse<int[]> customSuccessResponse = new CustomSuccessResponse<>(codes, codes[0]);
        return new ResponseEntity<>(customSuccessResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomSuccessResponse<int[]>> handlerConstraintViolation(
            ConstraintViolationException exception) {
        int[] codes = exception
                .getConstraintViolations()
                .stream()
                .mapToInt(error -> ServerErrorCodes.getCodeByMessage(error.getMessage()))
                .toArray();
        CustomSuccessResponse<int[]> customSuccessResponse = new CustomSuccessResponse<>(codes, codes[0]);
        return new ResponseEntity<>(customSuccessResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomSuccessResponse<int[]>> handleInvalidStatus(HttpMessageNotReadableException exception) {
        int[] codes = {ServerErrorCodes.getCodeByMessage(ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION)};
        CustomSuccessResponse<int[]> customSuccessResponse = new CustomSuccessResponse<>(codes, codes[0]);
        return new ResponseEntity<>(customSuccessResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseSuccessResponse> handleCustomException(CustomException exception) {
        int code = ServerErrorCodes.getCodeByMessage(exception.getMessage());
        BaseSuccessResponse baseSuccessResponse = new BaseSuccessResponse(code);
        return new ResponseEntity<>(baseSuccessResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<BaseSuccessResponse> handleJwtException() {
        int code = ServerErrorCodes.getCodeByMessage(ValidationConstants.UNAUTHORISED);
        BaseSuccessResponse baseSuccessResponse = new BaseSuccessResponse(code);
        return new ResponseEntity<>(baseSuccessResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<BaseSuccessResponse> handleFileException(FileUploadException exception) {
        return new ResponseEntity<>(new BaseSuccessResponse(
                ServerErrorCodes.getCodeByMessage(exception.getMessage())), HttpStatus.BAD_REQUEST);
    }
}
