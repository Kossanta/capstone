package org.coursera.capstone.symptom.repository;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * An interface for a repository that can store CheckIn objects and allow them to
 * be searched by IdPatient
 * 
 */
@Repository
public interface CheckInRepository extends CrudRepository<CheckIn, Long> {

	// Find all CheckIns with a matching id patient (e.g., CheckIn.idPatient)
	public ArrayList<CheckIn> findByIdPatient(long id);

}
