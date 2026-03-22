package bank.common;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferMessage implements Serializable {
    private UUID id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
}
