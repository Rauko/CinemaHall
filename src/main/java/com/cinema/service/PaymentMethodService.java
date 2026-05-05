package com.cinema.service;

import com.cinema.dto.AddPaymentMethodRequest;
import com.cinema.model.PaymentMethod;
import com.cinema.model.User;
import com.cinema.model.enums.PaymentMethodType;
import com.cinema.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private static final Logger log = LoggerFactory.getLogger(PaymentMethodService.class);

    private final PaymentMethodRepository repository;

    public PaymentMethod addMethod(User user, AddPaymentMethodRequest request) {

        log.info("Adding payment method: userId={}, type={}, makeDefault={}",
                user.getId(),
                request.getType(),
                request.isMakeDefault()
        );

        if (request.getType() == null) {
            log.warn("Invalid payment method type: userId={}", user.getId());
            throw new IllegalArgumentException("Payment method type is required");
        }

        PaymentMethod method = new PaymentMethod();
        method.setUser(user);
        method.setType(request.getType());

        if (request.getType() == PaymentMethodType.CARD) {

            if (request.getCardNumber() == null || request.getCardNumber().length() < 4) {
                log.warn("Invalid card data: userId={}", user.getId());
                throw new IllegalArgumentException("Invalid card number");
            }

            String masked = "**** **** **** " + request.getCardNumber()
                    .substring(request.getCardNumber().length() - 4);

            method.setMaskedDetails(masked);

            //  use real tokenization here
            method.setProviderToken(UUID.randomUUID().toString());
        }
        if (request.isMakeDefault()) {
            unsetOldDefault(user);
            method.setDefault(true);

            log.info("Default payment method changed: userId={}, newType={}",
                    user.getId(),
                    method.getType()
            );
        }

        log.info("Payment method added: userId={}, methodId={}, type={}",
                user.getId(),
                method.getId(),
                method.getType()
        );

        return repository.save(method);
    }

    private void unsetOldDefault(User user) {
        repository.findByUserAndIsDefaultTrue(user)
                .ifPresent(pm -> {

                    log.info("Removing previous default payment method: userId={}, methodId={}",
                            user.getId(),
                            pm.getId()
                    );

                    pm.setDefault(false);
                    repository.save(pm);
                });
    }
}
