package com.blooddonor.backend.model;
import jakarta.persistence.*;
@Entity @Table(name="users")
public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 public String name;
 @Column(unique=true,nullable=false) public String email;
 @Column(nullable=false) public String password;
 public User(){}
}