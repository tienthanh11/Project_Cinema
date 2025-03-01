package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.entity.District;
import org.example.cinema_fullstack.repositories.DistrictRepository;
import org.example.cinema_fullstack.services.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    DistrictRepository districtRepository;

    @Override
    public List<District> findAllByProvinceId(int provinceId) {
        return districtRepository.findAllByProvinceId(provinceId);
    }
}
