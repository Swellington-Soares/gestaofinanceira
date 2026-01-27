package dev.suel.msuser.infra.security;

import dev.suel.msuser.infra.mapper.CustomerMapper;
import dev.suel.msuser.infra.persistence.entity.CustomerEntity;
import dev.suel.msuser.infra.persistence.repository.CustomerEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final CustomerEntityRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomerEntity customerEntity = customerRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário indisponível."));
        return new AppUserDetails( customerMapper.entityToModel(customerEntity) );
    }
}
