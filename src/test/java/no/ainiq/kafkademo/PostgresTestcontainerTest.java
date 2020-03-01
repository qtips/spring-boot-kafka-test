package no.ainiq.kafkademo;

import no.ainiq.kafkademo.app.HobbyUser;
import no.ainiq.kafkademo.app.repository.HobbyUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(value = {
        "kafka-enabled=false",
        "spring.datasource.url=jdbc:tc:postgresql:///my-db",
        "spring.datasource.username=foo",
        "spring.datasource.password=secret",
        "spring.datasource.driverClassName=org.testcontainers.jdbc.ContainerDatabaseDriver",
        "spring.jpa.show-sql=false",
})
@Testcontainers
public class PostgresTestcontainerTest {

    @Container
    private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
            .withDatabaseName("my-db")
            .withUsername("foo")
            .withPassword("secret");


    @Autowired
    HobbyUserRepository repository;

                                           
    @Test
    void test() {
        repository.save(HobbyUser.newInstance("Ole", 10));
        assertThat(repository.findByName("Ole").getHobbies()).isEqualTo(10);
    }


}
