package fr.serfa.biblioWeb.controller;

import fr.serfa.biblioWeb.model.Author;
import fr.serfa.biblioWeb.model.Book;
import fr.serfa.biblioWeb.model.Loan;
import fr.serfa.biblioWeb.service.BookService;
import fr.serfa.biblioWeb.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.NumberOfInterveningJobs;
import java.util.*;

@RestController
@RequestMapping("/books")
public class BookController {

	private final BookService bookService;
	private final LoanService loanService;

	public BookController(BookService bookService, LoanService loanService) {
		this.bookService = bookService;
		this.loanService = loanService;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<Book>> getAllBooks() {
		return ResponseEntity.ok(bookService.getAllBooks());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable String id) {
		return bookService.getBookById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/size")
	public ResponseEntity<Integer> getAllBooksSize() {
		return ResponseEntity.ok(bookService.getAllBooks().size());
	}

	@GetMapping("/isbn/{isbn}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Book> getBookByISBN(@PathVariable Long isbn) {
		return bookService.getBookByISBN(isbn)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/search")
	public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
		return ResponseEntity.ok(bookService.searchBooksByTitle(title));
	}

	@GetMapping("/authors")
	public ResponseEntity<List<Author>> getAllAuthors() {
		return ResponseEntity.ok(bookService.getAllAuthorsFromBooks());
	}

	@PostMapping
	public ResponseEntity<Integer> createBooks(@RequestBody List<Book> books) {
		Integer booksCreated = 0;

		for (Book book : books) {
			if (bookService.existsByIsbn(book.getISBN())) {
				if (books.size() == 1) {
					return ResponseEntity.status(HttpStatus.CONFLICT).body(booksCreated);
				}

				continue;
			}

			booksCreated++;
			bookService.createBook(book);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(booksCreated);
	}

	@GetMapping("/{id}/availability")
	public ResponseEntity<Map<String, Boolean>> checkBookAvailability(@PathVariable String id) {
		boolean isAvailable = loanService.isBookAvailable(id);
		return ResponseEntity.ok(Map.of("available", isAvailable));
	}

	@GetMapping("/{id}/loans")
	public ResponseEntity<List<Loan>> getBookLoanHistory(@PathVariable String id) {
		return ResponseEntity.ok(loanService.getLoansByBookId(id));
	}

	@GetMapping("/{id}/current-borrower")
	public ResponseEntity<Loan> getCurrentBorrower(@PathVariable String id) {
		return loanService.getActiveLoanByBookId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable String id) {
		if (id.matches("\\d+")) {
			bookService.deleteBookByISBN(Long.parseLong(id));
		} else {
			bookService.deleteBookById(id);
		}

		return ResponseEntity.noContent().build();
	}
}