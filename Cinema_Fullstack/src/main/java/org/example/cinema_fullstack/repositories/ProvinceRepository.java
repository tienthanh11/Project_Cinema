package org.example.cinema_fullstack.repositories;

import org.example.cinema_fullstack.models.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,Integer> {
    List<Province> findAll();
}
