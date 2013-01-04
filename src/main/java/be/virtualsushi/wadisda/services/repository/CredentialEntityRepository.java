package be.virtualsushi.wadisda.services.repository;

import be.virtualsushi.wadisda.entities.CredentialEntity;

public interface CredentialEntityRepository {

	CredentialEntity findByUserId(String userId);

}
