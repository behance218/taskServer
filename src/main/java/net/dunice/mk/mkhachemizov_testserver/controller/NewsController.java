package net.dunice.mk.mkhachemizov_testserver.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import net.dunice.mk.mkhachemizov_testserver.constants.ValidationConstants;
import net.dunice.mk.mkhachemizov_testserver.dto.BaseSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomNewsSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.GetNewsOutDto;
import net.dunice.mk.mkhachemizov_testserver.dto.NewsDto;
import net.dunice.mk.mkhachemizov_testserver.dto.PageableResponse;
import net.dunice.mk.mkhachemizov_testserver.service.NewsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/news")
@Validated
@CrossOrigin
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<CustomNewsSuccessResponse> createNews(@RequestBody @Valid NewsDto newsDto) {
        String id = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok(newsService.createNews(newsDto, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> putNews(@PathVariable Long id, @RequestBody @Valid NewsDto newsDto) {
        String uuid = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok(newsService.putNews(newsDto, id, UUID.fromString(uuid)));
    }

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutDto>>>> getNews(
            @RequestParam
            @Min(value = 1, message = ValidationConstants.PAGE_SIZE_NOT_VALID)
            Integer page,
            @RequestParam
            @Min(value = 1, message = ValidationConstants.PER_PAGE_MIN_NOT_VALID)
            @Max(value = 1000, message = ValidationConstants.PER_PAGE_MAX_NOT_VALID)
            Integer perPage) {
        return ResponseEntity.ok(newsService.getNews(page, perPage));
    }

    @GetMapping("/find")
    public ResponseEntity<PageableResponse<List<GetNewsOutDto>>> findNews(
            @RequestParam
            @Min(value = 1, message = ValidationConstants.PAGE_SIZE_NOT_VALID)
            Integer page,
            @RequestParam
            @Min(value = 1, message = ValidationConstants.PER_PAGE_MIN_NOT_VALID)
            @Max(value = 1000, message = ValidationConstants.PER_PAGE_MAX_NOT_VALID)
            Integer perPage,
            @RequestParam
            String author,
            @RequestParam
            String keywords,
            @RequestParam
            List<@NotBlank String> tags) {
        return ResponseEntity.ok(newsService.findNews(page, perPage, author, keywords, tags));
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutDto>>>> getUserNews(
            @RequestParam
            @Min(value = 1, message = ValidationConstants.PAGE_SIZE_NOT_VALID)
            Integer page,
            @RequestParam
            @Min(value = 1, message = ValidationConstants.PER_PAGE_MIN_NOT_VALID)
            @Max(value = 1000, message = ValidationConstants.PER_PAGE_MAX_NOT_VALID)
            Integer perPage,
            @PathVariable
            UUID userid) {
        return ResponseEntity.ok(newsService.getUserNews(page, perPage, userid));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(newsService.delete(id));
    }
}