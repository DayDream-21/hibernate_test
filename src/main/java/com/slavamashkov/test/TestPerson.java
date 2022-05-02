package com.slavamashkov.test;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "test_person", schema = "schema_export")
public class TestPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NonNull
    @Column(name = "test_person_name", nullable = false, unique = true, length = 20)
    String name;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "department", nullable = false)
    Department department;
}
