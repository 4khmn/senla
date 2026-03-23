package bank.consumer.repository;

import bank.common.Account;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class JdbcAccountRepository implements AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Account> findById(Long id) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
                    Account acc = new Account();
                    acc.setId(rs.getLong("id"));
                    acc.setBalance(rs.getBigDecimal("balance"));
                    return acc;
                }, id)
                .stream()
                .findFirst();
    }

    @Override
    public void updateBalance(Long id, BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, newBalance, id);

        if (rowsAffected == 0) {
            throw new RuntimeException("Не удалось обновить баланс: счет с id " + id + " не найден");
        }
    }

}
