package com.inaumov.jpa;

import com.inaumov.entities.Author;
import com.inaumov.entities.Book;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;

@Slf4j
public class JpaElementCollectionWithEmbeddedTest extends ATestDbInitializer {

    private ArrayList<String> words;

    @Override
    public void setUp() {
        super.setUp();
        words = new ArrayList<>();

        words.add("Lorem");
        words.add("ipsum");
        words.add("dolor");
        words.add("sit");
        words.add("amet,");
        words.add("consectetur");
        words.add("adipiscing");
        words.add("elit,");
        words.add("sed");
        words.add("do");
        words.add("eiusmod");
        words.add("tempor");
        words.add("incididunt");
        words.add("ut");
        words.add("labore");
        words.add("et");
        words.add("dolore");
        words.add("magna");
        words.add("aliqua");
    }

    @Test
    public void testCreateNewBook() throws Exception {
        Book book = new Book();
        book.setBookId("test_id");
        book.setActive(false);
        book.setName("Test book");
        Author author = new Author();
        author.setName("Me");
        author.setIsbn("978-3-16-148410-0");
        book.setAuthor(author);
        book.setWords(words);
        entityManager.persist(book);
        commitTransaction();
    }

    @Test
    public void testUpdateWords() throws Exception {
        Book book = entityManager.find(Book.class, "book_3");
        book.setWords(words);
        entityManager.merge(book);
        commitTransaction();
    }

}