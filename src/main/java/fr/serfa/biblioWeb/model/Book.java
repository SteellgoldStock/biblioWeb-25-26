package fr.serfa.biblioWeb.model;

import java.util.UUID;

public class Book {

	private final UUID id;

	public String name;
	public Long isbn;
	public Author author;

	public Book(String name, Long isbn, Author author) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.isbn = isbn;
		this.author = author;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Author getAuthor() {
		return author;
	}

	public Long getISBN() {
		return isbn;
	}
}