package ru.taxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DriverSaveRequestDTO {
    private long id;
    private String name;
    private String phoneNumber;
    private String photoUrl;
    private boolean license;
    private String carName;
    private String carNumber;
    private String carColor;
}
