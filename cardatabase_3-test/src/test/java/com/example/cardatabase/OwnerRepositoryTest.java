package com.example.cardatabase;

import com.example.cardatabase.domain.Owner;
import com.example.cardatabase.domain.OwnerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OwnerRepositoryTest {
    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    void saveOwner() {
        // H2인메모리상황이기 때문에 DB에 아무런 Owner가 없습니다.
        ownerRepository.save(new Owner("칠백", "김"));
        assertThat(ownerRepository.findByFirstName("칠백").isPresent()).isTrue();
    }

    @Test
    @DisplayName("삭제 테스트 : ")
    void deleteOwners() {
        ownerRepository.save(new Owner("팔백", "박"));
        ownerRepository.deleteAll();
        assertThat(ownerRepository.count()).isEqualTo(0);
    }
}
