package com.cinema.controller;

import com.cinema.dto.AddPaymentMethodRequest;
import com.cinema.dto.payment.PaymentMethodDto;
import com.cinema.dto.payment.mapper.PaymentMethodMapper;
import com.cinema.model.PaymentMethod;
import com.cinema.model.User;
import com.cinema.service.PaymentMethodService;
import com.cinema.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<PaymentMethodDto> addMethod(
            @RequestBody AddPaymentMethodRequest request) {

        User user = userService.getCurrentUser();

        PaymentMethod method = paymentMethodService.addMethod(user, request);

        return ResponseEntity.ok(PaymentMethodMapper.toDto(method));
    }
}
