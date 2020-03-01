package no.ainiq.kafkademo.app;

import no.ainiq.kafkademo.app.repository.HobbyUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty("kafka-enabled")
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    private HobbyUserRepository hobbyUserRepository;

    @Autowired
    public Consumer(HobbyUserRepository hobbyUserRepository) {
        this.hobbyUserRepository = hobbyUserRepository;
    }

    @KafkaListener(topics = "users", groupId = "group_id")
    public void consume(String message) {
        logger.info(String.format("#### -> Consumed message -> %s", message));
        hobbyUserRepository.save(HobbyUser.newInstance(message, 20));
    }
}