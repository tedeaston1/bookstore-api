package com.tedeaston.crudapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tedeaston.crudapi.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
        List<Book> findByTitleContaining(String title);
}
