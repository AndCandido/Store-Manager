package io.github.AndCandido.storemanager.domain.enums;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Role {
    ADMIN(createGrantedAuthority("ROLE_ADMIN", "ROLE_USER")),
    USER(createGrantedAuthority("ROLE_USER"));

    private final List<SimpleGrantedAuthority> grantedAuthorities;

    Role(List<SimpleGrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    private static List<SimpleGrantedAuthority> createGrantedAuthority(String... roles) {
        return Arrays.stream(roles).map(SimpleGrantedAuthority::new).toList();
    }
}
