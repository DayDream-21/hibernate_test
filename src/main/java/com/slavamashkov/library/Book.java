package com.slavamashkov.library;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NonNull
    @Column(name = "title")
    private String title;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "books_readers",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "reader_id")
    )
    List<Reader> readers;
}