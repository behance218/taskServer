package net.dunice.mk.mkhachemizov_testserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomSuccessResponse<T> extends BaseSuccessResponse {
    private int[] codes;
    private T data;

    public CustomSuccessResponse(T data) {
        setData(data);
    }

    public CustomSuccessResponse(int[] codes, int statusCode) {
        setCodes(codes);
        setStatusCode(statusCode);
    }
}
