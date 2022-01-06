package ru.taxi.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.taxi.model.DriverModel;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DriverRowMapper implements RowMapper<DriverModel> {
    @Override
    public DriverModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DriverModel(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("phone_number"),
                rs.getString("photo_url"),
                rs.getFloat("rating"),
                rs.getBoolean("license"),
                rs.getString("car_Name"),
                rs.getString("car_number"),
                rs.getString("car_color"));
    }
}

