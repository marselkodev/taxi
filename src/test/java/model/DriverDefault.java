package model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.taxi.dto.DriverGetAllResponseDTO;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DriverDefault {

    public static DriverGetAllResponseDTO get() {
        DriverGetAllResponseDTO driverGetAllResponseDTO = new DriverGetAllResponseDTO();
        DriverGetAllResponseDTO.Driver driver = new DriverGetAllResponseDTO.Driver();
        driver.setId(1);
        driver.setName("name");
        driver.setPhoneNumber("phoneNumber");
        driver.setPhotoUrl("photoUrl");
        driver.setRating(1.1);
        driver.setCarName("carName");
        driver.setCarNumber("carNumber");
        driver.setCarColor("carColor");
        driverGetAllResponseDTO.setDrivers(List.of(driver));

        return driverGetAllResponseDTO;
    }
}
