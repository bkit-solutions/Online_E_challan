package com.trafficchallan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trafficchallan.entity.Vehiclechallan;

public interface VehiclechallanRepository extends JpaRepository<Vehiclechallan, Long> {
	Optional<Vehiclechallan> findTopByVehicleNumberOrderByIdDesc(String vehicleNumber);
	List<Vehiclechallan> findByVehicleNumber(String vehicleNumber);
	
}
