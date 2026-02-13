package fr.serfa.biblioWeb.repositories;

import fr.serfa.biblioWeb.enums.LoanStatus;
import fr.serfa.biblioWeb.model.Book;
import fr.serfa.biblioWeb.model.Loan;
import fr.serfa.biblioWeb.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {

	List<Loan> findByMember(Member member);

	List<Loan> findByMemberId(UUID memberId);

	@Query("SELECT l FROM Loan l WHERE l.member.id = :memberId AND (l.status = 'ACTIVE' OR l.status = 'OVERDUE')")
	List<Loan> findActiveLoansByMemberId(@Param("memberId") UUID memberId);

	List<Loan> findByBook(Book book);

	List<Loan> findByBookId(UUID bookId);

	@Query("SELECT l FROM Loan l WHERE l.book.id = :bookId AND (l.status = 'ACTIVE' OR l.status = 'OVERDUE')")
	Optional<Loan> findActiveLoanByBookId(@Param("bookId") UUID bookId);

	List<Loan> findByStatus(LoanStatus status);

	@Query("SELECT l FROM Loan l WHERE l.status <> 'RETURNED' AND l.expectedReturnDate < CURRENT_DATE")
	List<Loan> findOverdueLoans();

	@Query("SELECT CASE WHEN COUNT(l) = 0 THEN true ELSE false END FROM Loan l WHERE l.book.id = :bookId AND (l.status = 'ACTIVE' OR l.status = 'OVERDUE')")
	boolean isBookAvailable(@Param("bookId") UUID bookId);
}
