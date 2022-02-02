package com.slavamashkov.library;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "books_readers")
public class BooksReaders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;
}