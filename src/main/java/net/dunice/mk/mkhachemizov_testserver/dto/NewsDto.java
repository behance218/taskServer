package net.dunice.mk.mkhachemizov_testserver.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import net.dunice.mk.mkhachemizov_testserver.constants.ValidationConstants;


@Getter
@Setter
public class NewsDto {
    @NotEmpty(message = ValidationConstants.NEWS_DESCRIPTION_HAS_TO_BE_PRESENT)
    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_DESCRIPTION_SIZE_NOT_VALID)
    private String description;
    @NotBlank(message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
    private String image;
    List<@NotBlank(message = ValidationConstants.TAGS_NOT_VALID) String> tags;
    @NotEmpty(message = ValidationConstants.NEWS_TITLE_NOT_NULL)
    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_TITLE_SIZE)
    private String title;
}