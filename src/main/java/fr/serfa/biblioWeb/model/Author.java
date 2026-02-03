package fr.serfa.biblioWeb.model;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class Author {

	private final UUID id;

	public String name;
	public LocalDate birthDate;
	public LocalDate deathDate;

	public Author (String name, LocalDate birthDate, LocalDate deathDate) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.birthDate = birthDate;
		this.deathDate = deathDate;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isDead () {
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