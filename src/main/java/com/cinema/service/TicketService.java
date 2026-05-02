package com.cinema.service;

import com.cinema.dto.payment.request.PaymentRequest;
import com.cinema.exception.*;
import com.cinema.model.*;
import com.cinema.model.enums.*;
import com.cinema.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final ScreeningRepository screeningRepository;
    private final PaymentService paymentService;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final UserService userService;

    @Value("${ticket.reservation-expiration-minutes}")
    private int expirationTimeMinutes;

    // Getters

    public List<Ticket> getMyTickets(){
        return ticketRepository.findByUser(userService.getCurrentUser());
    }

    public List<Ticket> getMyPaidTickets(){
        return ticketRepository.findByUserAndStatus(
                userService.getCurrentUser(),
                TicketStatus.PAID
        );
    }

    public List<Ticket> getMyTicketsByStatus(TicketStatus status){
        return ticketRepository.findByUserAndStatus(
                userService.getCurrentUser(),
                status
        );
    }

    // Admin Getters

    public List<Ticket> getTicketsByUserId(Long userId) {
        User user = userService.getUserById(userId);
        return ticketRepository.findByUser(user);
    }

    public List<Ticket> getTicketsByUserIdAndStatus(Long userId, TicketStatus status) {
        User user = userService.getUserById(userId);
        return ticketRepository.findByUserAndStatus(user, status);
    }

    public Ticket getTicketById(Long ticketId){
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }

    // Create Ticket

    public Ticket createTicket(Long screeningId,
                               Long seatId,
                               ImaxGlassesOption glassesOption){
        User user = userService.getCurrentUser();

        log.info("Creating reservation: userId={}, screeningId={}, seatId={}",
                user.getId(),
                screeningId,
                seatId
        );

        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException(seatId));

        if (!seat.getHall().getId().equals(screening.getHall().getId())) {

            log.warn("Seat/screening mismatch: userId={}, screeningId={}, seatId={}",
                    user.getId(),
                    screeningId,
                    seatId
            );

            throw new SeatScreeningMismatchException(seatId);
        }

        boolean taken = ticketRepository.existsByScreeningIdAndSeatIdAndStatusIn(
                screeningId,
                seatId,
                List.of(TicketStatus.PAID, TicketStatus.RESERVED));


        if (taken) {

            log.warn("Seat already taken: userId={}, screeningId={}, seatId={}",
                    user.getId(),
                    screeningId,
                    seatId
            );

            throw new SeatAlreadyTakenException(seatId);
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setScreening(screening);
        ticket.setSeat(seat);

        double price = screening.getBasePrice();

        // IMAX
        if(screening.isImax()){
            price *= 1.5;

            if (glassesOption == ImaxGlassesOption.RENT){
                price += 50;
            } else if(glassesOption == ImaxGlassesOption.BUY) {
                price += 150;
            }
        }

        // VIP
        if (seat.getType().equals(SeatType.VIP)) {
            price += 0.5 * screening.getBasePrice();
        }

        ticket.setPrice(price);

        ticket.setStatus(TicketStatus.RESERVED);
        ticket.setReservedAt(java.time.LocalDateTime.now());

        ticketRepository.save(ticket);

        log.info("Reservation created: ticketId={}, userId={}, screeningId={}, seatId={}, price={}",
                ticket.getId(),
                user.getId(),
                screeningId,
                seatId,
                ticket.getPrice()
        );

        return ticket;
    }

    // Cancel Reservation

    public Ticket cancelReservation(Long ticketId){

        User user = userService.getCurrentUser();

        log.info("Cancelling reservation: ticketId={}, userId={}",
                ticketId,
                user.getId()
        );

        Ticket ticket = getTicketById(ticketId);

        ticketCheck(ticket, user);

        ticket.setStatus(TicketStatus.CANCELLED);
        ticketRepository.save(ticket);

        log.info("Reservation cancelled: ticketId={}, userId={}",
                ticketId,
                user.getId()
        );


        return ticket;
    }

    // Auto expire

    @Scheduled(fixedRate = 60000) //once a minute
    @Transactional
    public void expireOldReservations(){
        LocalDateTime expirationTime =
                LocalDateTime.now().minusMinutes(expirationTimeMinutes);

        List<Ticket> oldTickets =
                ticketRepository.findByStatusAndReservedAtBefore(
                        TicketStatus.EXPIRED,
                        expirationTime
                );

        for (Ticket ticket : oldTickets) {
            ticket.setStatus(TicketStatus.EXPIRED);

            log.info("Reservation auto-expired: ticketId={}, userId={}",
                    ticket.getId(),
                    ticket.getUser().getId()
            );
        }

        ticketRepository.saveAll(oldTickets);
    }

    // Pay

    @Transactional // if exception - rollback
    public Ticket payForTicket(Long ticketId, PaymentRequest paymentRequest){
        User user = userService.getCurrentUser();

        log.info("Processing ticket payment: ticketId={}, userId={}",
                ticketId,
                user.getId()
        );

        Ticket ticket = getTicketById(ticketId);

        ticketCheck(ticket, user);

        PurchaseHistory history = new PurchaseHistory();
        history.setUser(user);
        history.setAmount(ticket.getPrice());
        history.setAction(PurchaseActionType.PURCHASE);
        history.setPurchaseTime(LocalDateTime.now());
        history.setPaymentStatus(PaymentStatus.PENDING);

        purchaseHistoryRepository.save(history);


        //expired payment protection
        LocalDateTime expirationTime =
                ticket.getReservedAt().plusMinutes(expirationTimeMinutes);

        if (expirationTime.isBefore(LocalDateTime.now())) {

            log.warn("Reservation expired before payment: ticketId={}, userId={}",
                    ticketId,
                    user.getId()
            );

            ticket.setStatus(TicketStatus.EXPIRED);
            ticketRepository.save(ticket);

            history.setPaymentStatus(PaymentStatus.EXPIRED);
            purchaseHistoryRepository.save(history);

            throw new ReservationExpiredException();
        }

        //payment itself - if failed - EXCEPTION and rollback
        PaymentMethod paymentMethod =
                paymentMethodRepository
                        .findByUserAndIsDefaultTrue(user)
                        .orElseThrow(NoDefaultPaymentMethodException::new);

        try {
            paymentService.processPayment(
                    paymentMethod,
                    ticket.getPrice()
            );
        } catch (PaymentException ex) {
            history.setPaymentStatus(PaymentStatus.FAILED);
            purchaseHistoryRepository.save(history);
            throw ex;
        }

        history.setPaymentStatus(PaymentStatus.SUCCESS);
        purchaseHistoryRepository.save(history);

        ticket.setStatus(TicketStatus.PAID);
        ticket.setPurchaseTime(LocalDateTime.now());

        ticketRepository.save(ticket);

        log.info("Payment successful: ticketId={}, userId={}, amount={}",
                ticket.getId(),
                user.getId(),
                ticket.getPrice()
        );

        return ticket;
    }

    private void ticketCheck(Ticket ticket, User user){

        if (!Objects.equals(ticket.getUser().getId(), user.getId())) {

            log.warn("Unauthorized ticket payment attempt: ticketId={}, ownerId={}, requesterId={}",
                    ticket.getId(),
                    ticket.getUser().getId(),
                    user.getId()
            );

            throw new AppAccessDeniedException("You can pay for YOUR OWN ticket only");
        }

        if (ticket.getStatus() != TicketStatus.RESERVED) {

            log.warn("Invalid ticket status for payment: ticketId={}, userId={}, status={}",
                    ticket.getId(),
                    user.getId(),
                    ticket.getStatus()
            );

            throw new InvalidTicketStateException("Only RESERVED tickets can be paid");
        }
    }
}
