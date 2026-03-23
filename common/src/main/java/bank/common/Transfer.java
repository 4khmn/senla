package bank.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    private Long id;

    @Column(name = "from_account_id") // решил не реализовывать связь @ManyToOne
    private Long fromId;
    @Column(name = "to_account_id")
    private Long toId;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

}
