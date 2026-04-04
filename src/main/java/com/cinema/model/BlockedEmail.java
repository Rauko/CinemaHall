package com.cinema.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blocked_emails")
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

    @Column(name = "blocked_at")
    private LocalDateTime blockedAt;
}
