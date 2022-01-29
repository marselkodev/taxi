package ru.taxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderSaveResponseDTO {
    private OrderSaveResponseDTO.Order order;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Order {
        private long id;
        private String addressFrom;
        private String addressTo;
        private String driverComment;
        private boolean babyChair;
        private double distance;
        private double duration;
        private double price;
        private boolean accept;
    }
}
