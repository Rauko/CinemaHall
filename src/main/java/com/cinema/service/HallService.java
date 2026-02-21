package com.cinema.service;

import com.cinema.model.Hall;
import com.cinema.model.Seat;
import com.cinema.model.enums.SeatType;
import com.cinema.repository.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HallService {

    private final HallRepository hallRepository;

    public Hall createHallWithSeats(String name, int rows, int seatsPerRow) {
        Hall hall = new Hall();
        hall.setName(name);

        for (int r = 1; r <= rows; r++){
            for ( int s = 1; s <= seatsPerRow; s++){
                Seat seat = new Seat();
                seat.setHall(hall);
                seat.setRowNumber(r);
                seat.setSeatNumber(s);

                seat.setType(r == rows ? SeatType.VIP : SeatType.STANDARD);

                hall.getSeats().add(seat);
            }
        }
        return hallRepository.save(hall);
    }
}
