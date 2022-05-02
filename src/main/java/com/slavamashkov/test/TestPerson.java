package com.slavamashkov.test;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "test_person", schema = "schema_export")
public class TestPerson {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "uuid", updatable = false, nullable = false)
    UUID id;

    @NonNull
    @Column(name = "test_person_name", nullable = false, unique = true, length = 20)
    String name;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "department", nullable = false)
    Department department;

    @NonNull
    @Column(name = "salary", nullable = false)
    Integer salary;
}
