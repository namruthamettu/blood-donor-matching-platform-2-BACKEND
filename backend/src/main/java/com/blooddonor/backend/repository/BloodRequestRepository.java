package com.blooddonor.backend.repository;
import com.blooddonor.backend.model.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BloodRequestRepository extends JpaRepository<BloodRequest,Long>{
 List<BloodRequest> findAllByOrderByCreatedAtDesc();
}