package autoservice.model.repository;

import autoservice.model.entities.Master;
import autoservice.model.exceptions.DBException;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class MasterDAO extends GenericDAO<Master>{
    @Override
    public Master mapResultSetToEntity(ResultSet rs) throws SQLException {
        Master m = new Master();
        m.setId(rs.getLong("id"));
        m.setName(rs.getString("name"));
        m.setSalary(rs.getBigDecimal("salary"));
        return m;
    }

    @Override
    public String getTableName() {
        return "master";
    }

    @Override
    public void setPreparedStatementForInsert(PreparedStatement ps, Master entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setBigDecimal(2, entity.getSalary());
    }

    @Override
    public void setPreparedStatementForUpdate(PreparedStatement ps, Master entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setBigDecimal(2, entity.getSalary());
        ps.setLong(3, entity.getId());
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO master (name, salary) VALUES (?, ?)";
    }

    @Override
    public String getUpdateSQL() {
        return "UPDATE master SET name=?, salary=? WHERE id=?";
    }

    public List<Master> mastersSortByName(){
        List<Master> sortedMasters = new ArrayList<>();
        String sql = "SELECT * FROM master ORDER BY name";
        try(PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                sortedMasters.add(mapResultSetToEntity(rs));
            }

        } catch (SQLException e) {
            log.error("Error getting sorted masters by name", e);
            throw new DBException("Error getting sorted masters by name", e);
        }
        return sortedMasters;
    }
}
