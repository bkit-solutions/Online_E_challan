package com.trafficchallan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trafficchallan.entity.User;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {
	
	
	 User findByEmail(String email);
	
	 User findByConfirmationToken(String confirmationToken);
	 
	  List<User> findAll();
	  
	  Optional<User> findByVehicleNumber(String vehicleNumber);
}