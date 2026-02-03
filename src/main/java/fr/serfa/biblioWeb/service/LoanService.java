package fr.serfa.biblioWeb.service;

import fr.serfa.biblioWeb.enums.LoanStatus;
import fr.serfa.biblioWeb.model.Loan;
import fr.serfa.biblioWeb.repositories.LoanRepository;
import fr.serfa.biblioWeb.repositories.BookRepository;
import fr.serfa.biblioWeb.repositories.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoanService {

	private final LoanRepository loanRepository;
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;

	public LoanService(LoanRepository loanRepository, BookRepository bookRepository, MemberRepository memberRepository) {
		this.loanRepository = loanRepository;
		this.bookRepository = bookRepository;
		this.memberRepository = memberRepository;
	}

	public List<Loan> getAllLoans() {
		List<Loan> loans = loanRepository.findAll();
		loans.forEach(Loan::updateStatus);
		return loans;
	}

	public Optional<Loan> getLoanById(UUID id) {
		Optional<Loan> loan = loanRepository.findById(id);
		loan.ifPresent(Loan::updateStatus);
		return loan;
	}

	public List<Loan> getLoansByMemberId(UUID memberId) {
		List<Loan> loans = loanRepository.findByMemberId(memberId);
		loans.forEach(Loan::updateStatus);
		return loans;
	}

	public List<Loan> getActiveLoansByMemberId(UUID memberId) {
		List<Loan> loans = loanRepository.findActiveLoansByMemberId(memberId);
		loans.forEach(Loan::updateStatus);
		return loans;
	}

	public List<Loan> getLoansByBookId(UUID bookId) {
		List<Loan> loans = loanRepository.findByBookId(bookId);
		loans.forEach(Loan::updateStatus);
		return loans;
	}

	public Optional<Loan> getActiveLoanByBookId(UUID bookId) {
		Optional<Loan> loan = loanRepository.findActiveLoanByBookId(bookId);
		loan.ifPresent(Loan::updateStatus);
		return loan;
	}

	public List<Loan> getOverdueLoans() {
		return loanRepository.findOverdueLoans();
	}

	public boolean isBookAvailable(UUID bookId) {
		return loanRepository.isBookAvailable(bookId);
	}

	public Loan createLoan(UUID memberId, UUID bookId) {
		if (memberRepository.findById(memberId).isEmpty()) {
			throw new IllegalArgumentException("Member not found");
		}

		if (bookRepository.findById(bookId).isEmpty()) {
			throw new IllegalArgumentException("Book not found");
		}

		if (!isBookAvailable(bookId)) {
			throw new IllegalStateException("Book is not available");
		}

		Loan loan = new Loan(memberId, bookId);
		return loanRepository.addNew(loan);
	}

	public Loan returnLoan(UUID loanId) {
		Optional<Loan> optionalLoan = loanRepository.findById(loanId);
		
		if (optionalLoan.isEmpty()) {
			throw new IllegalArgumentException("Loan not found");
		}

		Loan loan = optionalLoan.get();
		
		if (loan.getStatus() == LoanStatus.RETURNED) {
			throw new IllegalStateException("Book already returned");
		}

		loan.returnBook();
		return loan;
	}

	public void deleteLoan(UUID id) {
		loanRepository.deleteById(id);
	}
}
