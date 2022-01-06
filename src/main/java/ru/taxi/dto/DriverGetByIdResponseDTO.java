package ru.taxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DriverGetByIdResponseDTO {
    private Driver driver;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Driver {
        private long id;
        private String name;
        private String phoneNumber;
        private String photoUrl;
        private double rating;
        private boolean license;
        private String carName;
        private String carNumber;
        private String carColor;
    }
}
