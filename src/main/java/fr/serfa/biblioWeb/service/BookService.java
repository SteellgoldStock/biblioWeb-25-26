package fr.serfa.biblioWeb.service;

import fr.serfa.biblioWeb.model.Author;
import fr.serfa.biblioWeb.model.Book;
import fr.serfa.biblioWeb.repositories.AuthorRepository;
import fr.serfa.biblioWeb.repositories.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;

	public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
	}

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public Optional<Book> getBookById(UUID id) {
		return bookRepository.findById(id);
	}

	public Optional<Book> getBookByISBN(Long isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	public List<Book> searchBooksByTitle(String title) {
		return bookRepository.findByNameContainingIgnoreCase(title);
	}

	public List<Book> getBooksByAuthor(Author author) {
		return bookRepository.findByAuthor(author);
	}

	public List<Book> getBooksByAuthorId(String authorId) {
		return bookRepository.findByAuthorId(UUID.fromString(authorId));
	}

	public List<Author> getAllAuthorsFromBooks() {
		return bookRepository.findAll().stream()
				.map(Book::getAuthor)
				.distinct()
				.collect(Collectors.toList());
	}

	public Book createBook(Book book) {
		Author author = book.getAuthor();
		if (author != null && author.getId() == null) {
			// Vérifier si l'auteur existe déjà
			Optional<Author> existingAuthor = authorRepository.findByNameIgnoreCaseAndBirthDateAndDeathDate(
					author.getName(),
					author.getBirthDate(),
					author.getDeathDate()
			);

			if (existingAuthor.isPresent()) {
				book.author = existingAuthor.get();
			} else {
				author = authorRepository.save(author);
				book.author = author;
			}
		}

		return bookRepository.save(book);
	}

	public List<Book> createBooks(List<Book> books) {
		return books.stream()
				.filter(book -> !bookRepository.existsByIsbn(book.getISBN()))
				.map(this::createBook)
				.toList();
	}

	public void deleteBookById(UUID id) {
		bookRepository.deleteById(id);
	}

	public void deleteBookByISBN(Long isbn) {
		bookRepository.findByIsbn(isbn).ifPresent(bookRepository::delete);
	}

	public boolean existsByIsbn(Long isbn) {
		return bookRepository.existsByIsbn(isbn);
	}
}
