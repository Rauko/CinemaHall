package com.cinema.dto.admin;

import com.cinema.model.enums.Role;
import com.cinema.model.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDto {
    private Long  id;
    private String name;
    private String email;
    private Role role;
    private UserStatus status;
    private int paymentMethodsCount;
}
