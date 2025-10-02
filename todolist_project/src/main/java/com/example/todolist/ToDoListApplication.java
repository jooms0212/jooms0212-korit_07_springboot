package com.example.todolist;

import com.example.todolist.domain.AppUser;
import com.example.todolist.domain.AppUserRepository;
import com.example.todolist.domain.Todo;
import com.example.todolist.domain.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ToDoListApplication {

	private final PasswordEncoder passwordEncoder;

	public ToDoListApplication(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	public static void main(String[] args) {
		SpringApplication.run(ToDoListApplication.class, args);
	}

	// 초기 데이터 삽입 로직
	@Bean
	public CommandLineRunner initData(AppUserRepository appUserRepository, TodoRepository todoRepository) {
		return args -> {
			// --- 1. 테스트 사용자 생성 및 저장 (비밀번호 암호화 필수) ---

			// 비밀번호 암호화
			String encodedPassword = passwordEncoder.encode("testpass");

			// 테스트 사용자 생성 (final 필드를 고려하여 AppUser 생성자 사용)
			// AppUser(username, password, role)
			AppUser testUser = new AppUser("testuser", encodedPassword, "USER");
			AppUser adminUser = new AppUser("admin", passwordEncoder.encode("adminpass"), "ADMIN");

			// 데이터베이스에 사용자 저장
			appUserRepository.save(testUser);
			appUserRepository.save(adminUser);

			// --- 2. 테스트 할 일 (Todo) 데이터 생성 ---

			// Todo 생성자: Todo(String content, boolean isCompleted, AppUser user)

			// testUser의 할 일 목록
			todoRepository.save(new Todo("Spring Security 설정 확인", false, testUser));
			todoRepository.save(new Todo("JWT 인증 플로우 테스트", false, testUser));
			todoRepository.save(new Todo("완료된 항목 일괄 삭제 기능 확인", true, testUser)); // 완료된 항목

			// adminUser의 할 일 목록
			todoRepository.save(new Todo("어드민 전용 기능 설계", false, adminUser));

			System.out.println("--- 초기 데이터 삽입 완료 (testuser/testpass, admin/adminpass) ---");
		};
	}
}
