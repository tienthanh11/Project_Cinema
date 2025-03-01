package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.entity.PaymentMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentMethodService {
    List<PaymentMethod> getAllPaymentMethod();
}
