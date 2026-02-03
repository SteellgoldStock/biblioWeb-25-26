package fr.serfa.biblioWeb.repositories;

import fr.serfa.biblioWeb.model.Member;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MemberRepository {

	private final List<Member> members = new ArrayList<>();

	public MemberRepository() {
	}

	public List<Member> findAll() {
		return new ArrayList<>(members);
	}

	public Optional<Member> findById(UUID id) {
		return members.stream()
				.filter(member -> member.getId().equals(id))
				.findFirst();
	}

	public Optional<Member> findByEmail(String email) {
		return members.stream()
				.filter(member -> member.getEmail().equalsIgnoreCase(email))
				.findFirst();
	}

	public List<Member> findByLastName(String lastName) {
		return members.stream()
				.filter(member -> member.getLastName().toLowerCase().contains(lastName.toLowerCase()))
				.collect(Collectors.toList());
	}

	public boolean existsByEmail(String email) {
		return members.stream()
				.anyMatch(member -> member.getEmail().equalsIgnoreCase(email));
	}

	public Member addNew(Member member) {
		members.add(member);
		return member;
	}

	public void deleteById(UUID id) {
		members.removeIf(member -> member.getId().equals(id));
	}
}