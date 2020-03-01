package no.ainiq.kafkademo.app.repository;

import no.ainiq.kafkademo.app.HobbyUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface HobbyUserRepository extends CrudRepository<HobbyUser, Long> {
    HobbyUser findByName(String name);

    //@Lock(LockModeType.PESSIMISTIC_READ)
    @Query(value = "select * from hobbyuser where name=?1 and status = ?2 for update", nativeQuery = true)
    HobbyUser findByNameAndStatus(String name, String status);
}
