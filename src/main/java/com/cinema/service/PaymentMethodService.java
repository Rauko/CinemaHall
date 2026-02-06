package com.cinema.service;

import com.cinema.dto.AddPaymentMethodRequest;
import com.cinema.model.PaymentMethod;
import com.cinema.model.User;
import com.cinema.model.enums.PaymentMethodType;
import com.cinema.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepository repository;

    public PaymentMethodService(PaymentMethodRepository repository) {
        this.repository = repository;
    }

    public PaymentMethod addMethod(User user, AddPaymentMethodRequest request) {

        PaymentMethod method = new PaymentMethod();
        method.setUser(user);
        method.setType(request.getType());

        if (request.getType() == PaymentMethodType.CARD) {
            String masked = "**** **** **** " + request.getCardNumber()
                    .substring(request.getCardNumber().length() - 4);

            method.setMaskedDetails(masked);

            //  use real tokenization here
            method.setProviderToken(UUID.randomUUID().toString());
        }
        if (request.isMakeDefault()) {
            unsetOldDefault(user);
            method.setDefault(true);
        }

        return repository.save(method);
    }

    private void unsetOldDefault(User user) {
        repository.findByUserAndIsDefaultTrue(user)
                .ifPresent(pm -> {
                    pm.setDefault(false);
                    repository.save(pm);
                });
    }
}
