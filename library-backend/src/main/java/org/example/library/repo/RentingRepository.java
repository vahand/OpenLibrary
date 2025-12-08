package org.example.library.repo;

import org.example.library.model.Renting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentingRepository extends JpaRepository<Renting, Long> {
}
