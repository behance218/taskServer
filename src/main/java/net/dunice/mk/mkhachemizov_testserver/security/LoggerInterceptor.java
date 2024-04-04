package net.dunice.mk.mkhachemizov_testserver.security;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.dunice.mk.mkhachemizov_testserver.entity.LogEntity;
import net.dunice.mk.mkhachemizov_testserver.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoggerInterceptor implements HandlerInterceptor {
    private final LogRepository logRepository;
    private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler,
                                Exception e) {
        var log = new LogEntity();
        log.setUserId(SecurityContextHolder.getContext()
                .getAuthentication()
                .getName());
        log.setMethod(request.getMethod());
        log.setUrl(String.valueOf(request.getRequestURL()));
        log.setStatus(response.getStatus());
        logger.info(STR."LOG [userId: \{log.getUserId()}, method: \{log.getMethod()}, url: \{log.getUrl()}, status: \{log.getStatus()}\{']'}");
        logRepository.save(log);
    }
}