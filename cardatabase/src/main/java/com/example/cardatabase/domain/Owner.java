package com.example.cardatabase.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ownerId;

    @NonNull
    private String firstName;
    @NonNull
    private String lastName;

    // 소유자는 다수의 차들을 가질 수 있기 때문에 Collections를 사용.
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Car> cars;
}
