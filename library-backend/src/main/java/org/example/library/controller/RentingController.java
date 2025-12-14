package org.example.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import org.example.library.model.Book;
import org.example.library.model.Renting;
import org.example.library.repo.BookRepository;
import org.example.library.repo.RentingRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentings")
public class RentingController {
    private final RentingRepository repo;
    private final BookRepository bookRepository;
    private final Connection natsConnection;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RentingController(RentingRepository repo, BookRepository bookRepository, Connection natsConnection) {
        this.repo = repo;
        this.bookRepository = bookRepository;
        this.natsConnection = natsConnection;
    }

    // Get all rentings
    @GetMapping
    public List<Renting> list() {
        return repo.findAll();
    }

    // Get a renting by ID
    @GetMapping("/{id}")
    public Renting getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    // Create a new renting
    @PostMapping
    public Renting create(@RequestBody Renting r) {
        // Set book availability to false when creating a renting
        Long bookId = r.getBookId();
        Book b = this.bookRepository.findById(bookId).orElse(null);
        if (b != null) {
            if (!b.isAvailable()) {
                throw new RuntimeException("Book is not available for renting");
            } else {
                b.setAvailable(false);
                this.bookRepository.save(b);
            }
        } else {
            throw new RuntimeException("Book not found for renting creation");
        }

        // Set dates
        r.setRentDate(DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()));
        r.setReturnDate(DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now().plusDays(14)));

        Renting savedRenting = repo.save(r);

        // Publish event to NATS
        try {
            var event = Map.of(
                    "eventType", "BOOK_RENTED",
                    "to", savedRenting.getRenterEmail(),
                    "subject", "Book Rented: " + b.getTitle(),
                    "body", "You have successfully rented the book '" + b.getTitle() + "'. Return by: "
                            + savedRenting.getReturnDate());
            String json = objectMapper.writeValueAsString(event);
            natsConnection.publish("book.rented", json.getBytes());
            System.out.println("Published event to book.rented: " + json);
        } catch (Exception e) {
            System.err.println("Failed to publish event: " + e.getMessage());
        }

        return savedRenting;
    }

    // Update an existing renting
    @PutMapping("/{id}")
    public Renting update(@PathVariable Long id, @RequestBody Renting r) {
        r.setId(id);
        return repo.save(r);
    }

    // Delete a renting by ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Renting r = repo.findById(id).orElse(null);
        if (r == null) {
            throw new RuntimeException("Renting not found for deletion");
        }

        // Set book availability to true when deleting a renting
        Long bookId = r.getBookId();
        Book b = this.bookRepository.findById(bookId).orElse(null);
        if (b != null) {
            b.setAvailable(true);
            this.bookRepository.save(b);
        } else {
            throw new RuntimeException("Book not found for renting deletion");
        }

        repo.deleteById(id);

        // Publish event to NATS
        try {
            var event = Map.of(
                    "eventType", "BOOK_RETURNED",
                    "to", r.getRenterEmail(),
                    "subject", "Book Returned: " + (b != null ? b.getTitle() : "Unknown Book"),
                    "body", "You have successfully returned the book. Thank you!");
            String json = objectMapper.writeValueAsString(event);
            natsConnection.publish("book.returned", json.getBytes());
            System.out.println("Published event to book.returned: " + json);
        } catch (Exception e) {
            System.err.println("Failed to publish return event: " + e.getMessage());
        }
    }
}
