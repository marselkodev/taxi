package ru.taxi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatModel {
    private boolean babyChair;
    private double distance;
    private double duration;
    private double price;
}
