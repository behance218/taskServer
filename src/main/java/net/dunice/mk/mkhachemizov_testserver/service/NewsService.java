package net.dunice.mk.mkhachemizov_testserver.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import net.dunice.mk.mkhachemizov_testserver.constants.ServerErrorCodes;
import net.dunice.mk.mkhachemizov_testserver.dto.BaseSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomNewsSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.GetNewsOutDto;
import net.dunice.mk.mkhachemizov_testserver.dto.NewsDto;
import net.dunice.mk.mkhachemizov_testserver.dto.PageableResponse;
import net.dunice.mk.mkhachemizov_testserver.entity.NewsEntity;
import net.dunice.mk.mkhachemizov_testserver.entity.TagEntity;
import net.dunice.mk.mkhachemizov_testserver.exception.CustomException;
import net.dunice.mk.mkhachemizov_testserver.mapper.NewsMapper;
import net.dunice.mk.mkhachemizov_testserver.repository.NewsRepository;
import net.dunice.mk.mkhachemizov_testserver.repository.TagRepository;
import net.dunice.mk.mkhachemizov_testserver.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final NewsMapper newsMapper;

    public CustomSuccessResponse<PageableResponse<List<GetNewsOutDto>>> newsPaginated(Page<NewsEntity> page) {
        PageableResponse<List<GetNewsOutDto>> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(newsMapper.newsEntityToGetNewsOutDto(page.getContent()));
        pageableResponse.setNumberOfElements(page.getTotalElements());
        return new CustomSuccessResponse<>(pageableResponse);
    }

    public PageableResponse<List<GetNewsOutDto>> pageableResponsePaginatedNews(Page<NewsEntity> page) {
        PageableResponse<List<GetNewsOutDto>> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(newsMapper.newsEntityToGetNewsOutDto(page.getContent()));
        pageableResponse.setNumberOfElements(page.getTotalElements());
        return pageableResponse;
    }

    public Set<TagEntity> createStringTags(List<String> stringList) {
        Set<TagEntity> tags = new HashSet<>();
        stringList.forEach(s -> {
            Optional<TagEntity> tag = tagRepository.findByTitle(s);
            if (tag.isEmpty()) {
                TagEntity tagEntity = new TagEntity();
                tagEntity.setTitle(s);
                tagRepository.save(tagEntity);
                tags.add(tagEntity);
            }
            else {
                tags.add(tag.get());
            }
        });
        return tags;
    }

    public CustomNewsSuccessResponse createNews(NewsDto newsDto, String id) {
        NewsEntity newsEntity = NewsMapper.newsDtoToNewsEntity(newsDto);
        newsEntity.setTags(createStringTags(newsDto.getTags() != null ? newsDto.getTags() : Collections.emptyList()));
        newsEntity.setUserEntity(userRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new CustomException(ServerErrorCodes.USER_NOT_FOUND.getMessage())));
        newsRepository.save(newsEntity);
        return new CustomNewsSuccessResponse(newsEntity.getId());
    }

    public BaseSuccessResponse putNews(NewsDto newsDto, Long id, UUID uuid) {
        Optional<NewsEntity> news = newsRepository.putNews(id, uuid);
        if (news.isEmpty()) {
            throw new CustomException(ServerErrorCodes.NEWS_NOT_FOUND.getMessage());
        }
        NewsEntity newsEntity = news.get();
        newsEntity.setDescription(newsDto.getDescription());
        newsEntity.setImage(newsDto.getImage());
        newsEntity.setTitle(newsDto.getTitle());
        newsEntity.setTags(createStringTags(newsDto.getTags()));
        newsRepository.save(newsEntity);
        return new BaseSuccessResponse();
    }

    public CustomSuccessResponse<PageableResponse<List<GetNewsOutDto>>> getNews(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<NewsEntity> newsEntity = newsRepository.findAll(pageable);
        return newsPaginated(newsEntity);
    }

    public PageableResponse<List<GetNewsOutDto>> findNews(Integer page, Integer perPage, String author, String keywords, List<String> tags) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<NewsEntity> newsEntityPage = newsRepository.findNews(author, keywords, tags, pageable);
        return pageableResponsePaginatedNews(newsEntityPage);
    }

    public CustomSuccessResponse<PageableResponse<List<GetNewsOutDto>>> getUserNews(Integer page, Integer perPage, UUID userId) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<NewsEntity> newsEntity = newsRepository.findUserNews(userId, pageable);
        return newsPaginated(newsEntity);
    }

    public BaseSuccessResponse delete(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new CustomException(ServerErrorCodes.NEWS_NOT_FOUND.getMessage());
        }
        newsRepository.deleteById(id);
        return new BaseSuccessResponse();
    }
}