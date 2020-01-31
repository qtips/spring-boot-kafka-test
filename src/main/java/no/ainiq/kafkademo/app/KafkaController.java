package no.ainiq.kafkademo.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {

    private final Producer producer;
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    KafkaController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        LOGGER.info("request="+message);
        this.producer.sendMessage(message);
    }
}