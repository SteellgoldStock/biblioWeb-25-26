package fr.serfa.biblioWeb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "members")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "first_name", nullable = false)
	@NotBlank
	public String firstName;

	@Column(name = "last_name", nullable = false)
	@NotBlank
	public String lastName;

	@Column(unique = true, nullable = false)
	@Email
	@NotBlank
	public String email;

	@Column(name = "registration_date", nullable = false, updatable = false)
	public LocalDate registrationDate;

	public Member() {
	}

	public Member(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.registrationDate = LocalDate.now();
	}

	public String getId() {
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
