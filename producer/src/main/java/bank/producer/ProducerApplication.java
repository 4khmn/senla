package bank.producer;

import bank.producer.config.DatabaseConfig;
import bank.producer.config.KafkaProducerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("bank.producer")
public class ProducerApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(DatabaseConfig.class,
                        KafkaProducerConfig.class);

        System.out.println("Producer is running...");
    }
}
