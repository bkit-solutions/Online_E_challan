package com.trafficchallan.service;


import java.util.List;

import com.trafficchallan.entity.Vehiclechallan;

public interface VehiclechallanService {

    Vehiclechallan saveChallan(Vehiclechallan challan);
    List<Vehiclechallan> getAllChallans();
    List<Vehiclechallan> getChallansByVehicleNumber(String vehicleNumber);
    
    Vehiclechallan findById(Long id);
    void saveChallanAmounnt(Vehiclechallan challan);
}