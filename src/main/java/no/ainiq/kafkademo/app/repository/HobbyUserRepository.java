package no.ainiq.kafkademo.app.repository;

import no.ainiq.kafkademo.app.HobbyUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HobbyUserRepository extends CrudRepository<HobbyUser, Long> {
    HobbyUser findByName(String name);

    @Query(value = "select * from hobbyuser where name=?1 and status = ?2 for update", nativeQuery = true)
    HobbyUser findByNameAndStatus(String name, String status);

    @Query(value = "select * from hobbyuser where status = 'READY' for update skip locked limit 1", nativeQuery = true)
    HobbyUser findReady();

    List<HobbyUser> findByStatus(HobbyUser.NotificationStatus status);
}
