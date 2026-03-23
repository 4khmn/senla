package bank.consumer.service;

import bank.common.TransferMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferConsumer {

    private final TransferProcessingService transferProcessingService;


    @KafkaListener(topics = "transfer", containerFactory = "kafkaListenerContainerFactory")
    public void handleBatch(List<TransferMessage> messages,
                            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        log.info("[CONSUMER] Fetching batch. Size: {}, partition №{}---", messages.size(), partition); // проверяем, из какой партиции читается сообщение

        for (TransferMessage msg : messages) {
            try {
                transferProcessingService.processSingleTransfer(msg);
            } catch (Exception e) {
                log.error("Критическая ошибка при обработке сообщения {}: {}", msg.getId(), e.getMessage());
            }
        }
    }
}
