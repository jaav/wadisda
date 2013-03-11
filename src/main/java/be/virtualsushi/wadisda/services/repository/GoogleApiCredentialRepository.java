package be.virtualsushi.wadisda.services.repository;

import org.apache.tapestry5.jpa.annotations.CommitAfter;

import be.virtualsushi.wadisda.entities.GoogleApiCredential;

public interface GoogleApiCredentialRepository extends BaseJpaRepository<GoogleApiCredential> {

	public GoogleApiCredential findByGoogleUserId(String googleUserId);

	@CommitAfter
	public void deleteByGoogleUserId(String googleUserId);

}
