package com.cinema.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "blocked_email")
@Getter
@Setter
@NoArgsConstructor
public class BlockedEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String email;

    private String reason;

    private LocalDateTime blockedDate;
}
