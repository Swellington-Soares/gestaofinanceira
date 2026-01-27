package dev.suel.msuser.infra.security;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("security")
public class SecurityService {
    public boolean isOwner(Long id, Authentication authentication) {
        var user = (AppUserDetails) authentication.getDetails();
        return user != null && user.getId().equals(id);
    }
}
