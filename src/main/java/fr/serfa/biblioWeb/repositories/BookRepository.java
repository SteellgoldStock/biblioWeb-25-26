package fr.serfa.biblioWeb.repositories;

import fr.serfa.biblioWeb.model.Author;
import fr.serfa.biblioWeb.model.Book;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BookRepository {

	private final List<Book> books = new ArrayList<>();

	public BookRepository() {
	}

	public List<Book> findAll() {
		return new ArrayList<>(books);
	}

	public Optional<Book> findById(UUID id) {
		return books.stream()
				.filter(book -> book.getId().equals(id))
				.findFirst();
	}

	public List<Book> findByTitle(String title) {
		return books.stream()
				.filter(book -> book.getName().toLowerCase().contains(title.toLowerCase()))
				.collect(Collectors.toList());
	}

	public Optional<Book> findByISBN(Long isbn) {
		return books.stream()
				.filter(book -> book.getISBN().equals(isbn))
				.findFirst();
	}

	public boolean existsByIsbn(Long isbn) {
		return books.stream()
				.anyMatch(book -> book.getISBN().equals(isbn));
	}

	public List<Book> findByAuthor(Author author) {
		return books.stream()
				.filter(book -> book.getAuthor().equals(author))
				.collect(Collectors.toList());
	}

	public List<Book> findByAuthorId(String id) {
		return books.stream()
				.filter(book -> book.getAuthor().getId().equals(UUID.fromString(id)))
				.collect(Collectors.toList());
	}

	public Book addNew(Book book) {
		books.add(book);
		return book;
	}

	public void deleteById(UUID id) {
		books.removeIf(book -> book.getId().equals(id));
	}

	public void deleteByISBN(Long isbn) {
		books.removeIf(book -> Objects.equals(book.getISBN(), isbn));
	}

	public List<Author> findAllAuthors() {
		return books.stream()
				.map(Book::getAuthor)
				.distinct()
				.collect(Collectors.toList());
	}
}