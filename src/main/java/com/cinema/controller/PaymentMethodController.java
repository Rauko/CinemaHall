package com.cinema.controller;

import com.cinema.dto.AddPaymentMethodRequest;
import com.cinema.model.PaymentMethod;
import com.cinema.model.User;
import com.cinema.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService service;

    @PostMapping
    public ResponseEntity<PaymentMethod> addMethod(
            @RequestBody AddPaymentMethodRequest request,
            Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(service.addMethod(user, request));
    }
}
