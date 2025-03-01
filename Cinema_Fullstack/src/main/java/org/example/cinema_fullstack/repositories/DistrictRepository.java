package org.example.cinema_fullstack.repositories;

import org.example.cinema_fullstack.models.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District,Integer> {
    List<District> findAllByProvinceId(int provinceId);
}
