package com.tedeaston.crudapi.service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tedeaston.crudapi.model.Book;
import com.tedeaston.crudapi.repository.BookRepository;

@Service
public class BookService {

    BookRepository bookRepository;

    public List<Book> handleSearchCriteria(String title, String author, String genre) {
        List<Book> books = new ArrayList<>();

        if (checkForSpecificCriteria(new String[]{title, author, genre})) {
            //checkWhichQuery
        };
        return books;

    }

    public boolean checkForSpecificCriteria(String[] keywords) {
        
        if (keywords.length < 2){
            return false;
        }

        return true;
    }

    public List<Book> handleWideSearchCriteria(String title, String author, String genre){
        List<String> keywords = new ArrayList<String>(Arrays.asList(title, author, genre));
        List<Book> books = new ArrayList<>();

        for (String keyword : keywords){
            if (keyword != null){
                bookRepository
                .findByTitleOrAuthorOrGenreContainingIgnoreCase(keyword)
                .forEach(books::add);;
            }
        }
        List<Book> selectedBooks = new ArrayList<>(new HashSet<>(books));

        return selectedBooks;
    }

}
