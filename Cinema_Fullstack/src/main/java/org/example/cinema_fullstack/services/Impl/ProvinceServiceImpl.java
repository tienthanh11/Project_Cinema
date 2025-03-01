package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.entity.Province;
import org.example.cinema_fullstack.repositories.ProvinceRepository;
import org.example.cinema_fullstack.services.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceServiceImpl implements ProvinceService {
    @Autowired
    ProvinceRepository provinceRepository;
    @Override
    public List<Province> findAll() {
        return provinceRepository.findAll();
    }
}
