package com.cinema.service;

import com.cinema.model.PurchaseHistory;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import com.cinema.model.enums.PurchaseActionType;
import com.cinema.repository.PurchaseHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseHistoryService {

    private PurchaseHistoryRepository repository;
    private UserService userService;

    public PurchaseHistoryService(PurchaseHistoryRepository repository,
                                  UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

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
        return  repository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<PurchaseHistory> getUserHistoryForPeriod(User user,
                                                         LocalDateTime start,
                                                         LocalDateTime end) {
        return repository.findByUserAndMadeAtBetween(user, start, end);
    }

    public List<PurchaseHistory> getHistoryForPeriod(LocalDateTime start,
                                                     LocalDateTime end) {
        User user = userService.getCurrentUser();
        return repository.findByUserAndMadeAtBetween(user, start, end);
    }
}
