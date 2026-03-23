package bank.consumer.service;

import bank.common.Account;
import bank.common.TransferMessage;
import bank.common.TransferStatus;
import bank.consumer.exception.ValidationException;
import bank.consumer.repository.AccountRepository;
import bank.consumer.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferProcessingService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public void processSingleTransfer(TransferMessage msg) {
        log.info("Начало обработки перевода: {}", msg.getId());
        try {
            executeInTransaction(msg);
            log.info("Перевод {} успешно завершен", msg.getId());
        } catch (ValidationException e) {
            log.error("Ошибка валидации для {}: {}", msg.getId(), e.getMessage());
        } catch (Exception e) {
            log.error("Транзакция упала для {}: {}. Сохраняем статус FAILED", msg.getId(), e.getMessage());
            transferRepository.save(msg, TransferStatus.FAILED);
        }
    }

    @Transactional
    public void executeInTransaction(TransferMessage msg) {
        Account from = accountRepository.findById(msg.getFromAccountId())
                .orElseThrow(() -> new ValidationException("Счет отправителя не найден"));
        Account to = accountRepository.findById(msg.getToAccountId())
                .orElseThrow(() -> new ValidationException("Счет получателя не найден"));

        if (from.getBalance().compareTo(msg.getAmount()) < 0) {
            throw new ValidationException("Недостаточно средств");
        }

        accountRepository.updateBalance(from.getId(), from.getBalance().subtract(msg.getAmount()));
        accountRepository.updateBalance(to.getId(), to.getBalance().add(msg.getAmount()));

        transferRepository.save(msg, TransferStatus.SUCCESS);
    }
}
