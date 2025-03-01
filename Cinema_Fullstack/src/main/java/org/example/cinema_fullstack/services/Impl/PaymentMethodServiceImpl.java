package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.entity.PaymentMethod;
import org.example.cinema_fullstack.repositories.PaymentMethodRepository;
import org.example.cinema_fullstack.services.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethod> getAllPaymentMethod() {
        return paymentMethodRepository.findAll();
    }
}
