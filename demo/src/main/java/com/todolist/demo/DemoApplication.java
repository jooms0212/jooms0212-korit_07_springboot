package com.todolist.demo;

import com.todolist.demo.domain.Todo;
import com.todolist.demo.domain.TodoRepository;
import com.todolist.demo.domain.User;
import com.todolist.demo.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepository, TodoRepository todoRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// 테스트용 user 생성
			User user = new User("user", passwordEncoder.encode("user"), "USER");
			userRepository.save(user);

			// 테스트용 to-do 목록 생성
			todoRepository.save(new Todo("스프링부트 프로젝트 만들기", user));
			todoRepository.save(new Todo("리엑트 연동하기", user));

		};
	}
}
