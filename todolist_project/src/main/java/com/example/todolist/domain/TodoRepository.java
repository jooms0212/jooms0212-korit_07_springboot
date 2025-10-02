package com.example.todolist.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Transactional
    Long deleteByUserIdAndIsCompleted(Long userId, boolean isCompleted);
}
