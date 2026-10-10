package com.blooddonor.backend.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="blood_requests")
public class BloodRequest {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 public String requesterName;
 public String phone;
 public String bloodGroup;
 public String city;
 public Integer units=1;
 public String hospital;
 @Column(length=2000) public String notes;
 public String status="OPEN";
 public LocalDateTime createdAt=LocalDateTime.now();
 public BloodRequest(){}
}