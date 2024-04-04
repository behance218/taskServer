package net.dunice.mk.mkhachemizov_testserver;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import net.dunice.mk.mkhachemizov_testserver.constants.ServerErrorCodes;
import net.dunice.mk.mkhachemizov_testserver.dto.BaseSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.CustomNewsSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.dto.NewsDto;
import net.dunice.mk.mkhachemizov_testserver.entity.NewsEntity;
import net.dunice.mk.mkhachemizov_testserver.entity.TagEntity;
import net.dunice.mk.mkhachemizov_testserver.entity.UserEntity;
import net.dunice.mk.mkhachemizov_testserver.exception.CustomException;
import net.dunice.mk.mkhachemizov_testserver.repository.NewsRepository;
import net.dunice.mk.mkhachemizov_testserver.repository.TagRepository;
import net.dunice.mk.mkhachemizov_testserver.repository.UserRepository;
import net.dunice.mk.mkhachemizov_testserver.service.NewsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {
    @InjectMocks
    private NewsService newsService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NewsRepository newsRepository;
    @Mock
    private TagRepository tagRepository;
    private static final Set<String> tags = Set.of("tag", "againTag", "andAgainTag");
    private static TagEntity[] tagEntity = new TagEntity[tags.size()];
    private static UserEntity user;
    private static NewsEntity news;
    private final String userId = user.getId().toString();
    private static final long NEWS_ID = 10;
    private static final String PASSWORD = "password";

    @BeforeAll
    static void setUpBeforeAll() {
        user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setAvatar("avatar.jpg");
        user.setEmail("email@example.ru");
        user.setName("name");
        user.setPassword(new BCryptPasswordEncoder().encode(PASSWORD));
        long i = 0;
        for (String tag : tags) {
            tagEntity[Math.toIntExact(i)] = new TagEntity();
            tagEntity[Math.toIntExact(i)].setId(i);
            tagEntity[Math.toIntExact(i)].setTitle(tag);
            i++;
        }
    }

    @BeforeEach
    void setUpBeforeEach() {
        news = new NewsEntity();
        news.setId(NEWS_ID);
        news.setTitle("MegaTitle");
        news.setImage("megaNews.jpg");
        news.setDescription("Viewer discretion is advised xD");
        Set<TagEntity> tagEntitySet = Arrays.stream(tagEntity).collect(Collectors.toSet());
        news.setTags(tagEntitySet);
    }

    @Description(value = "Testing request of creating news via entering correct news data")
    @Test
    void testCorrectCreateNews() {
        NewsDto newsDto = new NewsDto();
        newsDto.setTags(List.of("1", "2", "3"));
        assertNotNull(newsDto.getTags());
        when(tagRepository.findByTitle(anyString())).thenReturn(Optional.of(tagEntity[0]));
        assertNotNull(tagRepository.findByTitle(newsDto.getTitle()));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        assertNotNull(userRepository.findById(UUID.fromString(userId)));
        when(newsRepository.save(any(NewsEntity.class))).thenReturn(news);
        assertNotNull(newsRepository.save(news));
        CustomNewsSuccessResponse response = newsService.createNews(newsDto, userId);
        assertNotNull(response);
        assertEquals(1, response.getStatusCode());
    }

    @Description(value = "Testing a request to create news for a non-existent user")
    @Test
    void testIncorrectCreateNews() {
        NewsDto newsDto = new NewsDto();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        CustomException exception = assertThrows(CustomException.class, () -> newsService.createNews(newsDto, userId));
        assertEquals(ServerErrorCodes.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Description(value = "Testing a request to update existing news")
    @Test
    void testCorrectPutNews() {
        NewsDto newsDto = new NewsDto();
        newsDto.setImage("new" + newsDto.getImage());
        assertNotNull(newsDto.getImage());
        newsDto.setTitle("new" + newsDto.getTitle());
        assertNotNull(newsDto.getTitle());
        newsDto.setDescription("new" + newsDto.getDescription());
        assertNotNull(newsDto.getDescription());
        newsDto.setTags(List.of("1", "2", "3"));
        assertNotNull(newsDto.getTags());
        when(newsRepository.putNews(any(Long.class), any(UUID.class))).thenReturn(Optional.of(news));
        assertNotNull(newsRepository.putNews(news.getId(), UUID.fromString(userId)));
        BaseSuccessResponse response = newsService.putNews(newsDto, news.getId(), UUID.fromString(userId));
        assertNotNull(response);
        assertEquals(1, response.getStatusCode());
    }

    @Description(value = "Testing a request to update news which do not exist")
    @Test
    void testIncorrectPutNews() {
        NewsDto newsDto = new NewsDto();
        when(newsRepository.putNews(any(Long.class), any(UUID.class))).thenReturn(Optional.empty());
        CustomException exception = assertThrows(CustomException.class, () -> newsService.putNews(newsDto, news.getId(), UUID.fromString(userId)));
        assertEquals(ServerErrorCodes.NEWS_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Description(value = "Testing a request to delete correctly user news")
    @Test
    void testCorrectDeleteUserNews() {
        when(newsRepository.existsById(any(Long.class))).thenReturn(true);
        BaseSuccessResponse response = newsService.delete(news.getId());
        assertNotNull(response);
        assertEquals(1, response.getStatusCode());
    }

    @Description(value = "Testing a request to delete incorrectly user news")
    @Test
    void testIncorrectDeleteUserNews() {
        when(newsRepository.existsById(any(Long.class))).thenReturn(false);
        CustomException exception = assertThrows(CustomException.class, () -> newsService.delete(news.getId()));
        assertEquals(ServerErrorCodes.NEWS_NOT_FOUND.getMessage(), exception.getMessage());
    }
}
