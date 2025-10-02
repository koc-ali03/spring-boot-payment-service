package cc.aliko.payment_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceId;
    private String cardHolder;
    private String cardMasked;
    private String currency;
    private String amount;
    private String status;
    private String responseMessage;

    private LocalDateTime createdAt = LocalDateTime.now();
}
