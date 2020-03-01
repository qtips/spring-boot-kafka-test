package no.ainiq.kafkademo.app;

import no.ainiq.kafkademo.app.repository.HobbyUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;

@Service
public class HobbyNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HobbyNotificationService.class);

    private HobbyUserRepository repository;

    public HobbyNotificationService(HobbyUserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void notify(String name) {
        HobbyUser user = repository.findByNameAndStatus(name, HobbyUser.NotificationStatus.READY.name());
        if (user == null) {
            LOGGER.info("User with name {} already sent or failed", name);
            return;
        }
        try {
            sendNotification(user);
            user.success();
        } catch (Exception e) {
            user.failed();
        }
        repository.save(user);
    }

    private void sendNotification(HobbyUser u) {
        LOGGER.info("Sending...." + u.getName());
        sleep(new Random().nextInt(150) + 50); // between 50 - 150 ms
        if (new Random().nextInt(100) < 5) {   // success 5% chance
            LOGGER.info("SUCCESS: " + u.getName() + " tries:" + u.getTries());
            return;
        }

        LOGGER.info("FAILED: " + u.getName() + " tries:" + u.getTries());

        throw new NotificationException("send failed");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class NotificationException extends RuntimeException {
        public NotificationException(String send_failed) {
            super(send_failed);
        }
    }
}
