package com.example.todolist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor(force = true) // 이 어노테이션이 기본 생성자를 만듭니다.
@Getter
@Setter
@ToString
// @RequiredArgsConstructor는 제거하여 충돌 가능성을 완전히 배제했습니다.
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isCompleted = false; // 기본값 설정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appuser_id") // 외래 키 컬럼 이름 명시
    @JsonIgnore
    private AppUser user;

    /*
      DTO에서 Entity로 변환할 때 사용되는 명시적 생성자입니다.
      매개변수 목록이 다르므로 @NoArgsConstructor와 충돌하지 않습니다.
     */
    public Todo(String content, boolean isCompleted, AppUser user) {
        this.content = content;
        this.isCompleted = isCompleted;
        this.user = user;
    }
}
