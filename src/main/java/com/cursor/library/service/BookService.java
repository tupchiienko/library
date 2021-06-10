package com.cursor.library.service;

import com.cursor.library.dto.CreateBookDto;
import com.cursor.library.exception.CreateBookException;
import com.cursor.library.entity.Book;
import com.cursor.library.repository.BookRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class BookService {

    private final BookRepo bookRepo;

    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public Book createBook(
            final String name,
            final String author,
            final Integer year,
            final String genre
    ) throws CreateBookException {
        if (name == null) {
            throw new CreateBookException("Name field cannot be null");
        }
        if (year == null || year > 2021) {
            throw new CreateBookException("Illegal year argument");
        }
        return bookRepo.saveBook(
                name.trim(),
                author == null ? null : author.trim(),
                year,
                genre == null ? null : genre.trim());
    }

    public List<Book> getAll() {
        return bookRepo.getAll();
    }

    public List<Book> getAll(int limit, int offset) {
        return bookRepo.getAllPaged(limit, offset);
    }

    public List<Book> getAllSorted(String sort) {
        var books = bookRepo.getAll();
        books.sort(Comparator.comparing(book -> (
                sort.equals("name")
                        ? book.getName()
                        : String.valueOf(book.getYear())))
        );
        return books;
    }

    public List<Book> getAllByAuthor(String author) {
        return bookRepo.getAllByAuthor(author);
    }

    public Book update(String id, CreateBookDto createBookDto) throws CreateBookException {
        var name = createBookDto.getName();
        var author = createBookDto.getAuthor();
        var year = createBookDto.getYear();
        var genre = createBookDto.getGenre();
        if (name == null) {
            throw new CreateBookException("Name field cannot be null");
        }
        if (year == null || year > LocalDate.now().getYear()) {
            throw new CreateBookException("Illegal year argument");
        }
        return bookRepo.updateBook(id, new Book(
                id,
                name.trim(),
                author == null ? null : author.trim(),
                year,
                genre == null ? null : genre.trim()));
    }

    public Book findById(String bookId) {
        return bookRepo.findById(bookId);
    }
}
