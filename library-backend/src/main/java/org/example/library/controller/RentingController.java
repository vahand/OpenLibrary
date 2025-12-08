package org.example.library.controller;

import org.example.library.model.Renting;
import org.example.library.repo.RentingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentings")
public class RentingController {
    private final RentingRepository repo;
    public RentingController(RentingRepository repo){ this.repo = repo; }

    @GetMapping
    public List<Renting> list() {
        return repo.findAll();
    }

    @PostMapping
    public Renting create(@RequestBody Renting r) {
        return repo.save(r);
    }
}
