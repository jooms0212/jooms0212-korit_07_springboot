package com.example.todolist.web;

import com.example.todolist.domain.Todo;
import com.example.todolist.domain.AppUser;
import com.example.todolist.domain.AppUserRepository;
import com.example.todolist.dto.TodoRequestRecord; // Todo 생성 요청 DTO
import com.example.todolist.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;
    private final AppUserRepository userRepository;

    public TodoController(TodoService todoService, AppUserRepository userRepository) {
        this.todoService = todoService;
        this.userRepository = userRepository;
    }

    // 현재 인증된 사용자의 ID를 가져오는 헬퍼 메서드 (clearCompletedTodos에만 사용)
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new SecurityException("User is not authenticated or token is invalid.");
        }

        // Principal 이름(username)으로 AppUser를 찾아 ID를 반환
        String username = authentication.getName();
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Authenticated user not found in database."));

        return user.getId();
    }

    // POST /api/todos : CREATE (Service 시그니처에 맞추어 DTO -> Entity 변환)
    @PostMapping
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody TodoRequestRecord request) {
        // [⚠️ 현재 서비스 문제점]: TodoService가 userId를 받지 않아,
        // 컨트롤러에서 사용자 정보를 찾아 Todo 객체에 수동으로 연결해야 합니다.
        Long userId = getCurrentUserId();

        // AppUser 객체 조회 (TodoService에서 수행해야 하지만, 서비스 시그니처 문제로 컨트롤러에서 수행)
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found during todo creation."));

        // DTO를 Todo Entity로 변환 (isCompleted=false, user 연결)
        Todo newTodo = new Todo(request.content(), false, user);

        return new ResponseEntity<>(todoService.addTodo(newTodo), HttpStatus.CREATED);
    }

    // GET /api/todos : READ ALL (Service 시그니처에 맞추어 userId 없이 전체 조회)
    @GetMapping
    public ResponseEntity<List<Todo>> getTodos() {
        // [⚠️ 현재 서비스 문제점]: userId 필터링 없이 모든 사용자의 할 일을 반환합니다.
        List<Todo> todos = todoService.getTodos();
        return ResponseEntity.ok(todos);
    }

    // GET /api/todos/{id} : READ ONE (Service 시그니처에 맞추어 userId 없이 조회)
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        // [⚠️ 현재 서비스 문제점]: 소유권 검증 없이 할 일을 반환합니다.
        Todo todo = todoService.getTodoById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found with id: " + id));
        return ResponseEntity.ok(todo);
    }

    // PATCH /api/todos/{id} : UPDATE STATUS (상태 토글 - 추가 로직)
    @PatchMapping("/{id}")
    public ResponseEntity<Todo> updateTodoStatus(@PathVariable Long id) {
        // [⚠️ 현재 서비스 문제점]: 소유권 검증 없이 상태를 변경합니다.
        Todo toggledTodo = todoService.updateTodoStatus(id);
        return ResponseEntity.ok(toggledTodo);
    }

    // DELETE /api/todos/completed : DELETE ALL COMPLETED (완료된 항목 일괄 삭제)
    @DeleteMapping("/completed")
    public ResponseEntity<Void> clearCompletedTodos() {
        // [✅ 유일하게 안전한 로직]: userId를 전달하여 현재 사용자만 삭제
        Long userId = getCurrentUserId();
        todoService.clearCompletedTodos(userId);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/todos/{id} 및 DELETE /api/todos/{id} 메서드는
    // 현재 TodoService에 해당 메서드가 정의되어 있지 않으므로 제외했습니다.
}
