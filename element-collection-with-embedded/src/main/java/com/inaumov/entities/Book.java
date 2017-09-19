package com.inaumov.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@ToString
@Entity
@Table(
        name = "BOOK",
        uniqueConstraints = @UniqueConstraint(
                name = "book_id_name_unique",
                columnNames = {"BOOK_ID", "NAME"}
        )
)
public class Book {

    @Id
    @Column(name = "BOOK_ID")
    private String bookId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ACTIVE")
    private boolean active;

    @Embedded
    private Author author;

    @ElementCollection
    @CollectionTable(
            name = "BOOK_WORDS",
            joinColumns = @JoinColumn(name = "BOOK_ID")
    )
    @Column(name = "WORD_ID")
    private List<String> words;

}