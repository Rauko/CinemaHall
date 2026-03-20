package com.cinema.dto.admin.mapper;

import com.cinema.dto.admin.AdminUserDto;
import com.cinema.model.User;

public class AdminUserMapper {
    public static AdminUserDto toDto(User user){
        if(user == null)  return null;

        AdminUserDto dto = new AdminUserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());

        dto.setPaymentMethodsCount(
                user.getPaymentMethods() != null
                ? user.getPaymentMethods().size()
                        : 0
        );

        return dto;
    }
}
