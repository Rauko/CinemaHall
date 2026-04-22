package com.cinema.service;

import com.cinema.model.Hall;
import com.cinema.model.Seat;
import com.cinema.model.enums.SeatType;
import com.cinema.repository.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class HallService {

    private final HallRepository hallRepository;

    private static final Logger log = LoggerFactory.getLogger(HallService.class);

    public Hall createHallWithSeats(String name, int rows, int seatsPerRow) {

        log.info("Creating hall: name={}, rows={}, seatsPerRow={}", name, rows, seatsPerRow);

        Hall hall = new Hall();
        hall.setName(name);

        int totalSeats = 0;

        for (int r = 1; r <= rows; r++){
            for ( int s = 1; s <= seatsPerRow; s++){
                Seat seat = new Seat();
                seat.setHall(hall);
                seat.setRowNumber(r);
                seat.setSeatNumber(s);

                seat.setType(r == rows ? SeatType.VIP : SeatType.STANDARD);

                hall.getSeats().add(seat);
                totalSeats++;
            }
        }

        hallRepository.save(hall);

        log.info("Hall created: id={}, name={}, totalSeats={}",
                hall.getId(),
                hall.getName(),
                totalSeats
        );

        return hall;
    }
}
