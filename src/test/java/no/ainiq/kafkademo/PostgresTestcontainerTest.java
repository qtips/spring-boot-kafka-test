package no.ainiq.kafkademo;

import no.ainiq.kafkademo.app.Users;
import no.ainiq.kafkademo.app.repository.UsersRepository;
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
        "spring.jpa.show-sql=true",
        "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect"
        //"spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect"
})
@Testcontainers
public class PostgresTestcontainerTest {

    @Container
    private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
            .withDatabaseName("my-db")
            .withUsername("foo")
            .withPassword("secret");


    @Autowired
    UsersRepository repository;

    @Test
    void test() throws Exception {
        repository.save(Users.newInstance("Ole", 10));
        assertThat(repository.findByName("Ole").getAge()).isEqualTo(10);
    }
}
