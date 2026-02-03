package fr.serfa.biblioWeb.service;

import fr.serfa.biblioWeb.model.Author;
import fr.serfa.biblioWeb.model.Book;
import fr.serfa.biblioWeb.repositories.AuthorRepository;
import fr.serfa.biblioWeb.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
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
		return bookRepository.findByISBN(isbn);
	}

	public List<Book> searchBooksByTitle(String title) {
		return bookRepository.findByTitle(title);
	}

	public List<Book> getBooksByAuthor(Author author) {
		return bookRepository.findByAuthor(author);
	}

	public List<Book> getBooksByAuthorId(String authorId) {
		return bookRepository.findByAuthorId(authorId);
	}

	public List<Author> getAllAuthorsFromBooks() {
		return bookRepository.findAllAuthors();
	}

	public Book createBook(Book book) {
		Author author = book.getAuthor();
		if (author != null) {
			boolean authorExists = authorRepository.existsByNameAndDates(
					author.getName(),
					author.getBirthDate(),
					author.getDeathDate()
			);

			if (!authorExists) {
				authorRepository.addNew(author);
			}
		}

		return bookRepository.addNew(book);
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
		bookRepository.deleteByISBN(isbn);
	}

	public boolean existsByIsbn(Long isbn) {
		return bookRepository.existsByIsbn(isbn);
	}
}