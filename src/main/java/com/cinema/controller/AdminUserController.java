package com.cinema.controller;

import com.cinema.dto.admin.AdminUserDto;
import com.cinema.dto.admin.mapper.AdminUserMapper;
import com.cinema.dto.ticket.TicketDto;
import com.cinema.dto.ticket.mapper.TicketMapper;
import com.cinema.model.enums.TicketStatus;
import com.cinema.service.TicketService;
import com.cinema.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;
    private final TicketService ticketService;

    @GetMapping("/all")
    public List<AdminUserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(AdminUserMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDto> getUserByID(@PathVariable Long id){
        try {
            return ResponseEntity.ok(
                    AdminUserMapper.toDto(userService.getUserById(id)));
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<TicketDto>> getUserTickets(@PathVariable Long id){
        return ResponseEntity.ok(
                ticketService.getTicketsByUserId(id)
                        .stream()
                        .map(TicketMapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/{id}/tickets/paid")
    public ResponseEntity<List<TicketDto>> getUserPaidTickets(@PathVariable Long id){
        return ResponseEntity.ok(
                ticketService.getTicketsByUserIdAndStatus(id, TicketStatus.PAID)
                        .stream()
                        .map(TicketMapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/{id}/tickets/reserved")
    public ResponseEntity<List<TicketDto>> getUserReservedTickets(@PathVariable Long id){
        return ResponseEntity.ok(
                ticketService.getTicketsByUserIdAndStatus(id, TicketStatus.RESERVED)
                        .stream()
                        .map(TicketMapper::toDto)
                        .toList()

        );
    }

    @GetMapping("/{id}/tickets/expired")
    public ResponseEntity<List<TicketDto>> getUserExpiredTickets(@PathVariable Long id){
        return ResponseEntity.ok(
                ticketService.getTicketsByUserIdAndStatus(id, TicketStatus.EXPIRED)
                        .stream()
                        .map(TicketMapper::toDto)
                        .toList()

        );
    }

    @GetMapping("/{id}/tickets/cancelled")
    public ResponseEntity<List<TicketDto>> getUserCancelledTickets(@PathVariable Long id){
        return ResponseEntity.ok(
                ticketService.getTicketsByUserIdAndStatus(id, TicketStatus.CANCELLED)
                        .stream()
                        .map(TicketMapper::toDto)
                        .toList()

        );
    }
}
