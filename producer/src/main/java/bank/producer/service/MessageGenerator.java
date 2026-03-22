package bank.producer.service;

import bank.common.TransferMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageGenerator {

    private final AccountInitializer accountInitializer;
    private final KafkaTemplate<String, TransferMessage> kafkaTemplate;
    private final Random random = new Random();


    @Scheduled(fixedDelay = 200)
    public void sendMessage() {
        var accountMap = accountInitializer.getAccountMap();
        if (accountMap.isEmpty()) {
            return;
        }

        List<Long> ids = new ArrayList<>(accountMap.keySet());

        Long fromId = ids.get(random.nextInt(ids.size()));
        Long toId = ids.get(random.nextInt(ids.size()));
        while(fromId.equals(toId)){
            fromId = ids.get(random.nextInt(ids.size()));
        }

        BigDecimal amount = new BigDecimal(random.nextInt(500) + 1);

        TransferMessage message = new TransferMessage();
        message.setFromAccountId(fromId);
        message.setToAccountId(toId);
        message.setAmount(amount);
        message.setId(UUID.randomUUID());

        log.info("[PRODUCER] Sending message: {} | From: {} To: {} Amount: {}",
                message.getId(), fromId, toId, amount);
        kafkaTemplate.send("transfer", null, message);
    }
}
