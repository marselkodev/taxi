package ru.taxi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderModel {
    private long id;
    private String addressFrom;
    private String addressTo;
    private String driverComment;
    private boolean babyChair;
    private int choiceOfCarClass;
    private double distance;
    private double duration;
    private double price;
    private boolean accept;
}
