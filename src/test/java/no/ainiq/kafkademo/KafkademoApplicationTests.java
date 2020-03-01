package no.ainiq.kafkademo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest("kafka-enabled=false")
@AutoConfigureTestDatabase
class KafkademoApplicationTests {

    @Test
    void contextLoads() {
    }

}
