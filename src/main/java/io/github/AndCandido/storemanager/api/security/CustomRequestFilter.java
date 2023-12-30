package io.github.AndCandido.storemanager.api.security;

import io.github.AndCandido.storemanager.domain.repositories.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class CustomRequestFilter extends OncePerRequestFilter {
    private final static String AUTHORIZATION = "Authorization";
    private final static String AUTHORIZATION_BASIC = "Basic ";

    private final IUserRepository IUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(isBasicAuth(request)) {
            Credentials credentials = getCredentialsFromRequest(request);

            var user = IUserRepository.findByUsername(credentials.username);

            if(user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("User does exist");
                return;
            }

            boolean isValidPassword = verifyPassword(credentials.password, user.getPassword());

            if(!isValidPassword) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Incorrect password");
                return;
            }

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private Credentials getCredentialsFromRequest(HttpServletRequest request) {
        String authorizationWithoutBasic = getRequestAuth(request)
            .replace(AUTHORIZATION_BASIC, "");

        String[] credentials = decodeBase64(authorizationWithoutBasic)
            .split(":");

        return new Credentials(credentials[0], credentials[1]);
    }

    private String decodeBase64(String base64) {
        byte[] base64Decoded = Base64.getDecoder().decode(base64);
        return new String(base64Decoded);
    }

    private boolean isBasicAuth(HttpServletRequest request) {
        String requestAuth = getRequestAuth(request);
        return requestAuth != null && requestAuth.startsWith(AUTHORIZATION_BASIC);
    }

    private String getRequestAuth(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION);
    }

    private static class Credentials {
        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        String username;
        String password;
    }
}
