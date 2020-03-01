package no.ainiq.kafkademo;

import com.github.javafaker.Faker;
import no.ainiq.kafkademo.app.HobbyNotificationService;
import no.ainiq.kafkademo.app.HobbyUser;
import no.ainiq.kafkademo.app.repository.HobbyUserRepository;
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
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

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

    @Test
    void notification() throws InterruptedException {
        List<String> names = names(10);
        names.forEach(n -> repository.save(HobbyUser.newInstance(n)));
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<Callable<Object>> callables = new ArrayList<>();
        callables.add(Executors.callable(() -> names.forEach(service::notify)));
        callables.add(Executors.callable(() -> names.forEach(service::notify)));
        callables.add(Executors.callable(() -> names.forEach(service::notify)));
        callables.add(Executors.callable(() -> names.forEach(service::notify)));

        executorService.invokeAll(callables);
        
        System.out.println("STARTED!!!");
        await().atMost(10, TimeUnit.SECONDS)
                .pollInterval(3, TimeUnit.SECONDS)
                .until(() -> {
                    HobbyUser lastUser = repository.findByName(names.get(names.size() - 1));
                    return lastUser.getTries() == 3 || lastUser.getStatus() == HobbyUser.NotificationStatus.SENT;
                });

    }

    private List<String> names(int no) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < no; i++) {
            names.add(Faker.instance().superhero().name());

        }
        return names;
    }
}
