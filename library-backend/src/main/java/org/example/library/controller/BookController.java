package org.example.library.controller;

import org.example.library.model.Book;
import org.example.library.repo.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:3000") // allow frontend dev server (Vite default)
public class BookController {
    private final BookRepository repo;
    public BookController(BookRepository repo){ this.repo = repo; }

    @GetMapping
    public List<Book> list(){ return repo.findAll(); }

    @PostMapping
    public Book create(@RequestBody Book b){ return repo.save(b); }
}
