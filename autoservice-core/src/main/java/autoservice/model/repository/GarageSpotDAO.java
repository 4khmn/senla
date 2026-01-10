package autoservice.model.repository;

import autoservice.model.entities.GarageSpot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GarageSpotDAO extends GenericDAO<GarageSpot> {
    @Override
    public GarageSpot mapResultSetToEntity(ResultSet rs) throws SQLException {
        GarageSpot g = new GarageSpot();
        g.setId(rs.getLong("id"));
        g.setSize(rs.getDouble("size"));
        g.setHasLift(rs.getBoolean("has_lift"));
        g.setHasPit(rs.getBoolean("has_pit"));
        return g;
    }

    @Override
    public String getTableName() {
        return "garage_spot";
    }

    @Override
    public void setPreparedStatementForInsert(PreparedStatement ps, GarageSpot entity) throws SQLException {
        ps.setDouble(1, entity.getSize());
        ps.setBoolean(2, entity.isHasLift());
        ps.setBoolean(3, entity.isHasPit());
    }

    @Override
    public void setPreparedStatementForUpdate(PreparedStatement ps, GarageSpot entity) throws SQLException {
        ps.setDouble(1, entity.getSize());
        ps.setBoolean(2, entity.isHasLift());
        ps.setBoolean(3, entity.isHasPit());
        ps.setLong(4, entity.getId());
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO garage_spot (size, has_lift, has_pit) VALUES (?, ?, ?)";
    }

    @Override
    public String getUpdateSQL() {
        return "UPDATE garage_spot SET size=?, has_lift=?, has_pit=? WHERE id=?";
    }
}