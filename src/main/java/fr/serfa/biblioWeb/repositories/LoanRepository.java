package fr.serfa.biblioWeb.repositories;

import fr.serfa.biblioWeb.enums.LoanStatus;
import fr.serfa.biblioWeb.model.Loan;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LoanRepository {

	private final List<Loan> loans = new ArrayList<>();

	public LoanRepository() {}

	public List<Loan> findAll() {
		return new ArrayList<>(loans);
	}

	public Optional<Loan> findById(UUID id) {
		return loans.stream()
				.filter(loan -> loan.getId().equals(id))
				.findFirst();
	}

	public List<Loan> findByMemberId(UUID memberId) {
		return loans.stream()
				.filter(loan -> loan.getMemberId().equals(memberId))
				.collect(Collectors.toList());
	}

	public List<Loan> findActiveLoansByMemberId(UUID memberId) {
		return loans.stream()
				.filter(loan -> loan.getMemberId().equals(memberId))
				.filter(loan -> loan.getStatus() == LoanStatus.ACTIVE || loan.getStatus() == LoanStatus.OVERDUE)
				.collect(Collectors.toList());
	}

	public List<Loan> findByBookId(UUID bookId) {
		return loans.stream()
				.filter(loan -> loan.getBookId().equals(bookId))
				.collect(Collectors.toList());
	}

	public Optional<Loan> findActiveLoanByBookId(UUID bookId) {
		return loans.stream()
				.filter(loan -> loan.getBookId().equals(bookId))
				.filter(loan -> loan.getStatus() == LoanStatus.ACTIVE || loan.getStatus() == LoanStatus.OVERDUE)
				.findFirst();
	}

	public List<Loan> findByStatus(LoanStatus status) {
		return loans.stream()
				.filter(loan -> loan.getStatus() == status)
				.collect(Collectors.toList());
	}

	public List<Loan> findOverdueLoans() {
		return loans.stream()
				.filter(Loan::isOverdue)
				.collect(Collectors.toList());
	}

	public Loan addNew(Loan loan) {
		loans.add(loan);
		return loan;
	}

	public void deleteById(UUID id) {
		loans.removeIf(loan -> loan.getId().equals(id));
	}

	public boolean isBookAvailable(UUID bookId) {
		return loans.stream()
				.filter(loan -> loan.getBookId().equals(bookId))
				.noneMatch(loan -> loan.getStatus() == LoanStatus.ACTIVE || loan.getStatus() == LoanStatus.OVERDUE);
	}
}
