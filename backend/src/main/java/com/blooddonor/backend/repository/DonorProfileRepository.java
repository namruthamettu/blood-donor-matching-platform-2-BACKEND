package com.blooddonor.backend.repository;
import com.blooddonor.backend.model.DonorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DonorProfileRepository extends JpaRepository<DonorProfile,Long>{
 List<DonorProfile> findByAvailableTrueOrderByCreatedAtDesc();
 List<DonorProfile> findByBloodGroupIgnoreCaseAndAvailableTrueOrderByCreatedAtDesc(String bloodGroup);
 List<DonorProfile> findByCityContainingIgnoreCaseAndAvailableTrueOrderByCreatedAtDesc(String city);
 List<DonorProfile> findByBloodGroupIgnoreCaseAndCityContainingIgnoreCaseAndAvailableTrueOrderByCreatedAtDesc(String bloodGroup,String city);
}