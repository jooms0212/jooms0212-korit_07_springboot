package com.example.todolist.service;

import com.example.todolist.domain.AppUser;
import com.example.todolist.domain.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    public UserDetailsServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 사용자 이름으로 AppUser 조회
        Optional<AppUser> user = appUserRepository.findByUsername(username);

        // 2. 사용자를 찾지 못하면 예외 발생
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 3. UserDetails 객체(Spring Security용 User) 생성 및 반환
        // 역할(Role)은 현재 "USER"로 고정합니다.
        return new User(user.get().getUsername(),
                user.get().getPassword(),
                Collections.emptyList()); // 권한은 비어 있지만, 인증 자체는 통과
    }
}

