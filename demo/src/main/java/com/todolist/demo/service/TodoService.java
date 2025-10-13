package com.todolist.demo.service;

import com.todolist.demo.domain.Todo;
import com.todolist.demo.domain.TodoRepository;
import com.todolist.demo.domain.User;
import com.todolist.demo.domain.UserRepository;
import com.todolist.demo.dto.TodoRequestDto;
import com.todolist.demo.dto.TodoRequestRecord;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class TodoService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    public TodoService(UserRepository userRepository, TodoRepository todoRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User를 찾을 수 없습니다."));
    }

    // 본인 확인 로직을 추가하겠습니다. 그러니까 To-do 객체 내에 있는 User와, 지금 요청을 보내는 애가 동일한지를 확인해야겠네요.
    private void checkOwnership(Todo todo) throws AccessDeniedException {
        if(!todo.getUser().equals(getCurrentUser())) {
            throw new AccessDeniedException("해당 todo에 접근할 수 없습니다.");
        }
    }

    // 1. 사용자 구분 없이 모든 할 일 목록 조회
    @Transactional
    public List<Todo> getTodos() {

        return todoRepository.findAll();
    }

    // 2. 현재 사용자의 모든 할 일 목록 조회
    @Transactional(readOnly = true)
    public List<Todo> getTodosForCurrentUser() {
        User currentUser = getCurrentUser();
        return todoRepository.findByUserId(currentUser.getId());
    }

    // 3. 새로운 to-do 추가
    // DTO version
    public Todo createTodo(TodoRequestDto todoRequestDto) {
        User currentUser = getCurrentUser();
        Todo newTodo = new Todo(todoRequestDto.getContent(), currentUser);

        return todoRepository.save(newTodo);
    }

    // Record version
    public Todo createTodo2(TodoRequestRecord todoRequestRecord) {
        User currentUser = getCurrentUser();
        Todo newTodo = new Todo(todoRequestRecord.content(), currentUser);

        return todoRepository.save(newTodo);
    }

    // 4. 할 일 내용 수정
    @Transactional
    public Todo updateTodoContent(Long id, TodoRequestDto updateDto) throws AccessDeniedException {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 todo가 없습니다. id : " + id));
        checkOwnership(todo);
        todo.setContent(updateDto.getContent());
        return todoRepository.save(todo);
    }

    // 5. 할 일 삭제
    @Transactional
    public void deleteTodo(Long id) throws AccessDeniedException {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 todo가 없습니다. id : " + id));
        checkOwnership(todo);
        todoRepository.delete(todo);
    }

    // 6. 할 일 완료 상태 토글
    @Transactional
    public Todo toggleTodoStatus(Long id) throws AccessDeniedException {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 todo가 없습니다. id : " + id));
        checkOwnership(todo);
        todo.setCompleted(!todo.isCompleted());
        return todoRepository.save(todo);
    }

    // 7. 완료된 할 일 전체 삭제하는 로직
    @Transactional
    public void clearCompletedTodos() {
        User currentUser = getCurrentUser();
        todoRepository.deleteByUserAndIsCompleted(currentUser, true);
    }

}
