package ru.taxi.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.taxi.dto.DriverGetAllResponseDTO;
import ru.taxi.dto.DriverGetByIdResponseDTO;
import ru.taxi.dto.DriverSaveRequestDTO;
import ru.taxi.dto.DriverSaveResponseDTO;
import ru.taxi.exception.DriverNotFoundException;
import ru.taxi.model.DriverModel;
import ru.taxi.rowmapper.DriverRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DriverManager {
    private final NamedParameterJdbcTemplate template;
    private final DriverRowMapper driverRowMapper;

    public DriverGetAllResponseDTO getAll() {
        final List<DriverModel> drivers = template.query(
                //language=PostgreSQL
                """
                        SELECT id, name, phone_number, photo_url, rating, license, car_name, car_number, car_color, position_x, position_y FROM drivers
                        WHERE removed = FALSE
                        ORDER BY id
                        LIMIT 100
                        """,
                driverRowMapper
        );

        final DriverGetAllResponseDTO responseDTO = new DriverGetAllResponseDTO(new ArrayList<>(drivers.size()));
        for (DriverModel driver : drivers) {
            responseDTO.getDrivers().add(new DriverGetAllResponseDTO.Driver(
                    driver.getId(),
                    driver.getName(),
                    driver.getPhoneNumber(),
                    driver.getPhotoUrl(),
                    driver.getRating(),
                    driver.getCarName(),
                    driver.getCarNumber(),
                    driver.getCarColor(),
                    driver.getPositionX(),
                    driver.getPositionY()
            ));

        }

        return responseDTO;
    }

    public DriverGetByIdResponseDTO getById(long id) {
        try {
            final DriverModel driver = template.queryForObject(
                    //language=PostgreSQL
                    """
                            SELECT id, name, phone_number, photo_url, rating, license, car_name, car_number, car_color, position_x, position_y FROM drivers
                            WHERE id = :id AND removed = FALSE
                            """,
                    Map.of("id", id),

                    driverRowMapper
            );

            final DriverGetByIdResponseDTO responseDTO = new DriverGetByIdResponseDTO(new DriverGetByIdResponseDTO.Driver(
                    driver.getId(),
                    driver.getName(),
                    driver.getPhoneNumber(),
                    driver.getPhotoUrl(),
                    driver.getRating(),
                    driver.getCarName(),
                    driver.getCarNumber(),
                    driver.getCarColor(),
                    driver.getPositionX(),
                    driver.getPositionY()
            ));
            return responseDTO;
        } catch (EmptyResultDataAccessException e) {
            throw new DriverNotFoundException(e);
        }
    }

    public DriverSaveResponseDTO save(DriverSaveRequestDTO requestDTO) {
        if (requestDTO.getId() == 0) {
            return create(requestDTO);
        }
        return update(requestDTO);

    }

    private DriverSaveResponseDTO create(DriverSaveRequestDTO requestDTO) {
            final DriverModel driver = template.queryForObject(
                    //language=PostgreSQL
                    """
                                        INSERT INTO drivers (name, phone_number, photo_url, license, car_name, car_number, car_color, position_x, position_y)
                                        VALUES (:name, :phone_number, :photo_url, :license, :car_name, :car_number, :car_color, :position_x, :position_y)
                                        RETURNING id, name, phone_number, photo_url, rating, license, car_name, car_number, car_color, position_x, position_y
                        """,
                Map.of("name", requestDTO.getName(),
                        "phone_number", requestDTO.getPhoneNumber(),
                        "photo_url", requestDTO.getPhotoUrl(),
                        "license", requestDTO.isLicense(),
                        "car_name", requestDTO.getCarName(),
                        "car_number", requestDTO.getCarNumber(),
                        "car_color", requestDTO.getCarColor(),
                        "position_x", requestDTO.getPositionX(),
                        "position_y", requestDTO.getPositionY()),
                driverRowMapper
        );

        final DriverSaveResponseDTO responseDTO = new DriverSaveResponseDTO(new DriverSaveResponseDTO.Driver(
                driver.getId(),
                driver.getName(),
                driver.getPhoneNumber(),
                driver.getPhotoUrl(),
                driver.getRating(),
                driver.getCarName(),
                driver.getCarNumber(),
                driver.getCarColor(),
                driver.getPositionX(),
                driver.getPositionY()
        ));

        return responseDTO;


    }

    private DriverSaveResponseDTO update(DriverSaveRequestDTO requestDTO) {
        try {
            final DriverModel driver = template.queryForObject(
                    //language=PostgreSQL
                    """
                                        UPDATE drivers SET name = :name,  phone_number = :phone_number, photo_url = :photo_url, license = :license, car_name = :car_name, car_number = :car_number, car_color = :car_color, position_x = :position_x, position_y = :position_y
                                        WHERE id = :id AND removed = FALSE
                                        RETURNING id, name, phone_number, photo_url, rating, license, car_name, car_number, car_color, position_x, position_y
                        """,
                Map.of("id", requestDTO.getId(),
                        "name", requestDTO.getName(),
                        "phone_number", requestDTO.getPhoneNumber(),
                        "photo_url", requestDTO.getPhotoUrl(),
                        "license", requestDTO.isLicense(),
                        "car_name", requestDTO.getCarName(),
                        "car_number", requestDTO.getCarNumber(),
                        "car_color", requestDTO.getCarColor(),
                        "position_x", requestDTO.getPositionX(),
                        "position_y", requestDTO.getPositionY()),
                driverRowMapper
        );

        final DriverSaveResponseDTO responseDTO = new DriverSaveResponseDTO(new DriverSaveResponseDTO.Driver(
                driver.getId(),
                driver.getName(),
                driver.getPhoneNumber(),
                driver.getPhotoUrl(),
                driver.getRating(),
                driver.getCarName(),
                driver.getCarNumber(),
                driver.getCarColor(),
                driver.getPositionX(),
                driver.getPositionY()
        ));

        return responseDTO;
            }catch (EmptyResultDataAccessException e){
            throw new DriverNotFoundException(e);
        }

    }

}
