package no.ainiq.kafkademo.app.repository;

import no.ainiq.kafkademo.app.Users;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Long> {
    Users findByName(String name);
}
