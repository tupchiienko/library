package com.cursor.library.controller;

import com.cursor.library.dto.CreateBookDto;
import com.cursor.library.entity.Book;
import com.cursor.library.exception.CreateBookException;
import com.cursor.library.service.BookService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(
            value = "/books",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Book> createBook(
            @RequestBody final CreateBookDto createBookDto
    ) throws CreateBookException {
        final Book book = bookService.createBook(createBookDto.getName(), createBookDto.getAuthor(), createBookDto.getYear(), createBookDto.getGenre());
        return ResponseEntity.ok(book);
    }

    @GetMapping(
            value = "/books",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Book>> getBooks() {
        var result = bookService.getAll();
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(
            value = "/books",
            params = {"limit", "offset"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Book>> getBooksPaged(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset
    ) {
        var result = bookService.getAll(limit, offset);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(
            value = "/books",
            params = {"sort"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Book>> getSortedBooks(
            @RequestParam(value = "sort", defaultValue = "name") String sort
    ) {
        if (!sort.equals("name") && !sort.equals("year")) {
            return ResponseEntity.badRequest().build();
        }
        var result = bookService.getAllSorted(sort);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(
            value = "/books",
            params = {"author"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Book>> getBooksByAuthor(
            @RequestParam(value = "author") String author
    ) {
        var result = bookService.getAllByAuthor(author);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping(
            value = "/books/{bookId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Book> updateBook(
            @PathVariable(value = "bookId") String id,
            @RequestBody final CreateBookDto createBookDto
    ) throws CreateBookException {
        var result = bookService.update(id, createBookDto);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(
            value = "/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Book> getBook(@PathVariable("bookId") String bookId) {
        final Book result = bookService.findById(bookId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
