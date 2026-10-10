package com.blooddonor.backend.controller;
import com.blooddonor.backend.model.DonorProfile;
import com.blooddonor.backend.repository.DonorProfileRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/donors")
public class DonorController {
 private final DonorProfileRepository repo;
 private static final List<String> GROUPS=List.of("A+","A-","B+","B-","AB+","AB-","O+","O-");
 public DonorController(DonorProfileRepository repo){this.repo=repo;}
 @GetMapping public List<DonorProfile> list(@RequestParam(required=false)String bloodGroup,@RequestParam(required=false)String city){
  boolean b=bloodGroup!=null&&!bloodGroup.isBlank(),c=city!=null&&!city.isBlank();
  if(b&&c)return repo.findByBloodGroupIgnoreCaseAndCityContainingIgnoreCaseAndAvailableTrueOrderByCreatedAtDesc(bloodGroup.trim(),city.trim());
  if(b)return repo.findByBloodGroupIgnoreCaseAndAvailableTrueOrderByCreatedAtDesc(bloodGroup.trim());
  if(c)return repo.findByCityContainingIgnoreCaseAndAvailableTrueOrderByCreatedAtDesc(city.trim());
  return repo.findByAvailableTrueOrderByCreatedAtDesc();
 }
 @PostMapping public ResponseEntity<?> create(@RequestBody Map<String,Object>b){
  String name=s(b,"name"),phone=s(b,"phone"),group=s(b,"bloodGroup").toUpperCase(),city=s(b,"city");
  if(name.isBlank()||phone.isBlank()||group.isBlank()||city.isBlank())return ResponseEntity.badRequest().body(Map.of("message","Please fill name, phone, blood group and city."));
  if(!GROUPS.contains(group))return ResponseEntity.badRequest().body(Map.of("message","Select a valid blood group."));
  DonorProfile d=new DonorProfile();d.name=name;d.phone=phone;d.bloodGroup=group;d.city=city;d.available=true;
  String uid=s(b,"userId");if(!uid.isBlank())try{d.userId=Long.parseLong(uid);}catch(Exception e){return ResponseEntity.badRequest().body(Map.of("message","Invalid user ID."));}
  return ResponseEntity.status(201).body(repo.save(d));
 }
 @PatchMapping("/{id}/availability") public ResponseEntity<?> availability(@PathVariable Long id,@RequestParam boolean available){
  DonorProfile d=repo.findById(id).orElse(null);if(d==null)return ResponseEntity.status(404).body(Map.of("message","Donor not found."));
  d.available=available;return ResponseEntity.ok(repo.save(d));
 }
 @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable Long id){
  if(!repo.existsById(id))return ResponseEntity.status(404).body(Map.of("message","Donor not found."));
  repo.deleteById(id);return ResponseEntity.ok(Map.of("message","Donor deleted."));
 }
 private String s(Map<String,Object>b,String k){Object v=b.get(k);return v==null?"":v.toString().trim();}
}