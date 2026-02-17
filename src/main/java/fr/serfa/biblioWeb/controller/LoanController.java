package fr.serfa.biblioWeb.controller;

import fr.serfa.biblioWeb.model.Loan;
import fr.serfa.biblioWeb.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/loans")
public class LoanController {

	private final LoanService loanService;

	public LoanController(LoanService loanService) {
		this.loanService = loanService;
	}

	@GetMapping
	public ResponseEntity<List<Loan>> getAllLoans() {
		return ResponseEntity.ok(loanService.getAllLoans());
	}

	@GetMapping("/overdues")
	public ResponseEntity<List<Loan>> getOverdueLoans() {
		return ResponseEntity.ok(loanService.getOverdueLoans());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Loan> getLoanById(@PathVariable String id) {
		return loanService.getLoanById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/member/{memberId}")
	public ResponseEntity<List<Loan>> getLoansByMemberId(@PathVariable String memberId) {
		return ResponseEntity.ok(loanService.getLoansByMemberId(memberId));
	}

	@GetMapping("/member/{memberId}/active")
	public ResponseEntity<List<Loan>> getActiveLoansByMemberId(@PathVariable String memberId) {
		return ResponseEntity.ok(loanService.getActiveLoansByMemberId(memberId));
	}

	@GetMapping("/book/{bookId}")
	public ResponseEntity<List<Loan>> getLoansByBookId(@PathVariable String bookId) {
		return ResponseEntity.ok(loanService.getLoansByBookId(bookId));
	}

	@GetMapping("/book/{bookId}/active")
	public ResponseEntity<Loan> getActiveLoanByBookId(@PathVariable String bookId) {
		return loanService.getActiveLoanByBookId(bookId)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/book/{bookId}/availability")
	public ResponseEntity<Map<String, Boolean>> checkBookAvailability(@PathVariable String bookId) {
		boolean isAvailable = loanService.isBookAvailable(bookId);
		return ResponseEntity.ok(Map.of("available", isAvailable));
	}

	@PostMapping
	public ResponseEntity<Loan> createLoan(@RequestBody Map<String, String> loanRequest) {
		try {
			String memberId = loanRequest.get("memberId");
			String bookId = loanRequest.get("bookId");

			Loan createdLoan = loanService.createLoan(memberId, bookId);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@PutMapping("/{id}/return")
	public ResponseEntity<Loan> returnLoan(@PathVariable String id) {
		try {
			Loan returnedLoan = loanService.returnLoan(id);
			return ResponseEntity.ok(returnedLoan);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLoan(@PathVariable String id) {
		loanService.deleteLoan(id);
		return ResponseEntity.noContent().build();
	}
}