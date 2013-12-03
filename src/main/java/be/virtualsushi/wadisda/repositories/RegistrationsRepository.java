package be.virtualsushi.wadisda.repositories;

import org.springframework.stereotype.Repository;

import be.virtualsushi.wadisda.entities.Registration;

@Repository(value = "registrations")
public interface RegistrationsRepository extends BaseEntityRepository<Registration> {

}
