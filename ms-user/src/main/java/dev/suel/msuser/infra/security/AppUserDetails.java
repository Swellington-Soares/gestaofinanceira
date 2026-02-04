package dev.suel.msuser.infra.security;

import dev.suel.msuser.domain.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class AppUserDetails implements UserDetails {

    private final Customer customer;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return customer.getRoles().stream()
                .flatMap(
                        role -> {
                            Stream<GrantedAuthority> authorities = Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                            Stream<GrantedAuthority> permissions = role.getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.getName()));
                            return Stream.concat(authorities, permissions);
                        }

                ).toList();
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getEmail();
    }

    public Long getId() {
        return customer.getId();
    }
}
