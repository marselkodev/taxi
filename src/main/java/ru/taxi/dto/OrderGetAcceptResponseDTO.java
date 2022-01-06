package ru.taxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderGetAcceptResponseDTO {
    private List<Order> orders;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Order {
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
}
