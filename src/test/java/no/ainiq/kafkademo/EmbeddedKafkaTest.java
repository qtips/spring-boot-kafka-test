package no.ainiq.kafkademo;

import no.ainiq.kafkademo.app.repository.HobbyUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest(value = {"kafka-enabled=true",
        "spring.datasource.url=jdbc:tc:postgresql:///my-db",
        "spring.datasource.username=foo",
        "spring.datasource.password=secret",
        "spring.datasource.driverClassName=org.testcontainers.jdbc.ContainerDatabaseDriver",
        "spring.jpa.show-sql=true",
})
@Testcontainers
@AutoConfigureMockMvc
@EmbeddedKafka(bootstrapServersProperty = "spring.kafka.bootstrap-servers", //setter oppgitt property til embedded kafka url
        brokerProperties = "log.dir=tmp/embedded-kafka",
        partitions = 1)
class EmbeddedKafkaTest {

    @Container
    private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
            .withDatabaseName("my-db")
            .withUsername("foo")
            .withPassword("secret");
    @Autowired
    MockMvc mvc;

    @Autowired
    HobbyUserRepository repository;

    @Test
    void fromRestToKafkaToDB() throws Exception {
        mvc.perform(get("/kafka/publish?message=hei"))
                .andDo(print());
        await().atMost(10, TimeUnit.SECONDS)
                .pollInterval(3, TimeUnit.SECONDS)
                .until(() -> repository.findByName("hei") != null);
        assertThat(repository.findByName("hei").getHobbies()).isEqualTo(20);
    }
}
