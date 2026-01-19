package pl.agh.edu.to.aleksandria.model.rental.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExtendRentalRequest {
    private int rentalId;
    private int days;
}
