package ru.taxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderSaveRequestDTO {
    private long id;
    private String addressFrom;
    private String addressTo;
    private String driverComment;
    private boolean babyChair;
    private int choiceOfCarClass;
}
