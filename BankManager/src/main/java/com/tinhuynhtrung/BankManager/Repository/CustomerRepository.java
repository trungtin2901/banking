package com.tinhuynhtrung.BankManager.Repository;

import com.tinhuynhtrung.BankManager.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL")
    List<Customer> findAllActive();

    @Query("SELECT c FROM Customer c WHERE c.id = :id AND c.deletedAt IS NULL")
    Optional<Customer> findActiveById(Long id);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(concat('%', :keyword, '%')) AND c.deletedAt IS NULL")
    List<Customer> searchByName(@Param("keyword") String keyword);

    @Modifying
    @Query(nativeQuery = true, value = """
        UPDATE customers c, accounts a
        SET c.deleted_at = :deletedAt,
            a.deleted_at = :deletedAt
        WHERE c.id = :id AND a.customer_id = c.id
            AND c.deleted_at IS NULL
            AND a.deleted_at IS NULL
        """)
    void softDelete(Long id, LocalDateTime deletedAt);
}
