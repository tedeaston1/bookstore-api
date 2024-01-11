package com.tedeaston.crudapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tedeaston.crudapi.model.Book;
import com.tedeaston.crudapi.repository.BookRepository;
import com.tedeaston.crudapi.service.BookService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    /* 
     * returns all books in repository if request param string (null)
     * otherwise returns books with a title containing the request param string
     */
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) String title) {
        try{
            List<Book> books = new ArrayList<Book>();
            
            if (title == null){
                bookRepository.findAll().forEach(books::add);
            } else {
                bookRepository.findByTitleContainingIgnoreCase(title).forEach(books::add);
            }

            if (books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);      
            }
            
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * returns book with an id matching the path variable if book with id exists
     * otherwise returns Http status not found
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") long id) {
        Optional<Book> bookData = bookRepository.findById(id);

        if (bookData.isPresent()) {
            return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
     * creates book object deriving attributes from posted object
     * otherwise returns Http status internal server error
     */
    @PostMapping("/books")
        public ResponseEntity<Book> createBookEntry(@RequestBody Book book) {
            try {
                Book newBook = bookRepository.save(new Book(book.getTitle(), book.getAuthor(), book.getGenre(), book.getPrice()));
                return new  ResponseEntity<>(newBook, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * if book with id matching param variable exists, gets book and updates attribute values to put-requested book object
     * otherwise returns Http status not found
     */
    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book book) {
        Optional<Book> bookData = bookRepository.findById(id);

        if(bookData.isPresent()) {
            Book updatedBook = bookData.get();
            updatedBook.setTitle(book.getTitle());
            updatedBook.setAuthor(book.getAuthor());
            updatedBook.setGenre(book.getGenre());
            updatedBook.setPrice(book.getPrice());
            return new ResponseEntity<>(bookRepository.save(updatedBook), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
     * if book with id matching path variable exists, delete book from repository
     * otherwise return Https status internal server error
     */
    @DeleteMapping("/books/{id}") 
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") long id) {
        try {
            bookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * deletes all books in repository
     * otherwise returns Http status internal server error
     */
    @DeleteMapping("/books")
    public ResponseEntity<HttpStatus> deleteAllBooks() {
        try {
            bookRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBookByTitleOrAuthorOrGenre(
        @RequestParam(defaultValue = "null") String title,
        @RequestParam(defaultValue = "null") String author,
        @RequestParam(defaultValue = "null") String genre) {
        try {
            List<Book> books = bookService.handleSearchCriteria(title, author, genre);
            
            if (books.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(books, HttpStatus.OK);
            }
            
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
