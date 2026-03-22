package com.cinema.dto.payment.mapper;

import com.cinema.dto.payment.PaymentMethodDto;
import com.cinema.model.PaymentMethod;

public class PaymentMethodMapper {
    public static PaymentMethodDto toDto(PaymentMethod paymentMethod) {
        if(paymentMethod == null) return null;
        PaymentMethodDto dto = new PaymentMethodDto();
        dto.setId(paymentMethod.getId());
        dto.setType(paymentMethod.getType());
        dto.setMaskedDetails(paymentMethod.getMaskedDetails());
        dto.setDefault(paymentMethod.isDefault());

        return dto;
    }
}
