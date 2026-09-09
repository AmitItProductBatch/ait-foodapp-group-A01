package com.ait.app.repository;
import com.ait.app.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByUser_UserId(int userId);
}
