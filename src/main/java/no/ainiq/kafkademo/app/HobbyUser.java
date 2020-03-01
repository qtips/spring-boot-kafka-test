package no.ainiq.kafkademo.app;

import javax.persistence.*;

@Entity
@Table(name = "hobbyuser")
public class HobbyUser {

    private static final int MAX_TRIES = 3;
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int hobbies;
    @Enumerated(value = EnumType.STRING)
    private NotificationStatus status = NotificationStatus.READY;
    private int tries = 0;

    public static HobbyUser newInstance(String message, int hobbies) {
        HobbyUser user = new HobbyUser();
        user.name = message;
        user.hobbies = hobbies;
        return user;
    }

    public static HobbyUser newInstance(String name) {
        return newInstance(name, 1);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHobbies() {
        return hobbies;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public int getTries() {
        return tries;
    }

    public void success() {
        this.status = NotificationStatus.SENT;
    }

    public void failed() {
        this.tries++;
        if (tries == MAX_TRIES) {
            this.status = NotificationStatus.FAILED;
        }

    }

    public enum NotificationStatus {
        READY, SENT, FAILED
    }
}