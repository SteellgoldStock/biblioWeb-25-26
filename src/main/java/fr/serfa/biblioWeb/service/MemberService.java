package fr.serfa.biblioWeb.service;

import fr.serfa.biblioWeb.model.Member;
import fr.serfa.biblioWeb.repositories.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public List<Member> getAllMembers() {
		return memberRepository.findAll();
	}

	public Optional<Member> getMemberById(String id) {
		return memberRepository.findById(id);
	}

	public Optional<Member> getMemberByEmail(String email) {
		return memberRepository.findByEmailIgnoreCase(email);
	}

	public List<Member> searchMembersByLastName(String lastName) {
		return memberRepository.findByLastNameContainingIgnoreCase(lastName);
	}

	public Member createMember(Member member) {
		return memberRepository.save(member);
	}

	public void deleteMemberById(String id) {
		memberRepository.deleteById(id);
	}

	public boolean existsByEmail(String email) {
		return memberRepository.existsByEmailIgnoreCase(email);
	}
}
