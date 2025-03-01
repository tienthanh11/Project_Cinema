package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.entity.Ward;

import java.util.List;

public interface WardService {
    List<Ward> findAllByDistrictId(int districtId);
}
