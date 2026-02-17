package fr.serfa.biblioWeb.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import fr.serfa.biblioWeb.enums.LoanStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "loans")
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id", nullable = false)
	@NotNull
	@JsonManagedReference
	public Member member;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_id", nullable = false)
	@NotNull
	@JsonManagedReference
	public Book book;

	@Column(name = "loan_date", nullable = false)
	public LocalDate loanDate;

	@Column(name = "expected_return_date", nullable = false)
	public LocalDate expectedReturnDate;

	@Column(name = "actual_return_date")
	public LocalDate actualReturnDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public LoanStatus status;

	public Loan() {
	}

	public Loan(Member member, Book book) {
		this.member = member;
		this.book = book;
		this.loanDate = LocalDate.now();
		this.expectedReturnDate = LocalDate.now().plusWeeks(2); // 2 semaines par défaut
		this.actualReturnDate = null;
		this.status = LoanStatus.ACTIVE;
	}

	public String getId() {
		return id;
	}

	public Member getMember() {
		return member;
	}

	public Book getBook() {
		return book;
	}

	public String getMemberId() {
		return member != null ? member.getId() : null;
	}

	public String getBookId() {
		return book != null ? book.getId() : null;
	}

	public LoanStatus getStatus() {
		return status;
	}

	public void returnBook() {
		this.actualReturnDate = LocalDate.now();
		this.status = LoanStatus.RETURNED;
	}

	public boolean isOverdue() {
		if (status == LoanStatus.RETURNED) {
			return false;
		}

		return LocalDate.now().isAfter(expectedReturnDate);
	}

	public void updateStatus() {
		if (status != LoanStatus.RETURNED && isOverdue()) {
			this.status = LoanStatus.OVERDUE;
		}
	}
}
