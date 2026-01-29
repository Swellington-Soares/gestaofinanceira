package dev.suel.mstransactionapi.infra.services;

import dev.suel.mstransactionapi.dto.UserTokenInfo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("security")
public class SecurityService {
    public boolean isOwner(Long id, Authentication authentication) {
        var user = (UserTokenInfo) authentication.getDetails();
        return user != null && user.id().equals(id);
    }

    public UserTokenInfo getOwner(Authentication authentication) {
        return  (UserTokenInfo) authentication.getDetails();
    }
}
