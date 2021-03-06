package no.ainiq.kafkademo;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("kafka-enabled")
public class KafkaTopicConfig {

    @Bean
    public NewTopic topic1() {
        return new NewTopic("users", 1, (short) 1);
    }
}