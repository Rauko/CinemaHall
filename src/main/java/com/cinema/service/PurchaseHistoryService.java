package com.cinema.service;

import com.cinema.model.PurchaseHistory;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import com.cinema.model.enums.PurchaseActionType;
import com.cinema.repository.PurchaseHistoryRepository;
import com.cinema.util.LoginLevelCheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryService {

    private final PurchaseHistoryRepository repository;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(PurchaseHistoryService.class);

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

        log.info("Fetching purchase history for userId={}", user.getId());

        List<PurchaseHistory> history =
                repository.findByUserOrderByPurchaseTimeDesc(user);

        log.debug("Purchase history fetched: userId={}, recordsCount={}",
                user.getId(),
                history.size()
        );

        return history;
    }

    public List<PurchaseHistory> getUserHistoryForPeriod(User user,
                                                         LocalDateTime start,
                                                         LocalDateTime end) {

        log.info("Filtering purchase history for userId={} from {} to {}",
                user.getId(),
                start,
                end
        );

        List<PurchaseHistory> history =
                repository.findByUserAndPurchaseTimeBetween(user, start, end);

        log.debug("Filtered purchase history fetched: userId={}, recordsCount={}, start={}, end={}",
                user.getId(),
                history.size(),
                start,
                end
        );

        return history;
    }

    public List<PurchaseHistory> getHistoryForPeriod(LocalDateTime start,
                                                     LocalDateTime end) {
        User user = userService.getCurrentUser();

        log.info("Fetching current user purchase history for period: userId={}, start={}, end={}",
                user.getId(),
                start,
                end
        );

        List<PurchaseHistory> history =
                repository.findByUserAndPurchaseTimeBetween(user, start, end);

        log.debug("Current user filtered history fetched: userId={}, recordsCount={}",
                user.getId(),
                history.size()
        );

        return history;
    }

    public double getRevenueForDay(LocalDate date) {

        LoginLevelCheckUtil.requireAdminOrSuperAdmin();

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        log.info("Calculating daily revenue for date={}", date);
        log.debug("Daily revenue period: start={}, end={}", start, end);

        return repository.sumRevenueBetween(start,end);
    }

    public double getRevenueForMonth(int year, int month) {
        LoginLevelCheckUtil.requireAdminOrSuperAdmin();

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        log.info("Calculating monthly revenue: year={}, month={}", year, month);
        log.debug("Monthly revenue period: start={}, end={}",
                start.atStartOfDay(),
                end.atTime(LocalTime.MAX)
        );

        return repository.sumRevenueBetween(
                start.atStartOfDay(),
                end.atTime(LocalTime.MAX)
        );
    }
}
