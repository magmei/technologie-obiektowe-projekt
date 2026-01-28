package pl.agh.edu.to.aleksandria.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RentalConfig {

    @Value("${application.rental.late-fee}")
    @Getter
    private double lateFee;
}
