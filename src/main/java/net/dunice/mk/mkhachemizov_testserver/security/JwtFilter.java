package net.dunice.mk.mkhachemizov_testserver.security;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dunice.mk.mkhachemizov_testserver.constants.ValidationConstants;
import net.dunice.mk.mkhachemizov_testserver.entity.UserEntity;
import net.dunice.mk.mkhachemizov_testserver.repository.UserRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException, JwtException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String username = null;
        String jwt = null;
        try {
            if (header != null && header.startsWith(ValidationConstants.BEARER)) {
                jwt = header.substring(ValidationConstants.BEARER.length());
                username = jwtUtil.extractUserName(jwt);
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Optional<UserEntity> userEntity = userRepository.findById(UUID.fromString(username));
                if (userEntity.isEmpty()) {
                    throw new JwtException("NoUser");
                }
                UserDetails userDetails = userEntity.get();
                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        catch (JwtException e) {
            System.err.println("JWT authorization failed");
        }
        filterChain.doFilter(request, response);
    }
}