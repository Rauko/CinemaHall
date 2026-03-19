package com.cinema.dto.user;

import com.cinema.model.enums.Role;
import com.cinema.model.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long  id;
    private String name;
    private String email;
    private Role role;
    private UserStatus status;
}
