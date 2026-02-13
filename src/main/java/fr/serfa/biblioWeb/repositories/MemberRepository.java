package fr.serfa.biblioWeb.repositories;

import fr.serfa.biblioWeb.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

	Optional<Member> findByEmailIgnoreCase(String email);

	List<Member> findByLastNameContainingIgnoreCase(String lastName);

	boolean existsByEmailIgnoreCase(String email);
}
