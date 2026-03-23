package bank.producer.service;

import bank.common.Account;
import bank.producer.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service
@DependsOn("dataSourceInitializer")
@RequiredArgsConstructor
public class AccountInitializer {
    private final AccountRepository accountRepository;
    private final Map<Long, Account> accountMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        long count = accountRepository.count();
        if (count == 0) {
            System.out.println("Generating 1000 accounts...");
            List<Account> accounts = new ArrayList<>();
            for (long i = 1; i <= 1000; i++) {
                Account acc = new Account();
                acc.setId(i);
                acc.setBalance(new BigDecimal("10000.00"));
                accounts.add(acc);
            }
            accountRepository.saveAll(accounts);
        }

        List<Account> allAccounts = accountRepository.findAll();
        for (Account acc : allAccounts) {
            accountMap.put(acc.getId(), acc);
        }
    }

    public Map<Long, Account> getAccountMap() {
        return accountMap;
    }
}
