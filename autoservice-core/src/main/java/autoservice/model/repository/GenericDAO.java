package autoservice.model.repository;

import autoservice.model.entities.Identifiable;
import autoservice.model.exceptions.DBException;
import lombok.extern.slf4j.Slf4j;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Slf4j

public abstract class GenericDAO<T extends Identifiable> {

    protected Connection connection = DBConnection.getInstance().getConnection();

    public abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    public abstract String getTableName();

    public abstract void setPreparedStatementForInsert(PreparedStatement ps, T entity) throws SQLException;

    public abstract void setPreparedStatementForUpdate(PreparedStatement ps, T entity) throws SQLException;

    protected abstract String getInsertSQL();

    public abstract String getUpdateSQL();


    public void save(T entity) {
        log.info("Saving entity {} to database", entity.getClass().getSimpleName());
        String sql = getInsertSQL();
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementForInsert(ps, entity);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            log.error("Error while saving entity {} to database", entity.getClass().getSimpleName(), e);
            throw new DBException("Error saving entity with id=" + entity.getId(), e);
        }
        log.info("Entity {} saved successfully to database with id={}", entity.getClass().getName(), entity.getId());
    }

    public void update(T entity) {
        String sql = getUpdateSQL();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setPreparedStatementForUpdate(ps, entity);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while updating entity {} to database", entity.getClass().getSimpleName(), e);
            throw new DBException("Error updating entity with id=" + entity.getId(), e);
        }
    }



    public void delete(long id) {
        log.info("Deleting entity with id={} from database {}", id, getTableName());
        String sql = "DELETE FROM " + getTableName() + " WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while deleting entity with id={} from database {}", id, getTableName(), e);
            throw new DBException("Error deleting entity with id=" + id, e);
        }
        log.info("Entity with id={} deleted successfully from database {}", id, getTableName());
    }

    public void deleteAll() {
        String sql = "DELETE FROM " + getTableName();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while deleting all entities from database {}", getTableName(), e);
            throw new DBException("Error deleting all entities", e);
        }
    }

    public T findById(long id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            log.error("Error while finding entity with id={} from database {}", id, getTableName(), e);
            throw new DBException("Error finding entity with id=" + id, e);
        }
        return null;
    }

    public List<T> findAll() {
        String sql = "SELECT * FROM " + getTableName();
        List<T> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            log.error("Error while finding all entities from database {}", getTableName(), e);
            throw new DBException("Error finding all entities", e);
        }
        return list;
    }
}