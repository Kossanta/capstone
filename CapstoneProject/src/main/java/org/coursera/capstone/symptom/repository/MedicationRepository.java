package org.coursera.capstone.symptom.repository;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * An interface for a repository that can store Medication objects and allow them to
 * be searched by id, name and id patient.
 * 
 */
@Repository
public interface MedicationRepository extends CrudRepository<Medication, Long> {

	// Find a Medication object with a matching id (e.g., Medication.id)
	public Medication findById(long id);
	// Find a Medication object with a matching name (e.g., Medication.name)
	public Medication findByName(String name);
	// Find all Medication objects with a matching id patient (e.g.,
	// Medication.idPatient)
	public ArrayList<Medication> findByIdPatient(long idPatient);
}
