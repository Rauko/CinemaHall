package com.cinema.service;

import com.cinema.model.PurchaseHistory;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import com.cinema.model.enums.PurchaseActionType;
import com.cinema.repository.PurchaseHistoryRepository;
import com.cinema.util.LoginLevelCheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryService {

    private PurchaseHistoryRepository repository;
    private UserService userService;

    public void record(User user,
                       Ticket ticket,
                       PurchaseActionType action) {
        PurchaseHistory history = new PurchaseHistory();
        history.setUser(user);
        history.setTicket(ticket);
        history.setAction(action);
        history.setAmount(ticket.getPrice());
        history.setPurchaseTime(LocalDateTime.now());

        repository.save(history);
    }

    public List<PurchaseHistory> getUserHistory(User user) {
        return  repository.findByUserOrderByPurchaseTimeDesc(user);
    }

    public List<PurchaseHistory> getUserHistoryForPeriod(User user,
                                                         LocalDateTime start,
                                                         LocalDateTime end) {
        return repository.findByUserAndPurchaseTimeBetween(user, start, end);
    }

    public List<PurchaseHistory> getHistoryForPeriod(LocalDateTime start,
                                                     LocalDateTime end) {
        User user = userService.getCurrentUser();
        return repository.findByUserAndPurchaseTimeBetween(user, start, end);
    }

    public double getRevenueForDay(LocalDate date) {

        LoginLevelCheckUtil.requireAdminOrSuperAdmin();

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        return repository.sumRevenueBetween(start,end);
    }

    public double getRevenueForMonth(int year, int month) {
        LoginLevelCheckUtil.requireAdminOrSuperAdmin();

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return repository.sumRevenueBetween(
                start.atStartOfDay(),
                end.atTime(LocalTime.MAX)
        );
    }
}
