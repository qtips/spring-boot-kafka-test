package no.ainiq.kafkademo.app.repository;

import no.ainiq.kafkademo.app.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
