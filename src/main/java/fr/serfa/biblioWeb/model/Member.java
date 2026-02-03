package fr.serfa.biblioWeb.model;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class Member {

	private final UUID id;

	public String firstName;
	public String lastName;
	public String email;

	public final LocalDate registrationDate;

	public Map<UUID, Loan> loans;

	public Member(String firstName, String lastName, String email) {
		this.id = UUID.randomUUID();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.registrationDate = LocalDate.now();
	}

	public UUID getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}
}