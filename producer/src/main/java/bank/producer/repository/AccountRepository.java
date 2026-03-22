package bank.producer.repository;

import bank.common.Account;

import java.util.List;

public interface AccountRepository {
    long count();
    void saveAll(List<Account> accounts);
    List<Account> findAll();
}
