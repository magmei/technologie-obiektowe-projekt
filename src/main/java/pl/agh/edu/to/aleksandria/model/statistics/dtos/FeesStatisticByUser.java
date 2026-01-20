package pl.agh.edu.to.aleksandria.model.statistics.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FeesStatisticByUser {
    private Integer userId;
    private String fullName;
    private Double totalPaid;
    private Double totalToBePaid;
}
