package net.dunice.mk.mkhachemizov_testserver.mapper;

import java.util.List;

import net.dunice.mk.mkhachemizov_testserver.dto.GetNewsOutDto;
import net.dunice.mk.mkhachemizov_testserver.dto.NewsDto;
import net.dunice.mk.mkhachemizov_testserver.entity.NewsEntity;
import org.mapstruct.Mapper;

import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface NewsMapper {

    List<GetNewsOutDto> newsEntityToGetNewsOutDto(List<NewsEntity> newsEntity);

    static NewsEntity newsDtoToNewsEntity(NewsDto newsDto) {
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setImage(newsDto.getImage());
        newsEntity.setTitle(newsDto.getTitle());
        newsEntity.setDescription(newsDto.getDescription());
        return newsEntity;
    }
}