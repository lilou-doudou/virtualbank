package com.virtuallink.virtualbank.models;
import com.virtuallink.virtualbank.enums.OperationType;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
@Entity
@Table(name = "operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    private double amount;
    private String description;
    private OffsetDateTime operationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;
}
