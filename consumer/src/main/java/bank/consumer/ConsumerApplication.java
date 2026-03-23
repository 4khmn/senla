package bank.consumer;

import bank.consumer.config.KafkaConsumerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("bank.consumer")
public class ConsumerApplication {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ConsumerApplication.class);
        System.out.println("Consumer is running...");
        Thread.currentThread().join();
    }
}