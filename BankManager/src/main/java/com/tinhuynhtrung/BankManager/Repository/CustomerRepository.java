package com.tinhuynhtrung.BankManager.Repository;

import com.tinhuynhtrung.BankManager.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL")
    List<Customer> findAllActive();

    @Query("SELECT c FROM Customer c WHERE c.id = :id AND c.deletedAt IS NULL")
    Optional<Customer> findActiveById(Long id);

    @Modifying
    @Query("UPDATE Customer c SET c.deletedAt = :deletedAt WHERE c.id = :id")
    void softDelete(Long id, LocalDateTime deletedAt);
}
