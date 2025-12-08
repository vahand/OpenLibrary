package org.example.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Renting {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    private String renterFirstName;
    private String renterLastName;
    private String renterEmail;

    private String rentDate;
    private String returnDate;
}
