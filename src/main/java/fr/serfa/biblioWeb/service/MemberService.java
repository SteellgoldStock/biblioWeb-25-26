package fr.serfa.biblioWeb.service;

import fr.serfa.biblioWeb.model.Member;
import fr.serfa.biblioWeb.repositories.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public List<Member> getAllMembers() {
		return memberRepository.findAll();
	}

	public Optional<Member> getMemberById(UUID id) {
		return memberRepository.findById(id);
	}

	public Optional<Member> getMemberByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	public List<Member> searchMembersByLastName(String lastName) {
		return memberRepository.findByLastName(lastName);
	}

	public Member createMember(Member member) {
		return memberRepository.addNew(member);
	}

	public void deleteMemberById(UUID id) {
		memberRepository.deleteById(id);
	}

	public boolean existsByEmail(String email) {
		return memberRepository.existsByEmail(email);
	}
}