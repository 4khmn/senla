package autoservice.model.repository;

import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.entities.TimeSlot;
import autoservice.model.enums.OrderSortField;
import autoservice.model.enums.OrderStatus;
import autoservice.model.exceptions.DBException;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
@Slf4j
public class OrderDAO extends GenericDAO<Order> {
    @Override
    public Order mapResultSetToEntity(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setDescription(rs.getString("description"));
        o.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        o.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        o.setPrice(rs.getBigDecimal("price"));
        o.setOrderStatus(OrderStatus.valueOf(rs.getString("order_status")));
        o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        long masterId = rs.getLong("master_id");
        long garageSpotId = rs.getLong("garage_spot_id");

        MasterDAO masterDAO = new MasterDAO();
        GarageSpotDAO garageSpotDAO = new GarageSpotDAO();

        o.setMaster(masterDAO.findById(masterId));
        o.setGarageSpot(garageSpotDAO.findById(garageSpotId));
        return o;
    }

    @Override
    public String getTableName() {
        return "orders";
    }

    @Override
    public void setPreparedStatementForInsert(PreparedStatement ps, Order entity) throws SQLException {
        ps.setString(1, entity.getDescription());
        ps.setTimestamp(2, Timestamp.valueOf(entity.getStartTime()));
        ps.setTimestamp(3, Timestamp.valueOf(entity.getEndTime()));
        ps.setBigDecimal(4, entity.getPrice());
        ps.setString(5, entity.getOrderStatus().name());
        ps.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt()));
        ps.setLong(7, entity.getMaster().getId());       // FK
        ps.setLong(8, entity.getGarageSpot().getId());   // FK
    }

    @Override
    public void setPreparedStatementForUpdate(PreparedStatement ps, Order entity) throws SQLException {
        ps.setString(1, entity.getDescription());
        ps.setLong(2, entity.getMaster().getId());
        ps.setLong(3, entity.getGarageSpot().getId());
        ps.setTimestamp(4, Timestamp.valueOf(entity.getStartTime()));
        ps.setTimestamp(5, Timestamp.valueOf(entity.getEndTime()));
        ps.setString(6, entity.getOrderStatus().name());
        ps.setBigDecimal(7, entity.getPrice());
        ps.setLong(8, entity.getId());
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO orders (description, start_time, end_time, price, order_status, created_at, master_id, garage_spot_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public String getUpdateSQL() {
        return "UPDATE orders SET description=?, master_id=?, garage_spot_id=?, start_time=?, end_time=?, order_status=?, price=? WHERE id=?";
    }
    //для методов вроде shiftOrder важная правильная последовательность заказов
    public List<Order> findAll() {
        String sql = "SELECT * FROM " + getTableName() + " ORDER BY start_time";
        List<Order> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            log.error("Error getting all orders from database ", getTableName(), e);
            throw new DBException("Error finding all entities", e);
        }
        return list;
    }
    public TreeSet<TimeSlot> findTimeSlotsByGarageSpot(long garageSpotId) {
        String sql = """
        SELECT start_time, end_time
        FROM orders
        WHERE garage_spot_id = ?
          AND order_status NOT IN ('CANCELLED')
    """;

        TreeSet<TimeSlot> slots = new TreeSet<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, garageSpotId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    slots.add(new TimeSlot(
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            log.error("Error getting time slots by garage spot from database {}", getTableName(), e);
            throw new DBException("Error finding time slots by garageSpot with id=" + garageSpotId, e);
        }

        return slots;
    }

    public TreeSet<TimeSlot> findTimeSlotsByMaster(long masterId) {
        String sql = """
        SELECT start_time, end_time
        FROM orders
        WHERE master_id = ?
          AND order_status NOT IN ('CANCELLED')
    """;

        TreeSet<TimeSlot> slots = new TreeSet<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, masterId);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    slots.add(new TimeSlot(
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            log.error("Error getting time slots by master from database {}", getTableName(), e);
            throw new DBException("Error finding time slots by master with id=" + masterId, e);
        }

        return slots;
    }

    public Order getOrderByMaster(Master master) {
        log.info("Getting orders by master with id {}", master.getId());
        String sql = "SELECT * FROM orders WHERE master_id = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, master.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToEntity(rs);
                    log.info("Order successfully founded with id={}", order.getId());
                    return order;
                }
            }
        } catch (SQLException e) {
            log.error("Error getting orders by master with id={} from database {}", master.getId(), getTableName(), e);
            throw new DBException("Error getting order by master with id=" + master.getId(), e);
        }
        return null;
    }

    public List<Order> ordersSortByPrice(boolean onlyActive) {
        return getOrdersSorted(OrderSortField.PRICE, onlyActive);
    }

    public List<Order> ordersSortByEndDate(boolean onlyActive) {
        return getOrdersSorted(OrderSortField.END_TIME, onlyActive);
    }

    public List<Order> ordersSortByCreationDate(boolean onlyActive) {
        return getOrdersSorted(OrderSortField.CREATED_AT, onlyActive);
    }

    public List<Order> ordersSortByStartDate() {
        return getOrdersSorted(OrderSortField.START_TIME, false);
    }
    //общий метод для сортировки по параметру
    public List<Order> getOrdersSorted(OrderSortField sortField, boolean onlyActive) {

        List<Order> sortedOrders = new ArrayList<>();

        String baseSql =
                "SELECT * FROM orders " +
                        "WHERE order_status NOT IN ('CANCELLED', 'CLOSED') ";

        String sql = baseSql +
                (onlyActive
                        ? "AND now() BETWEEN start_time AND end_time "
                        : "") +
                "ORDER BY " + sortField.getColumn();

        try (PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                sortedOrders.add(mapResultSetToEntity(rs));
            }

        } catch (SQLException e) {
            log.error("Error getting orders by sorting from database {}", getTableName(), e);
            throw new DBException("Error getting sorted orders", e);
        }

        return sortedOrders;
    }

    public List<Order> ordersSortByTimeFrameByCreationDate(LocalDateTime start, LocalDateTime end) {
        return getOrdersByTimeFrameSorted(start, end, OrderSortField.CREATED_AT);
    }

    public List<Order> ordersSortByTimeFrameByEndDate(LocalDateTime start, LocalDateTime end) {
        return getOrdersByTimeFrameSorted(start, end, OrderSortField.END_TIME);
    }

    public List<Order> ordersSortByTimeFrameByPrice(LocalDateTime start, LocalDateTime end) {
        return getOrdersByTimeFrameSorted(start, end, OrderSortField.PRICE);
    }
    //общий метод для сортировки по отрезку времени и параметру
    public List<Order> getOrdersByTimeFrameSorted(LocalDateTime start, LocalDateTime end, OrderSortField sortField) {
        List<Order> sortedOrders = new ArrayList<>();

        String sql = "SELECT * FROM orders WHERE start_time <= ? AND end_time >= ? ORDER BY " + sortField.getColumn();


        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(start));
            ps.setTimestamp(2, Timestamp.valueOf(end));

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    sortedOrders.add(mapResultSetToEntity(rs));
                }
            }

        } catch (SQLException e) {
            log.error("Error getting orders by sorting from database {}", getTableName(), e);
            throw new DBException("Error getting sorted by time frame orders", e);
        }

        return sortedOrders;
    }
}