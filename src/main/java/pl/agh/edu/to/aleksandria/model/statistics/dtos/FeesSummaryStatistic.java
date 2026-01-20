package pl.agh.edu.to.aleksandria.model.statistics.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FeesSummaryStatistic {
    Double totalPaid;
    Double totalToBePaid;
    List<FeesStatisticByUser> feesByUser;
}
