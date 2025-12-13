package com.laptops.ALaptop.repository;

import com.laptops.ALaptop.model.Laptops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaptopRepository extends JpaRepository<Laptops,Long> {
    Laptops getLaptopById(long id);
}
