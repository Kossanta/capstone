package org.coursera.capstone.symptom.repository;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * An interface for a repository that can store PatientDetail objects and allow them to
 * be searched by username, id doctor, registration id, name and surname.
 * 
 * 
 */
@Repository
public interface PatientDetailRepository extends
		CrudRepository<PatientDetail, Long> {

	// Find a PatientDetail object with a matching username (e.g.,
	// PatientDetail.username)
	public PatientDetail findByUsername(String username);

	// Find all PatientDetail objects with a matching id doctor (e.g.,
	// PatientDetail.idDoctor)
	public ArrayList<PatientDetail> findByIdDoctor(long idDoctor);

	// Find all PatientDetail objects with a name contain the given string
	// (e.g., PatientDetail.name)
	public ArrayList<PatientDetail> findByNameContaining(String str);

	// Find all PatientDetail objects with a surname contain the given string
	// (e.g., PatientDetail.surname)
	public ArrayList<PatientDetail> findBySurnameContaining(String str);

	// Find a PatientDetail object with a matching registration id (e.g.,
	// PatientDetail.regId)
	public PatientDetail findByRegId(String regId);
}
