package ru.taxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatsGetByIdResponseDTO {
    private int countOrders;
    private int countWithBabyChair;
    private int countWithoutBabyChair;
    private double countDistance;
    private double countDuration;
    private double countPrice;
}
