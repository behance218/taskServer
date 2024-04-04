package net.dunice.mk.mkhachemizov_testserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableResponse<T> {
    private T content;
    private Long numberOfElements;
}
