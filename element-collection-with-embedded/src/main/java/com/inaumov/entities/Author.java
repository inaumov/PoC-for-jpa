package com.inaumov.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class Author {

    @Column(name = "AUTHOR_NAME")
    private String name;

    @Column(name = "ISBN")
    private String isbn;

}
