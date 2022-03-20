package edu.idol.mca.bcs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.idol.mca.bcs.domain.Address;

/**
 *This AddressRepository will be responsible for performing all the CRUD operations on User
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
