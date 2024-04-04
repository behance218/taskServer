package net.dunice.mk.mkhachemizov_testserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomNewsSuccessResponse extends BaseSuccessResponse {
    private Long id;
}
