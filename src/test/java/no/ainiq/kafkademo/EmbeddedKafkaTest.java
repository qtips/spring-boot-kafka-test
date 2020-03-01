package no.ainiq.kafkademo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest("kafka-enabled=true")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@EmbeddedKafka(bootstrapServersProperty = "spring.kafka.bootstrap-servers", //setter oppgitt property til embedded kafka url
        topics = "my-test-topic",
        brokerProperties = "log.dir=tmp/embedded-kafka",
        partitions = 1)
public class EmbeddedKafkaTest {

    @Autowired
    MockMvc mvc;

    @Test
    void test() throws Exception {
        mvc.perform(get("/kafka/publish?message=hei"))
                .andDo(print());

    }
}
