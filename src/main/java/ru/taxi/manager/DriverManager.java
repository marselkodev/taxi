package ru.taxi.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.taxi.dto.*;
import ru.taxi.exception.DriverNotFoundException;
import ru.taxi.exception.IncorrectDataException;
import ru.taxi.exception.OperationWithXmlException;
import ru.taxi.exception.OrderNotFoundException;
import ru.taxi.model.DriverModel;
import ru.taxi.model.OrderModel;
import ru.taxi.model.StatModel;
import ru.taxi.rowmapper.DriverRowMapper;
import ru.taxi.rowmapper.OrderRowMapper;
import ru.taxi.rowmapper.StatRowMapper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class DriverManager {
    private final NamedParameterJdbcTemplate template;
    private final DriverRowMapper driverRowMapper;
    private final OrderRowMapper orderRowMapper;
    private final StatRowMapper statsRowMapper;
    private final String PATH_TO_STATS = "C:" + File.separator +
            "Users" + File.separator + "marce" + File.separator +
            "Documents" + File.separator + "DriverStats" + File.separator;

    public DriverGetAllResponseDTO getAll() {
        final List<DriverModel> drivers = template.query(
                //language=PostgreSQL
                """
                        SELECT id, name, phone_number, photo_url, rating, license, car_name, car_number, car_color FROM drivers
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
                    driver.getCarColor()
            ));
        }
        return responseDTO;
    }

    public DriverGetByIdResponseDTO getById(long id) {
        if (id == 0) {
            throw new DriverNotFoundException("0 is not allowed");
        }
        try {
            final DriverModel driver = template.queryForObject(
                    //language=PostgreSQL
                    """
                            SELECT id, name, phone_number, photo_url, rating, license, car_name, car_number, car_color FROM drivers
                            WHERE id = :id AND removed = FALSE
                            """,
                    Map.of("id", id),
                    driverRowMapper
            );
            return new DriverGetByIdResponseDTO(new DriverGetByIdResponseDTO.Driver(
                    driver.getId(),
                    driver.getName(),
                    driver.getPhoneNumber(),
                    driver.getPhotoUrl(),
                    driver.getRating(),
                    driver.isLicense(),
                    driver.getCarName(),
                    driver.getCarNumber(),
                    driver.getCarColor()
            ));
        } catch (EmptyResultDataAccessException e) {
            throw new DriverNotFoundException("driver not found");
        }
    }

    public DriverSaveResponseDTO save(DriverSaveRequestDTO requestDTO) {
        if (requestDTO.getId() == 0) {
            return create(requestDTO);
        }
        return update(requestDTO);
    }

    private DriverSaveResponseDTO create(DriverSaveRequestDTO requestDTO) {
        if (!validatePhone(requestDTO.getPhoneNumber())) {
            throw new IncorrectDataException("phone number is incorrect");
        }

        final DriverModel driver = template.queryForObject(
                //language=PostgreSQL
                """
                                        INSERT INTO drivers (name, phone_number, photo_url, license, car_name, car_number, car_color)
                                        VALUES (:name, :phone_number, :photo_url, :license, :car_name, :car_number, :car_color)
                                        RETURNING id, name, phone_number, photo_url, rating, license, car_name, car_number, car_color
                        """,
                Map.of("name", requestDTO.getName(),
                        "phone_number", requestDTO.getPhoneNumber(),
                        "photo_url", requestDTO.getPhotoUrl(),
                        "license", requestDTO.isLicense(),
                        "car_name", requestDTO.getCarName(),
                        "car_number", requestDTO.getCarNumber(),
                        "car_color", requestDTO.getCarColor()),
                driverRowMapper
        );

        return new DriverSaveResponseDTO(new DriverSaveResponseDTO.Driver(
                driver.getId(),
                driver.getName(),
                driver.getPhoneNumber(),
                driver.getPhotoUrl(),
                driver.getRating(),
                driver.getCarName(),
                driver.getCarNumber(),
                driver.getCarColor()
        ));
    }

    private boolean validatePhone(String phoneNumber) {
        String pattern = "^\\d{11}$";
        return Pattern.matches(pattern, phoneNumber);
    }

    private DriverSaveResponseDTO update(DriverSaveRequestDTO requestDTO) {
        final DriverGetByIdResponseDTO.Driver beforeEdit = getById(requestDTO.getId()).getDriver();
        if (beforeEdit.getId() == 0) {
            throw new DriverNotFoundException("driver not found");
        }
        try {
            final DriverModel driver = template.queryForObject(
                    //language=PostgreSQL
                    """
                                            UPDATE drivers SET name = :name, phone_number = :phone_number, photo_url = :photo_url, license = :license, car_name = :car_name, car_number = :car_number, car_color = :car_color
                                            WHERE id = :id AND removed = FALSE
                                            RETURNING id, name, phone_number, photo_url, rating, license, car_name, car_number, car_color
                            """,
                    Map.of("id", requestDTO.getId(),
                            "name", requestDTO.getName() != null ? requestDTO.getName() : beforeEdit.getName(),
                            "phone_number", requestDTO.getPhoneNumber() != null && validatePhone(requestDTO.getPhoneNumber()) ? requestDTO.getPhoneNumber() : beforeEdit.getPhoneNumber(),
                            "photo_url", requestDTO.getPhotoUrl() != null ? requestDTO.getPhotoUrl() : beforeEdit.getPhotoUrl(),
                            "license", requestDTO.isLicense(),
                            "car_name", requestDTO.getCarName() != null ? requestDTO.getCarName() : beforeEdit.getCarName(),
                            "car_number", requestDTO.getCarNumber() != null ? requestDTO.getCarNumber() : beforeEdit.getCarNumber(),
                            "car_color", requestDTO.getCarColor() != null ? requestDTO.getCarColor() : beforeEdit.getCarColor()),
                    driverRowMapper
            );

            return new DriverSaveResponseDTO(new DriverSaveResponseDTO.Driver(
                    driver.getId(),
                    driver.getName(),
                    driver.getPhoneNumber(),
                    driver.getPhotoUrl(),
                    driver.getRating(),
                    driver.getCarName(),
                    driver.getCarNumber(),
                    driver.getCarColor()
            ));
        } catch (EmptyResultDataAccessException e) {
            throw new DriverNotFoundException("driver not found");
        }
    }

    public void removeById(long id) {
        if (id == 0) {
            throw new DriverNotFoundException("0 is not allowed");
        }
        //language=PostgreSQL
        final int removed = template.update("""
                        UPDATE drivers SET removed = TRUE
                        WHERE id = :id
                        """,
                Map.of("id", id));
        if (removed == 0) {
            throw new DriverNotFoundException("no driver removed");
        }
    }

    public OrderGetAcceptResponseDTO getAccept() {
        final List<OrderModel> orders = template.query(
                //language=PostgreSQL
                """
                        SELECT id, address_from, address_to, driver_comment, baby_chair, choice_of_car_class, distance, duration, price, accept FROM orders
                        WHERE accept = TRUE
                        ORDER BY id
                        LIMIT 100
                        """,
                orderRowMapper
        );

        final OrderGetAcceptResponseDTO responseDTO = new OrderGetAcceptResponseDTO(new ArrayList<>(orders.size()));
        for (OrderModel order : orders) {
            responseDTO.getOrders().add(new OrderGetAcceptResponseDTO.Order(
                    order.getId(),
                    order.getAddressFrom(),
                    order.getAddressTo(),
                    order.getDriverComment(),
                    order.isBabyChair(),
                    order.getChoiceOfCarClass(),
                    order.getDistance(),
                    order.getDuration(),
                    order.getPrice(),
                    order.isAccept()
            ));
        }
        return responseDTO;
    }

    public DriverGetByIdResponseDTO acceptDriver(long id, long driverId) {
        if (id == 0) {
            throw new OrderNotFoundException("0 is not allowed");
        }
        if (driverId == 0) {
            throw new DriverNotFoundException("0 is not allowed");
        }
        //language=PostgreSQL
        final int acceptDriver = template.update("""
                        UPDATE orders SET accept_driver = TRUE, driver_id = :driver_id
                        WHERE id = :id
                        """,
                Map.of("id", id,
                        "driver_id", driverId));
        if (acceptDriver == 0) {
            throw new OrderNotFoundException("no order accept");
        }

        final DriverGetByIdResponseDTO driver = getById(driverId);

        return new DriverGetByIdResponseDTO(new DriverGetByIdResponseDTO.Driver(
                driver.getDriver().getId(),
                driver.getDriver().getName(),
                driver.getDriver().getPhoneNumber(),
                driver.getDriver().getPhotoUrl(),
                driver.getDriver().getRating(),
                driver.getDriver().isLicense(),
                driver.getDriver().getCarName(),
                driver.getDriver().getCarNumber(),
                driver.getDriver().getCarColor()
        ));
    }

    public void completeOrder(long id) {
        if (id == 0) {
            throw new OrderNotFoundException("0 is not allowed");
        }
        //language=PostgreSQL
        final int completeOrder = template.update("""
                        UPDATE orders SET complete_order = TRUE
                        WHERE id = :id
                        """,
                Map.of("id", id));

        if (completeOrder == 0) {
            throw new OrderNotFoundException("no order completed");
        }
    }

    public StatsGetByIdResponseDTO getStatsById(long driverId, boolean saveToXml) {
        if (driverId == 0) {
            throw new DriverNotFoundException("0 is not allowed");
        }
        final List<StatModel> stats = template.query(
                //language=PostgreSQL
                """
                        SELECT baby_chair, distance, duration, price FROM orders
                        WHERE complete_order = TRUE AND driver_id = :driver_id
                        """,
                Map.of("driver_id", driverId),
                statsRowMapper
        );
        StatsGetByIdResponseDTO responseDTO = new StatsGetByIdResponseDTO(0, 0, 0, 0, 0, 0);
        for (StatModel stat : stats) {
            if (stat.isBabyChair()) {
                responseDTO.setCountWithBabyChair(responseDTO.getCountWithBabyChair() + 1);
            } else {
                responseDTO.setCountWithoutBabyChair(responseDTO.getCountWithoutBabyChair() + 1);
            }
            responseDTO.setCountDistance(responseDTO.getCountDistance() + stat.getDistance());
            responseDTO.setCountDuration(responseDTO.getCountDuration() + stat.getDuration());
            responseDTO.setCountPrice(responseDTO.getCountPrice() + stat.getPrice());
        }
        responseDTO.setCountOrders(stats.size());
        if (saveToXml) {
            saveToXml(driverId, responseDTO);
        }
        return responseDTO;
    }

    private void saveToXml(long id, StatsGetByIdResponseDTO stat) {
        Path dir = Paths.get(PATH_TO_STATS + id);
        if (!dir.toFile().exists()) {
            try {
                Files.createDirectory(dir);
                Files.createFile(Paths.get(dir.toFile().getAbsolutePath() + File.separator + "stat.xml"));
            } catch (IOException e) {
                throw new OperationWithXmlException("unable to create stat path");
            }
        }
        Document doc = generateStat(id, stat);
        if (doc != null) {
            try (FileOutputStream output = new FileOutputStream(PATH_TO_STATS + id + File.separator + "stat.xml")) {
                writeXml(doc, output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Document generateStat(long id, StatsGetByIdResponseDTO stat) {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Stats");
            rootElement.setAttribute("DriverId", String.valueOf(id));
            doc.appendChild(rootElement);

            Element orders = doc.createElement("Orders");
            rootElement.appendChild(orders);

            Element totalOrders = doc.createElement("TotalOrders");
            totalOrders.setTextContent(String.valueOf(stat.getCountOrders()));
            orders.appendChild(totalOrders);

            Element ordersWithBabyChair = doc.createElement("OrdersWithBabyChair");
            ordersWithBabyChair.setTextContent(String.valueOf(stat.getCountWithBabyChair()));
            orders.appendChild(ordersWithBabyChair);

            Element ordersWithoutBabyChair = doc.createElement("OrdersWithoutBabyChair");
            ordersWithoutBabyChair.setTextContent(String.valueOf(stat.getCountWithoutBabyChair()));
            orders.appendChild(ordersWithoutBabyChair);

            Element salary = doc.createElement("Salary");
            rootElement.appendChild(salary);

            Element price = doc.createElement("price");
            price.setTextContent(String.valueOf(stat.getCountPrice()));
            salary.appendChild(price);

            Element totalDistanceAndDuration = doc.createElement("TotalDistanceAndDuration");
            rootElement.appendChild(totalDistanceAndDuration);

            Element distance = doc.createElement("distance");
            distance.setTextContent(String.valueOf(stat.getCountDistance()));
            totalDistanceAndDuration.appendChild(distance);

            Element duration = doc.createElement("duration");
            duration.setTextContent(String.valueOf(stat.getCountDuration()));
            totalDistanceAndDuration.appendChild(duration);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private static void writeXml(Document doc, OutputStream output) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(output);
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}