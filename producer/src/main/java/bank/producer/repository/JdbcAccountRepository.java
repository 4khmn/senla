package bank.producer.repository;

import bank.common.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
@Repository
@RequiredArgsConstructor
public class JdbcAccountRepository implements AccountRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT count(*) FROM accounts", Long.class);
        return count != null ? count : 0;
    }


    @Override
    public void saveAll(List<Account> accounts) {
        String sql = "INSERT INTO accounts (id, balance) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Account account = accounts.get(i);
                ps.setLong(1, account.getId());
                ps.setBigDecimal(2, account.getBalance());
            }

            @Override
            public int getBatchSize() {
                return accounts.size();
            }
        });
    }

    @Override
    public List<Account> findAll() {
        return jdbcTemplate.query("SELECT id, balance FROM accounts", (rs, rowNum) -> {
            Account account = new Account();
            account.setId(rs.getLong("id"));
            account.setBalance(rs.getBigDecimal("balance"));
            return account;
        });
    }
}
