package com.blooddonor.backend.controller;
import com.blooddonor.backend.model.User;
import com.blooddonor.backend.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/users")
public class UserController {
 private final UserRepository users; private final PasswordEncoder encoder;
 public UserController(UserRepository users,PasswordEncoder encoder){this.users=users;this.encoder=encoder;}
 @PostMapping("/register") public ResponseEntity<?> register(@RequestBody Map<String,String> b){
  String name=s(b,"name"),email=s(b,"email").toLowerCase(),password=s(b,"password");
  if(name.isBlank()||email.isBlank()||password.length()<6)return ResponseEntity.badRequest().body(Map.of("message","Enter your name and email, and a password of at least 6 characters."));
  if(users.existsByEmailIgnoreCase(email))return ResponseEntity.status(409).body(Map.of("message","This email is already registered."));
  User u=new User();u.name=name;u.email=email;u.password=encoder.encode(password);
  u=users.save(u);return ResponseEntity.status(201).body(Map.of("id",u.id,"name",u.name,"email",u.email,"message","Account created."));
 }
 @PostMapping("/login") public ResponseEntity<?> login(@RequestBody Map<String,String> b){
  User u=users.findByEmailIgnoreCase(s(b,"email").toLowerCase()).orElse(null);
  if(u==null||!encoder.matches(s(b,"password"),u.password))return ResponseEntity.status(401).body(Map.of("message","Invalid email or password."));
  return ResponseEntity.ok(Map.of("id",u.id,"name",u.name,"email",u.email,"message","Signed in."));
 }
 private String s(Map<String,String>b,String k){return b.getOrDefault(k,"").trim();}
}