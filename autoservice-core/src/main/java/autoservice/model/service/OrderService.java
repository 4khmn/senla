package autoservice.model.service;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.enums.ActiveOrdersSortEnum;
import autoservice.model.enums.OrderStatus;
import autoservice.model.enums.OrdersSortByTimeFrameEnum;
import autoservice.model.enums.OrdersSortEnum;
import autoservice.model.exceptions.OrderException;
import autoservice.model.repository.OrderDAO;
import autoservice.model.utils.HibernateUtil;
import config.annotation.Component;
import config.annotation.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderService {
    private transient OrderDAO orderDAO;

    @Inject
    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }


    //4 список заказов
    public List<Order> ordersSort(OrdersSortEnum decision) {
        log.info("Sorting orders by decision={}", decision);
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Order> sortedOrders;
            switch (decision) {
                case BY_CREATION_DATE:
                    //по дате подачи
                    sortedOrders = orderDAO.ordersSortByCreationDate(false);
                    transaction.commit();
                    break;
                case BY_END_DATE:
                    //дата выполнения
                    sortedOrders = orderDAO.ordersSortByEndDate(false);
                    transaction.commit();
                    break;
                case BY_START_DATE:
                    //дата планируемого начала выполнения
                    sortedOrders = orderDAO.ordersSortByStartDate();
                    transaction.commit();
                    break;
                case BY_PRICE:
                    //по цене
                    sortedOrders = orderDAO.ordersSortByPrice(false);
                    transaction.commit();
                    break;
                default:
                    //error
                    log.error("Invalid decision={}", decision);
                    throw new IllegalArgumentException("Неизвестный тип: " + decision);
            }
            log.info("Orders successfully sorted by decision={}", decision);
            return sortedOrders;
        } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                log.error("Error sorting orders by decision={}", decision, e);
                throw new OrderException("Impossible to sort orders", e);
            }
    }

    //4
    public List<Order> activeOrdersSort(ActiveOrdersSortEnum decision) {
        log.info("Sorting active orders by decision={}", decision);
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Order> sortedOrders;
            switch (decision) {
                case BY_CREATION_DATE:
                    //по дате подачи
                    sortedOrders = orderDAO.ordersSortByCreationDate(true);
                    transaction.commit();
                    break;
                case BY_END_DATE:
                    //по дате выполнения
                    sortedOrders = orderDAO.ordersSortByEndDate(true);
                    transaction.commit();
                    break;
                case BY_PRICE:
                    //по цене
                    sortedOrders = orderDAO.ordersSortByPrice(true);
                    transaction.commit();
                    break;
                default:
                    //error
                    log.error("Invalid decision={}", decision);
                    throw new IllegalArgumentException("Неизвестный тип: " + decision);
            }
            log.info("Active orders successfully sorted by decision={}", decision);
            return sortedOrders;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error sorting active orders by decision={}", decision, e);
            throw new OrderException("Impossible to sort active orders", e);
        }
    }

    //4
    public Order getOrderByMaster(Master master) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Order orderByMaster = orderDAO.getOrderByMaster(master);
            transaction.commit();
            return orderByMaster;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error getting order by master with id={}", master.getId(), e);
            throw new OrderException("Impossible to get order by master", e);
        }
    }

    //4
    public List<Order> ordersSortByTimeFrame(LocalDateTime start, LocalDateTime end, OrdersSortByTimeFrameEnum decision) {
        log.info("Sorting orders by time frame by decision={}", decision);
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Order> ordersAtCurrentTime;
            switch (decision) {
                case BY_CREATION_DATE:
                    //по дате подачи
                    ordersAtCurrentTime = orderDAO.ordersSortByTimeFrameByCreationDate(start, end);
                    transaction.commit();
                    break;
                case BY_END_DATE:
                    //по дате выполнения
                    ordersAtCurrentTime = orderDAO.ordersSortByTimeFrameByEndDate(start, end);
                    transaction.commit();
                    break;
                case BY_PRICE:
                    //по цене
                    ordersAtCurrentTime = orderDAO.ordersSortByTimeFrameByPrice(start, end);
                    transaction.commit();
                    break;
                default:
                    //error
                    log.error("Invalid decision={}", decision);
                    throw new IllegalArgumentException("Неизвестный тип: " + decision);
            }
            log.info("Orders successfully sorted by time frame [{} - {}] by decision={}", start, end, decision);
            return ordersAtCurrentTime;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error sorting orders by time frame [{} - {}] by decision={}", start, end, decision, e);
            throw new OrderException("Impossible to sort orders by time frame", e);
        }
    }


    public long addOrderFromImport(String description, Master master, GarageSpot garageSpot, LocalDateTime startTime, LocalDateTime endtime, BigDecimal price) {
        Order order = new Order(description, master, garageSpot, startTime, endtime, price);
        orderDAO.save(order);
        return order.getId();
    }

    public long addOrder(String description, Master master, GarageSpot garageSpot, LocalDateTime startTime, LocalDateTime endtime, BigDecimal price) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        Order order = new Order(description, master, garageSpot, startTime, endtime, price);
        try {
            transaction = session.beginTransaction();
            orderDAO.save(order);
            transaction.commit();
            return order.getId();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error creating new order", e);
            throw new OrderException("Impossible to create new order", e);
        }
    }


    public void update(Order order) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            orderDAO.update(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error updating order with id={}", order.getId(), e);
            throw new OrderException("Impossible to update order: " + order.getId(), e);
        }
    }

    public void deleteOrder(long id) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            orderDAO.delete(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error deleting order with id={}", id, e);
            throw new OrderException("Impossible to delete order: " + id, e);
        }
    }

    public List<Order> getOrders() {
        List<Order> orders = orderDAO.findAll();
        for (var order : orders) {
            if (order.getEndTime().isBefore(LocalDateTime.now())) {
                order.setOrderStatus(OrderStatus.CLOSED);
            }
        }
        return orders;
    }

    public Order getOrderById(long id) {
        Order order = orderDAO.findById(id);
        if (order != null) {
            if (order.getEndTime().isBefore(LocalDateTime.now())) {
                order.setOrderStatus(OrderStatus.CLOSED);
            }
            return order;
        }
        return null;
    }

    public boolean closeOrder(long id) {
        log.info("Closing order with id={}", id);
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Order byId = orderDAO.findById(id);
            if (byId == null) {
                transaction.rollback();
                return false;
            }
            byId.setOrderStatus(OrderStatus.CLOSED);
            transaction.commit();
            log.info("Order with id={} successfully closed", id);
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error closing order with id={}", id, e);
            throw new OrderException("Impossible to close order with id=" + id, e);
        }
    }

    public boolean cancelOrder(long id) {
        log.info("Canceling order with id={}", id);
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Order byId = orderDAO.findById(id);
            if (byId == null) {
                transaction.rollback();
                return false;
            }

            byId.setOrderStatus(OrderStatus.CANCELLED);

            transaction.commit();
            log.info("Order with id={} successfully cancelled", id);
            return true;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error canceling order with id={}", id, e);
            throw new OrderException("Impossible to cancel order with id=" + id, e);
        }
    }
    //метод не используется
    public long findOrderByTimeByCurrentMaster(Master master, LocalDateTime date) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Order> orders = orderDAO.findAll();
            for (var v : orders) {
                if (v.getMaster().equals(master) && v.getStartTime().isEqual(date)) {
                    transaction.commit();
                    return v.getId();
                }
            }
        } catch (Exception e) {
            transaction.rollback();
        }
        return -1;
    }

    public boolean shiftOrder(long id, int durationToShiftInHours) {
        log.info("Shifting order with id={}", id);
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Order> orders = getOrders();
            Order conflictSource = null;
            int startIndex = -1;

            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getId() == id) {
                    conflictSource = orders.get(i);
                    startIndex = i;
                    break;
                }
            }
            if (conflictSource == null) {
                return false;
            }
            conflictSource.setEndTime(
                    conflictSource.getEndTime().plusHours(durationToShiftInHours)
            );
            for (int i = startIndex + 1; i < orders.size(); i++) {

                Order candidate = orders.get(i);

                if (!candidate.getStartTime().isBefore(conflictSource.getEndTime())) {
                    continue;
                }

                boolean sameMaster =
                        candidate.getMaster().getId() ==
                                conflictSource.getMaster().getId();

                boolean sameGarageSpot =
                        candidate.getGarageSpot().getId() ==
                                conflictSource.getGarageSpot().getId();

                if (!sameMaster && !sameGarageSpot) {
                    continue;
                }

                Duration shift =
                        Duration.between(
                                candidate.getStartTime(),
                                conflictSource.getEndTime()
                        );

                candidate.setStartTime(
                        candidate.getStartTime().plus(shift)
                );
                candidate.setEndTime(
                        candidate.getEndTime().plus(shift)
                );

                conflictSource = candidate;
            }
            transaction.commit();
            log.info("Order with id={} successfully shifted", id);
            return true;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error shifting order  with id={}", id, e);
            throw new OrderException("Impossible to shift order with id=" + id, e);
        }
    }

    public Long getOrdersCount() {
        return orderDAO.count();
    }
}