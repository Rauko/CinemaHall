package com.cinema.repository;

import com.cinema.model.Ticket;
import com.cinema.model.TicketStatus;
import com.cinema.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository  extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser(User user);
    List<Ticket> findByStatus(TicketStatus status);

    void deleteAllByStatus(TicketStatus status);

    List<Ticket> findByUserAndStatus(User user, TicketStatus ticketStatus);

    long countByStatus(TicketStatus status);

    boolean existsByScreeningIdAndSeatId(Long screeningId, Long seatId);
}
