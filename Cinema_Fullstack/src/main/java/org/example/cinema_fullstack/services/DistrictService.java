package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.entity.District;

import java.util.List;

public interface DistrictService {
    List<District> findAllByProvinceId(int provinceId);
}
