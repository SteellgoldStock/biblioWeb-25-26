package fr.serfa.biblioWeb.service;

import fr.serfa.biblioWeb.enums.LoanStatus;
import fr.serfa.biblioWeb.model.Book;
import fr.serfa.biblioWeb.model.Loan;
import fr.serfa.biblioWeb.model.Member;
import fr.serfa.biblioWeb.repositories.LoanRepository;
import fr.serfa.biblioWeb.repositories.BookRepository;
import fr.serfa.biblioWeb.repositories.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
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

	public Optional<Loan> getLoanById(String id) {
		Optional<Loan> loan = loanRepository.findById(id);
		loan.ifPresent(Loan::updateStatus);
		return loan;
	}

	public List<Loan> getLoansByMemberId(String memberId) {
		List<Loan> loans = loanRepository.findByMemberId(memberId);
		loans.forEach(Loan::updateStatus);
		return loans;
	}

	public List<Loan> getActiveLoansByMemberId(String memberId) {
		List<Loan> loans = loanRepository.findActiveLoansByMemberId(memberId);
		loans.forEach(Loan::updateStatus);
		return loans;
	}

	public List<Loan> getLoansByBookId(String bookId) {
		List<Loan> loans = loanRepository.findByBookId(bookId);
		loans.forEach(Loan::updateStatus);
		return loans;
	}

	public Optional<Loan> getActiveLoanByBookId(String bookId) {
		Optional<Loan> loan = loanRepository.findActiveLoanByBookId(bookId);
		loan.ifPresent(Loan::updateStatus);
		return loan;
	}

	public List<Loan> getOverdueLoans() {
		return loanRepository.findOverdueLoans();
	}

	public boolean isBookAvailable(String bookId) {
		return loanRepository.isBookAvailable(bookId);
	}

	public Loan createLoan(String memberId, String bookId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new IllegalArgumentException("Member not found"));

		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new IllegalArgumentException("Book not found"));

		if (!isBookAvailable(bookId)) {
			throw new IllegalStateException("Book is not available");
		}

		Loan loan = new Loan(member, book);
		return loanRepository.save(loan);
	}

	public Loan returnLoan(String loanId) {
		Loan loan = loanRepository.findById(loanId)
				.orElseThrow(() -> new IllegalArgumentException("Loan not found"));

		if (loan.getStatus() == LoanStatus.RETURNED) {
			throw new IllegalStateException("Book already returned");
		}

		loan.returnBook();
		return loanRepository.save(loan);
	}

	public void deleteLoan(String id) {
		loanRepository.deleteById(id);
	}
}
