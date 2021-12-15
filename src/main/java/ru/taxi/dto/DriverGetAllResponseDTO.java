package ru.taxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DriverGetAllResponseDTO {
    private List<Driver> drivers;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Driver {
        private long id;
        private String name;
        private String phoneNumber;
        private String photoUrl;
        private double rating;
        private String carName;
        private String carNumber;
        private String carColor;
        private double position_x;
        private double position_y;
    }
}
