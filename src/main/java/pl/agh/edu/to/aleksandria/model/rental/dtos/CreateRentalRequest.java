package pl.agh.edu.to.aleksandria.model.rental.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CreateRentalRequest {
    private int userId;
    private int bookId;
    private int rentalDays;

}
