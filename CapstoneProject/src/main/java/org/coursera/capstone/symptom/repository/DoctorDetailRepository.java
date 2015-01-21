package org.coursera.capstone.symptom.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *  An interface for a repository that can store DoctorDetail objects and allow
 * them to be searched by registration id and username.
 * 
 */
@Repository
public interface DoctorDetailRepository extends
		CrudRepository<DoctorDetail, Long> {
	
	// Find a DoctorDetail with a matching registration id (e.g., DoctorDetail.regId)
	public DoctorDetail findByRegId(String regId);
	// Find a DoctorDetail with a matching username (e.g., DoctorDetail.username)
	public DoctorDetail findByUsername(String username);
}
