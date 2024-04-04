package net.dunice.mk.mkhachemizov_testserver.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import net.dunice.mk.mkhachemizov_testserver.repository.Tag;

@Getter
@Setter
public class GetNewsOutDto {
    private String description;
    private Integer id;
    private String image;
    private List<Tag> tags;
    private String title;
    private UUID userId;
    private String username;
}