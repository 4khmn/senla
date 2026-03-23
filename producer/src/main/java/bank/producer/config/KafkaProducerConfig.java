package bank.producer.config;

import bank.common.TransferMessage;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, TransferMessage> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        String bootstrapServers = System.getenv("SPRING_KAFKA_BOOTSTRAP_SERVERS");
        if (bootstrapServers == null || bootstrapServers.isEmpty()) {
            bootstrapServers = "localhost:9092";
        }

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "bank-tx-" + java.util.UUID.randomUUID().toString());
        DefaultKafkaProducerFactory<String, TransferMessage> factory = new DefaultKafkaProducerFactory<>(configProps);
        factory.setTransactionIdPrefix("bank-tx-");
        return factory;
    }

    @Bean
    public KafkaTemplate<String, TransferMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    @Bean
    public KafkaTransactionManager<String, TransferMessage> kafkaTransactionManager() {
        return new KafkaTransactionManager<>(producerFactory());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("SPRING_KAFKA_BOOTSTRAP_SERVERS"));
        return new KafkaAdmin(configs);
    }


    @Bean
    public NewTopic transferTopic() {
        return TopicBuilder.name("transfer")
                .partitions(3)
                .replicas(3)
                .configs(Map.of(
                        "min.insync.replicas", "2", // подтверждение доставки на 2 брокера из 3 при отправке сообщений
                        "retention.ms", "300000"
                ))
                .build();
    }

}
