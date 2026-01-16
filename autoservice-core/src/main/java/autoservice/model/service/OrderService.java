package autoservice.model.service;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.enums.ActiveOrdersSortEnum;
import autoservice.model.enums.OrderStatus;
import autoservice.model.enums.OrdersSortByTimeFrameEnum;
import autoservice.model.enums.OrdersSortEnum;
import autoservice.model.exceptions.ImportException;
import autoservice.model.repository.DBConnection;
import autoservice.model.repository.OrderDAO;
import config.annotation.Component;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
public class OrderService {
    private OrderDAO orderDAO = new  OrderDAO();


    public OrderService() {}


    //4 список заказов
    public List<Order> ordersSort(OrdersSortEnum decision) {
        log.info("Sorting orders by decision={}", decision);
        List<Order> sortedOrders;
        switch (decision) {
            case BY_CREATION_DATE:
                //по дате подачи
                sortedOrders = orderDAO.ordersSortByCreationDate(false);
                break;
            case BY_END_DATE:
                //дата выполнения
                sortedOrders = orderDAO.ordersSortByEndDate(false);
                break;
            case BY_START_DATE:
                //дата планируемого начала выполнения
                sortedOrders = orderDAO.ordersSortByStartDate();
                break;
            case BY_PRICE:
                //по цене
                sortedOrders = orderDAO.ordersSortByPrice(false);
                break;
            default:
                //error
                log.error("Invalid decision={}", decision);
                throw new IllegalArgumentException("Неизвестный тип: " + decision);
        }
        log.info("Orders successfully sorted by decision={}", decision);
        return sortedOrders;
    }

    //4
    public List<Order> activeOrdersSort(ActiveOrdersSortEnum decision) {
        log.info("Sorting active orders by decision={}", decision);
        List<Order> sortedOrders;
        switch (decision) {
            case BY_CREATION_DATE:
                //по дате подачи
                sortedOrders = orderDAO.ordersSortByCreationDate(true);
                break;
            case BY_END_DATE:
                //по дате выполнения
                sortedOrders = orderDAO.ordersSortByEndDate(true);
                break;
            case BY_PRICE:
                //по цене
                sortedOrders = orderDAO.ordersSortByPrice(true);
                break;
            default:
                //error
                log.error("Invalid decision={}", decision);
                throw new IllegalArgumentException("Неизвестный тип: " + decision);
        }
        log.info("Active orders successfully sorted by decision={}", decision);
        return sortedOrders;
    }

    //4
    public Order getOrderByMaster(Master master){
        return orderDAO.getOrderByMaster(master);
    }

    //4
    public List<Order> ordersSortByTimeFrame(LocalDateTime start, LocalDateTime end, OrdersSortByTimeFrameEnum decision) {
        log.info("Sorting orders by time frame by decision={}", decision);
        List<Order> ordersAtCurrentTime;
        switch (decision) {
            case BY_CREATION_DATE:
                //по дате подачи
                ordersAtCurrentTime = orderDAO.ordersSortByTimeFrameByCreationDate(start, end);
                break;
            case BY_END_DATE:
                //по дате выполнения
                ordersAtCurrentTime = orderDAO.ordersSortByTimeFrameByEndDate(start, end);
                break;
            case BY_PRICE:
                //по цене
                ordersAtCurrentTime = orderDAO.ordersSortByTimeFrameByPrice(start, end);
                break;
            default:
                //error
                log.error("Invalid decision={}", decision);
                throw new IllegalArgumentException("Неизвестный тип: " + decision);
        }
        log.info("Orders successfully sorted by time frame [{} - {}] by decision={}", start, end, decision);
        return ordersAtCurrentTime;
    }


    public long addOrder(String description, Master master, GarageSpot garageSpot, LocalDateTime startTime, LocalDateTime endtime, BigDecimal price) {

        Order order = new Order(description, master, garageSpot, startTime, endtime, price);
        orderDAO.save(order);
        return order.getId();
        //System.out.println("Order #" + order.getId() + " was successfully added");
    }


    public void update(Order order){
        orderDAO.update(order);
    }

    public void deleteOrder(long id){
        orderDAO.delete(id);
    }

    public List<Order> getOrders() {
        List<Order> orders = orderDAO.findAll();
        for (var order: orders){
            if (order.getEndTime().isBefore(LocalDateTime.now())){
                order.setOrderStatus(OrderStatus.CLOSED);
                update(order);
            }
        }
        return orders;
    }

    public Order getOrderById(long id){
        Order order = orderDAO.findById(id);
        if (order != null) {
            if (order.getEndTime().isBefore(LocalDateTime.now())) {
                order.setOrderStatus(OrderStatus.CLOSED);
                update(order);
            }
        }
        return order;
    }

    public boolean closeOrder(long id){
        log.info("Closing order with id={}", id);
        boolean closed = false;
        Order byId = orderDAO.findById(id);
        if (byId != null){
            byId.setOrderStatus(OrderStatus.CLOSED);
            update(byId);
            closed = true;
        }
        log.info("Order with id={} successfully closed", id);
        return closed;
    }

    public boolean cancelOrder(long id){
        log.info("Canceling order with id={}", id);
        boolean canceled = false;
        Order byId = orderDAO.findById(id);
        if (byId != null){
            byId.setOrderStatus(OrderStatus.CANCELLED);
            update(byId);
            canceled = true;
        }
        log.info("Order with id={} successfully cancelled", id);
        return canceled;
    }
    public long findOrderByTimeByCurrentMaster(Master master, LocalDateTime date){
        List<Order> orders = orderDAO.findAll();
        for (var v: orders){
            if (v.getMaster().equals(master) && v.getStartTime().isEqual(date)){
                return v.getId();
            }
        }
        return -1;
    }

    public boolean shiftOrder(long id, int durationToShiftInHours) {
        log.info("Shifting order with id={}", id);
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
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

            for (Order order : orders) {

                update(order);
            }
            connection.commit();
            log.info("Order with id={} successfully shifted", id);
            return true;

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            log.error("Error shifting order  with id={}", id, e);
            throw new RuntimeException("Перенос заказа не удался: " + e.getMessage());

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}