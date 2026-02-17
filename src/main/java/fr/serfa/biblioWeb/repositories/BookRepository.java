package fr.serfa.biblioWeb.repositories;

import fr.serfa.biblioWeb.model.Author;
import fr.serfa.biblioWeb.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

	Optional<Book> findByIsbn(Long isbn);

	List<Book> findByNameContainingIgnoreCase(String name);

	boolean existsByIsbn(Long isbn);

	List<Book> findByAuthor(Author author);

	List<Book> findByAuthorId(String authorId);
}
