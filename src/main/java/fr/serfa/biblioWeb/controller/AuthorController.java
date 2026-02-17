package fr.serfa.biblioWeb.controller;

import fr.serfa.biblioWeb.model.Author;
import fr.serfa.biblioWeb.model.Book;
import fr.serfa.biblioWeb.service.AuthorService;
import fr.serfa.biblioWeb.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

	private final AuthorService authorService;
	private final BookService bookService;

	public AuthorController(AuthorService authorService, BookService bookService) {
		this.authorService = authorService;
		this.bookService = bookService;
	}

	@GetMapping
	public ResponseEntity<List<Author>> getAllAuthors() {
		return ResponseEntity.ok(authorService.getAllAuthors());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Author> getAuthorById(@PathVariable String id) {
		return authorService.getAuthorById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/size")
	public ResponseEntity<Integer> getAllBooksSize() {
		return ResponseEntity.ok(authorService.getAllAuthors().size());
	}

	@GetMapping("/{id}/books")
	public ResponseEntity<List<Book>> getAllBooks(@PathVariable String id) {
		return ResponseEntity.ok(authorService.getBooksByAuthor(id));
	}

	@GetMapping("/{id}/books/count")
	public ResponseEntity<Integer> getAuthorBookCount(@PathVariable String id) {
		return ResponseEntity.ok(authorService.getAuthorBookCount(id));
	}

	@GetMapping("/search")
	public ResponseEntity<List<Author>> searchAuthorsByName(@RequestParam String name) {
		return ResponseEntity.ok(authorService.findAuthorsByName(name));
	}

	@PostMapping
	public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
		Author createdAuthor = authorService.createAuthor(author);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAuthor(@PathVariable String id) {
		if (authorService.getAuthorById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		authorService.deleteAuthor(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/clean")
	public ResponseEntity<Void> deleteAll() {
		// Supprimer tous les livres et auteurs via les repositories
		bookService.getAllBooks().forEach(book -> bookService.deleteBookById(book.getId()));
		authorService.getAllAuthors().forEach(author -> authorService.deleteAuthor(author.getId()));
		return ResponseEntity.noContent().build();
	}
}
