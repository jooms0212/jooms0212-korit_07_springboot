package com.example.cardatabase4.Service;

import com.example.cardatabase4.domain.AppUser;
import com.example.cardatabase4.domain.AppUserRepository;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    public UserDetailsServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = appUserRepository.findByUsername(username);

        UserBuilder builder = null;
        if (user.isPresent()) {
            AppUser currentUser = user.get();   // user 자체는 Optional 자료형이지 AppUser가 아닙니다.
            builder = withUsername(username);
            builder.password(currentUser.getPassword()).roles(currentUser.getRole()).build();
        } else {
            throw new UsernameNotFoundException("User Not Found.");
        }

        return builder.build();
    }
}
