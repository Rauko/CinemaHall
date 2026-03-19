package com.cinema.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateNameRequest {
        @NotBlank
        private String Name;
}
