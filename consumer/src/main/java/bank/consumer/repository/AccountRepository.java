package bank.consumer.repository;

import bank.common.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById (Long id);


    void updateBalance(Long id, BigDecimal newBalance);
}
