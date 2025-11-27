package org.example.library.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @GetMapping("/status")
    public String status() {
        return "Library Backend is running.";
    }
}
