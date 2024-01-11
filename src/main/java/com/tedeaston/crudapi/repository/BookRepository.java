package com.tedeaston.crudapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tedeaston.crudapi.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
        List<Book> findByTitleContainingIgnoreCase(String title);
        List<Book> findByTitleOrAuthorOrGenreContainingIgnoreCase(String string);
        List<Book> findByTitleAndAuthorContainingIgnoreCase(String title, String author);
        List<Book> findByTitleAndGenreContainingIgnoreCase(String title, String genre);
        List<Book> findByAuthorAndGenreContainingIgnoreCase(String author, String genre);
}
