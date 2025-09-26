package com.example.cardatabase;

import com.example.cardatabase.domain.AppUser;
import com.example.cardatabase.domain.AppUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AppRepositoryTest {
    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    @DisplayName("사용자 조회 테스트")
    void findByUsernameShouldReturnAppUser() {
        // given
        AppUser appUser = new AppUser("user1", "user1", "USER");
        appUserRepository.save(appUser);
        // when
        Optional<AppUser> foundUser = appUserRepository.findByUsername("user1");
        // when
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getRole()).isEqualTo("USER");

    }
}
