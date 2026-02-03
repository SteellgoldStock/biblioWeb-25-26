package fr.serfa.biblioWeb.repositories;

import fr.serfa.biblioWeb.model.Author;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class AuthorRepository {

	private final List<Author> authors = new ArrayList<>();

	public AuthorRepository() {
	}

	public List<Author> findAll() {
		return new ArrayList<>(authors);
	}

	public Optional<Author> findById(UUID id) {
		return authors.stream()
				.filter(author -> author.getId().equals(id))
				.findFirst();
	}

	public List<Author> findByName(String name) {
		return authors.stream()
				.filter(author -> author.getName().toLowerCase().contains(name.toLowerCase()))
				.collect(Collectors.toList());
	}

	public boolean existsByNameAndDates(String name, java.time.LocalDate birthDate, java.time.LocalDate deathDate) {
		return authors.stream()
				.anyMatch(author ->
						author.getName().equalsIgnoreCase(name) &&
								author.birthDate.equals(birthDate) &&
								((author.deathDate == null && deathDate == null) ||
										(author.deathDate != null && author.deathDate.equals(deathDate)))
				);
	}

	public Author addNew(Author author) {
		authors.add(author);
		return author;
	}

	public void deleteById(UUID id) {
		authors.removeIf(author -> author.getId().equals(id));
	}
}