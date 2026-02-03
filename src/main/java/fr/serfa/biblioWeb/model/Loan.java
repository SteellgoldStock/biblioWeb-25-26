package fr.serfa.biblioWeb.model;

import fr.serfa.biblioWeb.enums.LoanStatus;

import java.time.LocalDate;
import java.util.UUID;

public class Loan {

	private final UUID id;

	public UUID memberId;
	public UUID bookId;

	public LocalDate loanDate;

	public LocalDate expectedReturnDate;
	public LocalDate actualReturnDate;

	public LoanStatus status;

	public Loan(UUID memberId, UUID bookId) {
		this.id = UUID.randomUUID();
		this.memberId = memberId;
		this.bookId = bookId;
		this.loanDate = LocalDate.now();
		this.expectedReturnDate = LocalDate.now().plusWeeks(2); // 2 semaines par défaut
		this.actualReturnDate = null;
		this.status = LoanStatus.ACTIVE;
	}

	public UUID getId() {
		return id;
	}

	public UUID getMemberId() {
		return memberId;
	}

	public UUID getBookId() {
		return bookId;
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