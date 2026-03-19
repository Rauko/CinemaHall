package com.cinema.dto.user.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdatePasswordRequest {

    @NotNull
    @Size(min = 8)
    private String password;
}
