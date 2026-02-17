package fr.serfa.biblioWeb.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@UuidGenerator
	private String id;

	@Column(nullable = false)
	@NotBlank
	public String name;

	@Column(unique = true, nullable = false)
	@NotNull
	public Long isbn;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id", nullable = false)
	@NotNull
	@JsonManagedReference
	public Author author;

	public Book() {
	}

	public Book(String name, Long isbn, Author author) {
		this.name = name;
		this.isbn = isbn;
		this.author = author;
	}

	public String getId() {
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
