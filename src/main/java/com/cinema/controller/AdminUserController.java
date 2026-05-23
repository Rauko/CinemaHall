package com.cinema.controller;

import com.cinema.dto.admin.AdminUserDto;
import com.cinema.dto.admin.mapper.AdminUserMapper;
import com.cinema.dto.ticket.TicketDto;
import com.cinema.dto.ticket.mapper.TicketMapper;
import com.cinema.model.User;
import com.cinema.model.enums.TicketStatus;
import com.cinema.service.TicketService;
import com.cinema.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private static final Logger log =
            LoggerFactory.getLogger(AdminUserController.class);

    private final UserService userService;
    private final TicketService ticketService;

    @GetMapping("/all")
    public List<AdminUserDto> getAllUsers() {

        User admin = userService.getCurrentUser();

        log.info("ADMIN ACTION by {}({}): requestType=GET_ALL_USERS",
                admin.getName(),
                admin.getId()
        );

        List<AdminUserDto> users = userService.getAllUsers()
                .stream()
                .map(AdminUserMapper::toDto)
                .toList();

        log.info("ADMIN ACTION by {}({}): requestType=GET_ALL_USERS, count={}",
                admin.getName(),
                admin.getId(),
                users.size()
        );

        return users;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDto> getUserByID(@PathVariable Long id){

        User admin = userService.getCurrentUser();

        log.info("ADMIN ACTION by {}({}): requestType=GET_USER, targetUserId={}",
                admin.getName(),
                admin.getId(),
                id
        );

        try {

            AdminUserDto dto = AdminUserMapper.toDto(
                    userService.getUserById(id)
            );

            log.info("ADMIN ACTION by {}({}): requestType=GET_USER, targetUserId={}",
                    admin.getName(),
                    admin.getId(),
                    id
            );

            return ResponseEntity.ok(dto);

        } catch (RuntimeException e) {

            log.warn("ADMIN ACTION by {}({}): User not found targetUserId={}",
                    admin.getName(),
                    admin.getId(),
                    id
            );

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<TicketDto>> getUserTickets(@PathVariable Long id){

        User admin = userService.getCurrentUser();

        log.info("ADMIN ACTION by {}({}): requestType=GET_USER_TICKETS, targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        List<TicketDto> tickets =
                ticketService.getTicketsByUserId(id)
                        .stream()
                        .map(TicketMapper::toDto)
                        .toList();

        log.info("ADMIN ACTION by {}({}): requestType=GET_USER_TICKETS, targetUserId={}, count={}",
                 admin.getName(),
                 admin.getId(),
                 id,
                 tickets.size());

        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}/tickets/paid")
    public ResponseEntity<List<TicketDto>> getUserPaidTickets(@PathVariable Long id){

        User admin = userService.getCurrentUser();

        log.info("ADMIN ACTION by {}({}): requestType=GET_PAID_TICKETS, targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        List<TicketDto> tickets =
                ticketService.getTicketsByUserIdAndStatus(
                                id,
                                TicketStatus.PAID
                        )
                        .stream()
                        .map(TicketMapper::toDto)
                        .toList();

        log.info("ADMIN ACTION by {}({}): requestType=GET_PAID_TICKETS, targetUserId={}, count={}",
                 admin.getName(),
                 admin.getId(),
                 id,
                 tickets.size());

        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}/tickets/reserved")
    public ResponseEntity<List<TicketDto>> getUserReservedTickets(@PathVariable Long id){

        User admin = userService.getCurrentUser();

        log.info("ADMIN ACTION by {}({}): requestType=GET_RESERVED_TICKETS, targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        List<TicketDto> tickets =
                ticketService.getTicketsByUserIdAndStatus(
                                id,
                                TicketStatus.RESERVED
                        )
                        .stream()
                        .map(TicketMapper::toDto)
                        .toList();

        log.info("ADMIN ACTION by {}({}): requestType=GET_RESERVED_TICKETS, targetUserId={}, count={}",
                 admin.getName(),
                 admin.getId(),
                 id,
                 tickets.size());

        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}/tickets/expired")
    public ResponseEntity<List<TicketDto>> getUserExpiredTickets(@PathVariable Long id){

        User admin = userService.getCurrentUser();

        log.info("ADMIN ACTION by {}({}): requestType=GET_EXPIRED_TICKETS, targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        List<TicketDto> tickets =
                ticketService.getTicketsByUserIdAndStatus(
                                id,
                                TicketStatus.EXPIRED
                        )
                        .stream()
                        .map(TicketMapper::toDto)
                        .toList();

        log.info("ADMIN ACTION by {}({}): requestType=GET_EXPIRED_TICKETS, targetUserId={}, count={}",
                 admin.getName(),
                 admin.getId(),
                 id,
                 tickets.size());

        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}/tickets/cancelled")
    public ResponseEntity<List<TicketDto>> getUserCancelledTickets(@PathVariable Long id){

        User admin = userService.getCurrentUser();

        log.info("ADMIN ACTION by {}({}): requestType=GET_CANCELLED_TICKETS, targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        List<TicketDto> tickets =
                ticketService.getTicketsByUserIdAndStatus(
                                id,
                                TicketStatus.CANCELLED
                        )
                        .stream()
                        .map(TicketMapper::toDto)
                        .toList();

        log.info("ADMIN ACTION by {}({}): requestType=GET_CANCELLED_TICKETS, targetUserId={}, count={}",
                 admin.getName(),
                 admin.getId(),
                 id,
                 tickets.size());

        return ResponseEntity.ok(tickets);
    }
}
