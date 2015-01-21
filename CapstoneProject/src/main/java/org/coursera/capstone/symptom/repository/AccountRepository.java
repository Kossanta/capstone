package org.coursera.capstone.symptom.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * TODO
 * An interface for a repository that can store Account objects and allow them to
 * be searched by username.
 * 
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

	// Find the account with a matching username (e.g., Account.username)
	public Account findByUsername(String title);
}
