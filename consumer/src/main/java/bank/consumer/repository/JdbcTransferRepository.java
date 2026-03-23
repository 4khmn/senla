package bank.consumer.repository;


import bank.common.TransferMessage;
import bank.common.TransferStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class JdbcTransferRepository implements TransferRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void save(TransferMessage msg, TransferStatus status) {
        String sql = "INSERT INTO transfers (id,from_account_id, to_account_id, amount, status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                msg.getId(),
                msg.getFromAccountId(),
                msg.getToAccountId(),
                msg.getAmount(),
                status.name()
        );
    }

}
