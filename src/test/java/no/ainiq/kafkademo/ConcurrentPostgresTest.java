package no.ainiq.kafkademo;

import com.github.javafaker.Faker;
import no.ainiq.kafkademo.app.HobbyNotificationService;
import no.ainiq.kafkademo.app.HobbyUser;
import no.ainiq.kafkademo.app.repository.HobbyUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public class ConcurrentPostgresTest {

    @Container
    private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
            .withDatabaseName("my-db")
            .withUsername("foo")
            .withPassword("secret");


    @Autowired
    HobbyUserRepository repository;

    @Autowired
    HobbyNotificationService service;

    @Test()
    @DisplayName("4 threads try to notify each user simultaneously")
    void test_for_update() throws InterruptedException {
        List<String> names = names(10);
        names.forEach(n -> repository.save(HobbyUser.newInstance(n)));
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<Callable<Object>> callables = new ArrayList<>();
        callables.add(Executors.callable(() -> names.forEach(service::notifyUser)));
        callables.add(Executors.callable(() -> names.forEach(service::notifyUser)));
        callables.add(Executors.callable(() -> names.forEach(service::notifyUser)));
        callables.add(Executors.callable(() -> names.forEach(service::notifyUser)));

        executorService.invokeAll(callables);

        System.out.println("RESULTS:");
        repository.findAll().forEach(System.out::println);

        assertThat(repository.findByStatus(HobbyUser.NotificationStatus.READY)).isEmpty();
    }

    @Test
    @DisplayName("4 threads notify 1 ready user at a time - skipping locked users by other threads")
    void test_skip_locked() throws InterruptedException {
        List<String> names = names(10);
        names.forEach(n -> repository.save(HobbyUser.newInstance(n)));
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<Callable<Object>> callables = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            callables.add(Executors.callable(() -> service.notifyNextUser()));
        }
        executorService.invokeAll(callables);

        System.out.println("RESULTS:");
        repository.findAll().forEach(System.out::println);

        assertThat(repository.findByStatus(HobbyUser.NotificationStatus.READY)).isEmpty();

    }

    private List<String> names(int no) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < no; i++) {
            names.add(Faker.instance().superhero().name());

        }
        return names;
    }
}
