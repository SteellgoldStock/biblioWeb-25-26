package fr.serfa.biblioWeb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "authors")
public class Author {

	@Id
	@UuidGenerator
	private String id;

	@Column(nullable = false)
	@NotBlank
	public String name;

	@Column(name = "birth_date", nullable = false)
	@NotNull
	public LocalDate birthDate;

	@Column(name = "death_date")
	public LocalDate deathDate;

	public Author() {
	}

	public Author(String name, LocalDate birthDate, LocalDate deathDate) {
		this.name = name;
		this.birthDate = birthDate;
		this.deathDate = deathDate;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isDead() {
		return deathDate != null;
	}

	public Integer getAge() {
		if (isDead()) {
			return deathDate.getYear() - birthDate.getYear();
		}

		return LocalDate.now().getYear() - birthDate.getYear();
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public LocalDate getDeathDate() {
		return deathDate;
	}

	// public String getDescription() {
	// 	StringBuilder desc = new StringBuilder("L'auteur ");
	// 	desc.append(this.name).append(" est né(e) le ").append(formatedDate(this.birthDate));

	// 	if (this.deathDate != null) {
	// 		desc.append(" et est mort le ").append(formatedDate(this.deathDate))
	// 				.append(" à l'âge de ").append(this.getAge()).append(" ans.");
	// 	} else {
	// 		desc.append(" et a ").append(this.getAge()).append(" ans.");
	// 	}
//
	// 	return desc.toString();
	// }

	// public String formatedDate(LocalDate date) {
	// 	return date.getDayOfMonth()+"/"+date.getMonthValue()+"/"+date.getYear();
	// }
}
