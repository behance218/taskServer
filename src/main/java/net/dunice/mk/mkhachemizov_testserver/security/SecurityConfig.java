package net.dunice.mk.mkhachemizov_testserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import net.dunice.mk.mkhachemizov_testserver.constants.ServerErrorCodes;
import net.dunice.mk.mkhachemizov_testserver.dto.BaseSuccessResponse;
import net.dunice.mk.mkhachemizov_testserver.repository.LogRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@PropertySource("classpath:application-dev.properties")
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
    private final JwtFilter jwtFilter;
    private final LogRepository logRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor(logRepository));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/auth/**", "/v1/file/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/news/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
                    BaseSuccessResponse baseSuccessResponse = new BaseSuccessResponse(
                            ServerErrorCodes.UNAUTHORISED.getCode());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json");
                    ObjectWriter objectWriter = new ObjectMapper()
                            .writer()
                            .withDefaultPrettyPrinter();
                    String content = objectWriter.writeValueAsString(baseSuccessResponse);
                    response.getWriter().write(content);
                }));
        return http.build();
    }
}
