package com.example.todolist.service;

import com.example.todolist.domain.Todo;
import com.example.todolist.domain.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/*
  - **`clearCompletedTodos()`**: 현재 로그인된 사용자의 완료된(`isCompleted = true`) 모든 할 일을 삭제합니다.

  - **로직**: 현재 인증된 사용자의 `id`를 가져옵니다. 해당 사용자의 `Todo` 목록 중 `isCompleted`가 `true`인 항목들을 모두 찾아서 한 번에 삭제합니다.
 */
@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    // 전체 조회
    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }
    // id로 조회
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    // 추가
    @Transactional
    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }


    // 업데이트
    @Transactional
    public Todo updateTodoStatus(Long id) {
        // 1. id로 Todo 항목을 찾고, 없으면 예외 발생
        Todo todo = getTodoById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found with id: " + id));

        // 2. isCompleted 값을 현재 상태의 반대(!isCompleted)로 변경
        todo.setCompleted(!todo.isCompleted());

        // 3. @Transactional에 의해 트랜잭션 종료 시 자동 저장됨.
        return todo;
    }
    // 완료된 모든 할 일 삭제
    @Transactional
    public Long clearCompletedTodos(Long userId) {
        // TodoRepository의 deleteByUserIdAndIsCompleted(Long userId, boolean isCompleted) 메서드를 호출
        // 이 메서드는 해당 조건에 맞는 모든 항목을 한 번의 쿼리로 삭제하며, 삭제된 개수(Long)를 반환합니다.
        return todoRepository.deleteByUserIdAndIsCompleted(userId, true);
    }
}
