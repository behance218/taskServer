package net.dunice.mk.mkhachemizov_testserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaseSuccessResponse {
    private boolean success = true;
    private int statusCode = 1;

    public BaseSuccessResponse(int statusCode) {
        setStatusCode(statusCode);
    }
}
