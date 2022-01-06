package ru.taxi.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.taxi.model.OrderModel;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderRowMapper implements RowMapper<OrderModel> {
    @Override
    public OrderModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new OrderModel(
                rs.getLong("id"),
                rs.getString("address_from"),
                rs.getString("address_to"),
                rs.getString("driver_comment"),
                rs.getBoolean("baby_chair"),
                rs.getInt("choice_of_car_class"),
                rs.getDouble("distance"),
                rs.getDouble("duration"),
                rs.getDouble("price"),
                rs.getBoolean("accept")
        );
    }
}
