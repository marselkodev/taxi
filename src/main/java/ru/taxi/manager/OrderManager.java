package ru.taxi.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.taxi.dto.OrderSaveRequestDTO;
import ru.taxi.dto.OrderSaveResponseDTO;
import ru.taxi.exception.OrderNotFoundException;
import ru.taxi.exception.RouteCannotCalculateException;
import ru.taxi.model.OrderModel;
import ru.taxi.model.RouteInfo;
import ru.taxi.rowmapper.OrderRowMapper;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderManager {
    private final NamedParameterJdbcTemplate template;
    private final OrderRowMapper orderRowMapper;
    private final GoogleMapManager mapManager;
    private static final int PRICE_FOR_ONE_KM = 9;
    private static final int PRICE_FOR_ONE_MINUTE = 3;
    private static final int LANDING = 40;

    public OrderSaveResponseDTO create(OrderSaveRequestDTO requestDTO) {
        log.info("Method create starting with param orderSaveRequestDTO = {}", requestDTO);
        final List<RouteInfo> infos = mapManager.query(requestDTO.getAddressFrom(), requestDTO.getAddressTo());
        if (infos.size() < 1) {
            log.warn("Route not found");
            throw new RouteCannotCalculateException("route can not calculate");
        }

        final RouteInfo info = infos.get(0);
        double distance = info.getDistance() / 1000;
        double duration = info.getDuration() / 60;

        double price = ((distance * PRICE_FOR_ONE_KM) + (duration * PRICE_FOR_ONE_MINUTE) + LANDING);

        final OrderModel order = template.queryForObject(
                //language=PostgreSQL
                """
                                        INSERT INTO orders (address_from, address_to, driver_comment, baby_chair, choice_of_car_class, distance, duration, price)
                                        VALUES (:address_from, :address_to, :driver_comment, :baby_chair, :choice_of_car_class, :distance, :duration, :price)
                                        RETURNING id, address_from, address_to, driver_comment, baby_chair, choice_of_car_class, distance, duration, price, accept
                        """,
                Map.of(
                        "address_from", requestDTO.getAddressFrom(),
                        "address_to", requestDTO.getAddressTo(),
                        "driver_comment", requestDTO.getDriverComment() == null ? "No comment" : requestDTO.getDriverComment(),
                        "baby_chair", requestDTO.isBabyChair(),
                        "choice_of_car_class", requestDTO.getChoiceOfCarClass() == 0 ? 1 : requestDTO.getChoiceOfCarClass(),
                        "distance", distance,
                        "duration", duration,
                        "price", price
                ),
                orderRowMapper
        );
        log.info("Method create finished");

        return new OrderSaveResponseDTO(new OrderSaveResponseDTO.Order(
                order.getId(),
                order.getAddressFrom(),
                order.getAddressTo(),
                order.getDriverComment(),
                order.isBabyChair(),
                distance,
                duration,
                price,
                order.isAccept())
        );
    }

    public void accept(long id) {
        log.info("Method accept starting with param id = {}", id);
        if (id == 0) {
            log.warn("When using the method accept with id = {} is not allowed", id);
            throw new OrderNotFoundException("0 is not allowed");
        }
        //language=PostgreSQL
        final int accept = template.update("""
                        UPDATE orders SET accept = TRUE
                        WHERE id = :id
                        """,
                Map.of("id", id));
        if (accept == 0) {
            log.warn("Failed to accept because no order with id {} was found", id);
            throw new OrderNotFoundException("no order accept");
        }
        log.info("Method accept finished");
    }
}
