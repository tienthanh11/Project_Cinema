package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.entity.Ward;
import org.example.cinema_fullstack.repositories.DistrictRepository;
import org.example.cinema_fullstack.repositories.WardRepository;
import org.example.cinema_fullstack.services.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class WardServiceImpl implements WardService {
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    WardRepository wardRepository;
    @Override
    public List<Ward> findAllByDistrictId(int districtId) {
        return wardRepository.findAllByDistrictId(districtId);
    }
}
