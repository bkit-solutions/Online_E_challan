package com.trafficchallan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trafficchallan.entity.Vehiclechallan;
import com.trafficchallan.repository.VehiclechallanRepository;

@Service
public class VehiclechallanServiceImpl implements VehiclechallanService {

    @Autowired
    private VehiclechallanRepository vehiclechallanRepository;

    
    
    @Override
    public Vehiclechallan saveChallan(Vehiclechallan challan) {
        return vehiclechallanRepository.save(challan);
    }

    @Override
    public List<Vehiclechallan> getAllChallans() {
        return vehiclechallanRepository.findAll();
    }
    
    public List<Vehiclechallan> findAll() {
        return vehiclechallanRepository.findAll();
    }
    
 
    @Override
    public List<Vehiclechallan> getChallansByVehicleNumber(String vehicleNumber) {
        return vehiclechallanRepository.findByVehicleNumber(vehicleNumber);
    }
    
    
    @Override
    public Vehiclechallan findById(Long id) {
        return vehiclechallanRepository.findById(id).orElse(null);
    }

    @Override
    public void saveChallanAmounnt(Vehiclechallan challan) {
        vehiclechallanRepository.save(challan);
    }
    
}