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
public class AuthorService {

	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;

	public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
		this.authorRepository = authorRepository;
		this.bookRepository = bookRepository;
	}

	public List<Author> getAllAuthors() {
		return authorRepository.findAll();
	}

	public Optional<Author> getAuthorById(UUID id) {
		return authorRepository.findById(id);
	}

	public List<Author> findAuthorsByName(String name) {
		return authorRepository.findByName(name);
	}

	public Author createAuthor(Author author) {
		return authorRepository.addNew(author);
	}

	public void deleteAuthor(UUID id) {
		authorRepository.deleteById(id);
	}

	public Integer getAuthorBookCount(UUID authorId) {
		return bookRepository.findByAuthorId(authorId.toString()).size();
	}

	public Integer getAuthorBookCount(Author author) {
		return getAuthorBookCount(author.getId());
	}

	public List<Book> getBooksByAuthor(UUID authorId) {
		return bookRepository.findByAuthorId(authorId.toString());
	}
}
