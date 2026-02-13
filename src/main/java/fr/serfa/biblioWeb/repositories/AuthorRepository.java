package fr.serfa.biblioWeb.repositories;

import fr.serfa.biblioWeb.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {

	List<Author> findByNameContainingIgnoreCase(String name);

	Optional<Author> findByNameIgnoreCaseAndBirthDateAndDeathDate(String name, LocalDate birthDate, LocalDate deathDate);

	boolean existsByNameIgnoreCaseAndBirthDateAndDeathDate(String name, LocalDate birthDate, LocalDate deathDate);
}
