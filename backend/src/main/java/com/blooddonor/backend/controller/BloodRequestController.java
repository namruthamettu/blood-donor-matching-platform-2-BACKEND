package com.blooddonor.backend.controller;
import com.blooddonor.backend.model.BloodRequest;
import com.blooddonor.backend.repository.BloodRequestRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/requests")
public class BloodRequestController {
 private final BloodRequestRepository repo;
 private static final List<String> GROUPS=List.of("A+","A-","B+","B-","AB+","AB-","O+","O-");
 public BloodRequestController(BloodRequestRepository repo){this.repo=repo;}
 @GetMapping public List<BloodRequest> list(){return repo.findAllByOrderByCreatedAtDesc();}
 @PostMapping public ResponseEntity<?> create(@RequestBody Map<String,Object>b){
  String name=s(b,"requesterName"),phone=s(b,"phone"),group=s(b,"bloodGroup").toUpperCase(),city=s(b,"city");
  if(name.isBlank()||phone.isBlank()||group.isBlank()||city.isBlank())return ResponseEntity.badRequest().body(Map.of("message","Please fill requester name, phone, blood group and city."));
  if(!GROUPS.contains(group))return ResponseEntity.badRequest().body(Map.of("message","Select a valid blood group."));
  int units=1;String raw=s(b,"units");if(!raw.isBlank())try{units=Integer.parseInt(raw);}catch(Exception e){return ResponseEntity.badRequest().body(Map.of("message","Units must be a number."));}
  if(units<1||units>100)return ResponseEntity.badRequest().body(Map.of("message","Units must be between 1 and 100."));
  BloodRequest r=new BloodRequest();r.requesterName=name;r.phone=phone;r.bloodGroup=group;r.city=city;r.units=units;r.hospital=s(b,"hospital");r.notes=s(b,"notes");r.status="OPEN";
  return ResponseEntity.status(201).body(repo.save(r));
 }
 @PatchMapping("/{id}/status") public ResponseEntity<?> status(@PathVariable Long id,@RequestParam String status){
  String s=status.toUpperCase();if(!List.of("OPEN","FULFILLED","CANCELLED").contains(s))return ResponseEntity.badRequest().body(Map.of("message","Invalid status."));
  BloodRequest r=repo.findById(id).orElse(null);if(r==null)return ResponseEntity.status(404).body(Map.of("message","Request not found."));
  r.status=s;return ResponseEntity.ok(repo.save(r));
 }
 @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable Long id){
  if(!repo.existsById(id))return ResponseEntity.status(404).body(Map.of("message","Request not found."));
  repo.deleteById(id);return ResponseEntity.ok(Map.of("message","Request deleted."));
 }
 private String s(Map<String,Object>b,String k){Object v=b.get(k);return v==null?"":v.toString().trim();}
}