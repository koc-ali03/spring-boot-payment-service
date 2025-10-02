package cc.aliko.payment_service.repository;

import cc.aliko.payment_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByInvoiceId(String invoiceId);
}
