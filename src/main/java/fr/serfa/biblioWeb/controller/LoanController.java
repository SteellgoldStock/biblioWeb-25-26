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

	@GetMapping("/{id}")
	public ResponseEntity<Loan> getLoanById(@PathVariable UUID id) {
		return loanService.getLoanById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/member/{memberId}")
	public ResponseEntity<List<Loan>> getLoansByMemberId(@PathVariable UUID memberId) {
		return ResponseEntity.ok(loanService.getLoansByMemberId(memberId));
	}

	@GetMapping("/member/{memberId}/active")
	public ResponseEntity<List<Loan>> getActiveLoansByMemberId(@PathVariable UUID memberId) {
		return ResponseEntity.ok(loanService.getActiveLoansByMemberId(memberId));
	}

	@GetMapping("/book/{bookId}")
	public ResponseEntity<List<Loan>> getLoansByBookId(@PathVariable UUID bookId) {
		return ResponseEntity.ok(loanService.getLoansByBookId(bookId));
	}

	@GetMapping("/book/{bookId}/active")
	public ResponseEntity<Loan> getActiveLoanByBookId(@PathVariable UUID bookId) {
		return loanService.getActiveLoanByBookId(bookId)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/book/{bookId}/availability")
	public ResponseEntity<Map<String, Boolean>> checkBookAvailability(@PathVariable UUID bookId) {
		boolean isAvailable = loanService.isBookAvailable(bookId);
		return ResponseEntity.ok(Map.of("available", isAvailable));
	}

	@PostMapping
	public ResponseEntity<Loan> createLoan(@RequestBody Map<String, String> loanRequest) {
		try {
			UUID memberId = UUID.fromString(loanRequest.get("memberId"));
			UUID bookId = UUID.fromString(loanRequest.get("bookId"));

			Loan createdLoan = loanService.createLoan(memberId, bookId);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@PutMapping("/{id}/return")
	public ResponseEntity<Loan> returnLoan(@PathVariable UUID id) {
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
	public ResponseEntity<Void> deleteLoan(@PathVariable UUID id) {
		loanService.deleteLoan(id);
		return ResponseEntity.noContent().build();
	}
}