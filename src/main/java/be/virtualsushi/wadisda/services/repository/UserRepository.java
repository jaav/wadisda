package be.virtualsushi.wadisda.services.repository;

import java.util.List;

import be.virtualsushi.wadisda.entities.User;

public interface UserRepository extends BaseJpaRepository<User> {

	User findByEmail(String email);

	List<User> listWithoutCurrent(User current, int offset, int count);

}
