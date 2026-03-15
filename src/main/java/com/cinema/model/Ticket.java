package com.cinema.model;

import com.cinema.model.enums.ImaxGlassesOption;
import com.cinema.model.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "tickets",
        uniqueConstraints = @UniqueConstraint(columnNames = {"screening_id", "seat_id"})
)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = false)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(nullable = false)
    private LocalDateTime purchaseTime;

    @Column(nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.RESERVED;

    private LocalDateTime reservedAt = LocalDateTime.now();
    private LocalDateTime purchaseDate;

    @Enumerated(EnumType.STRING)
    private ImaxGlassesOption glassesOption = ImaxGlassesOption.NONE;
}
