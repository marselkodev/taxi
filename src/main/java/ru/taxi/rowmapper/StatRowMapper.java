package ru.taxi.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.taxi.model.StatModel;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StatRowMapper implements RowMapper<StatModel> {
    @Override
    public StatModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new StatModel(
                rs.getBoolean("baby_chair"),
                rs.getDouble("distance"),
                rs.getDouble("duration"),
                rs.getDouble("price")
        );
    }
}
