package org.example.library.controller;

import org.example.library.model.Book;
import org.example.library.repo.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookRepository repo;
    public BookController(BookRepository repo){ this.repo = repo; }

    // Get all books
    @GetMapping
    public List<Book> list(){ return repo.findAll(); }

    // Get a book by ID
    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    // Create a new book
    @PostMapping
    public Book create(@RequestBody Book b){ return repo.save(b); }

    // Update an existing book
    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book b){
        b.setId(id);
        return repo.save(b);
    }

    // Delete a book by ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ repo.deleteById(id); }
}
