package ru.taxi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DriverModel {
    private long id;
    private String name;
    private String phoneNumber;
    private String photoUrl;
    private float rating;
    private boolean license;
    private String carName;
    private String carNumber;
    private String carColor;
    private double positionX;
    private double positionY;
}
