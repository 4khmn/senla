package autoservice.model.repository;

import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.entities.TimeSlot;
import autoservice.model.enums.OrderSortField;
import autoservice.model.enums.OrderStatus;
import autoservice.model.exceptions.DBException;
import autoservice.model.utils.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;
@Repository
@Slf4j
public class OrderRepository extends HibernateAbstractDAO<Order, Long> {

    public OrderRepository() {
        super(Order.class);
    }
    public List<Order> findAll() {
        try {
            String hql = "FROM Order o ORDER BY o.startTime";
            Session session = HibernateUtil.getSession();
            Query<Order> query = session.createQuery(hql, Order.class);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Error getting all orders", e);
            throw new DBException("Error getting all orders", e);
        }
    }

    @Override
    public Long count() {
        String hql = "select count(*) from Order";
        Session session = HibernateUtil.getSession();
        Query<Long> query = session.createQuery(hql, Long.class);
        return query.getSingleResult();
    }
    public List<Object[]> findTimeSlotsForAllGarageSpots() {
        try {
            Session session = HibernateUtil.getSession();
            String hql = """
            SELECT o.garageSpot.id, o.startTime, o.endTime
            FROM Order o
            WHERE o.orderStatus <> :cancelled
            """;
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("cancelled", OrderStatus.CANCELLED);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Error finding time slots for all garage spots", e);
            throw new DBException("Error finding time slots for all garage spots", e);
        }
    }



    public TreeSet<TimeSlot> findTimeSlotsByGarageSpot(long garageSpotId) {
        try {
            String hql = """
                SELECT new autoservice.model.entities.TimeSlot(o.startTime, o.endTime)
                FROM Order o
                WHERE o.garageSpot.id = :garageSpotId
                AND o.orderStatus <> :cancelled
            """;

            Session session = HibernateUtil.getSession();

            Query<TimeSlot> query = session.createQuery(hql, TimeSlot.class);
            query.setParameter("garageSpotId", garageSpotId);
            query.setParameter("cancelled", OrderStatus.CANCELLED);

            List<TimeSlot> result = query.getResultList();

            return new TreeSet<>(result);

        } catch (Exception e) {
            log.error("Error finding time slots by garageSpot with id={}", garageSpotId, e);
            throw new DBException( "Error finding time slots by garageSpot with id=" + garageSpotId, e);
        }
    }


    public List<Object[]> findTimeSlotsForAllMasters() {
        try {
            Session session = HibernateUtil.getSession();
            String hql = """
            SELECT o.master.id, o.startTime, o.endTime
            FROM Order o
            WHERE o.orderStatus <> :cancelled
            """;
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("cancelled", OrderStatus.CANCELLED);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Error finding time slots for all masters", e);
            throw new DBException("Error finding time slots for all masters", e);
        }
    }

    public TreeSet<TimeSlot> findTimeSlotsByMaster(long masterId) {
        try {
            String hql = """
                SELECT new autoservice.model.entities.TimeSlot(o.startTime, o.endTime)
                FROM Order o
                WHERE o.master.id = :masterId
                AND o.orderStatus <> :cancelled
                """;
            Session session = HibernateUtil.getSession();
            Query<TimeSlot> query = session.createQuery(hql, TimeSlot.class);
            query.setParameter("masterId", masterId);
            query.setParameter("cancelled", OrderStatus.CANCELLED);
            List<TimeSlot> result = query.getResultList();
            return new TreeSet<>(result);
        } catch (Exception e) {
            log.error("Error getting time slots by master from database orders", e);
            throw new DBException("Error finding time slots by master with id=" + masterId, e);
        }
    }



    public Order getOrderByMaster(Master master) {
        try {
            String hql = "FROM Order o WHERE o.master.id = :masterId";
            Session session = HibernateUtil.getSession();
            Query<Order> query = session.createQuery(hql, Order.class);
            query.setParameter("masterId", master.getId());
            query.setMaxResults(1);
            return query.uniqueResult();

        } catch (Exception e) {
            log.error("Error getting orders by master with id={} from database orders", master.getId(), e);
            throw new DBException("Error getting order by master with id=" + master.getId(), e);
        }
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
        try {
            StringBuilder hql = new StringBuilder(
                    "FROM Order o WHERE o.orderStatus NOT IN (:closedStatuses) "
            );
            if (onlyActive) {
                hql.append(
                        "AND :now BETWEEN o.startTime AND o.endTime "
                );
            }
            hql.append("ORDER BY o.").append(sortField.getFieldName());

            Session session = HibernateUtil.getSession();

            Query<Order> query = session.createQuery(hql.toString(), Order.class);

            query.setParameter(
                    "closedStatuses",
                    List.of(OrderStatus.CANCELLED, OrderStatus.CLOSED)
            );

            if (onlyActive) {
                query.setParameter("now", LocalDateTime.now());
            }

            return query.getResultList();

        } catch (Exception e) {
            log.error("Error getting sorted orders", e);
            throw new DBException("Error getting sorted orders", e);
        }
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
        try {
            String hql = "FROM Order o WHERE o.startTime <= :start AND o.endTime >= :end ORDER BY o." + sortField.getFieldName();
            Session session = HibernateUtil.getSession();
            Query<Order> query = session.createQuery(hql, Order.class);
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Error getting sorted orders by time frame", e);
            throw new DBException("Error getting sorted orders by time frame", e);
        }
    }

}