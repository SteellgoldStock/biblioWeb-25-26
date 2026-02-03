package fr.serfa.biblioWeb.controller;

import fr.serfa.biblioWeb.model.Loan;
import fr.serfa.biblioWeb.model.Member;
import fr.serfa.biblioWeb.service.LoanService;
import fr.serfa.biblioWeb.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;
	private final LoanService loanService;

	public MemberController(MemberService memberService, LoanService loanService) {
		this.memberService = memberService;
		this.loanService = loanService;
	}

	@GetMapping
	public ResponseEntity<List<Member>> getAllMembers() {
		return ResponseEntity.ok(memberService.getAllMembers());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Member> getMemberById(@PathVariable UUID id) {
		return memberService.getMemberById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<Member> getMemberByEmail(@PathVariable String email) {
		return memberService.getMemberByEmail(email)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/search")
	public ResponseEntity<List<Member>> searchMembersByLastName(@RequestParam String lastName) {
		return ResponseEntity.ok(memberService.searchMembersByLastName(lastName));
	}

	@GetMapping("/{id}/loans")
	public ResponseEntity<List<Loan>> getMemberLoans(@PathVariable UUID id) {
		return ResponseEntity.ok(loanService.getLoansByMemberId(id));
	}

	@GetMapping("/{id}/loans/active")
	public ResponseEntity<List<Loan>> getMemberActiveLoans(@PathVariable UUID id) {
		return ResponseEntity.ok(loanService.getActiveLoansByMemberId(id));
	}

	@PostMapping
	public ResponseEntity<Member> createMember(@RequestBody Member member) {
		if (memberService.existsByEmail(member.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}

		Member createdMember = memberService.createMember(member);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMember(@PathVariable UUID id) {
		memberService.deleteMemberById(id);
		return ResponseEntity.noContent().build();
	}
}