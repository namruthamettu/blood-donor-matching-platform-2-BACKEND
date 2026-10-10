package com.blooddonor.backend.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="donor_profiles")
public class DonorProfile {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 public Long userId;
 public String name;
 public String phone;
 public String bloodGroup;
 public String city;
 public boolean available=true;
 public LocalDateTime createdAt=LocalDateTime.now();
 public DonorProfile(){}
}