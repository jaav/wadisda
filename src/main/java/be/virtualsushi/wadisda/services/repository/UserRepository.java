package be.virtualsushi.wadisda.services.repository;

import be.virtualsushi.wadisda.entities.User;

public interface UserRepository extends BaseJpaRepository<User> {

	User findByEmail(String email);

}
