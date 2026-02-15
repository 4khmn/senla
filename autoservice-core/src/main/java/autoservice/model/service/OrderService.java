package autoservice.model.service;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.enums.ActiveOrdersSortEnum;
import autoservice.model.enums.OrderStatus;
import autoservice.model.enums.OrdersSortByTimeFrameEnum;
import autoservice.model.enums.OrdersSortEnum;
import autoservice.model.exceptions.OrderException;
import autoservice.model.repository.GarageSpotRepository;
import autoservice.model.repository.MasterRepository;
import autoservice.model.repository.OrderRepository;
import autoservice.model.service.domain.GarageSpotDomainService;
import autoservice.model.service.domain.MasterDomainService;
import autoservice.model.utils.HibernateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private transient final OrderRepository orderRepository;
    private transient final GarageSpotRepository garageSpotRepository;
    private transient final MasterRepository masterRepository;

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
                    sortedOrders = orderRepository.ordersSortByCreationDate(false);
                    transaction.commit();
                    break;
                case BY_END_DATE:
                    //дата выполнения
                    sortedOrders = orderRepository.ordersSortByEndDate(false);
                    transaction.commit();
                    break;
                case BY_START_DATE:
                    //дата планируемого начала выполнения
                    sortedOrders = orderRepository.ordersSortByStartDate();
                    transaction.commit();
                    break;
                case BY_PRICE:
                    //по цене
                    sortedOrders = orderRepository.ordersSortByPrice(false);
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

    //4 список текущих выполняемых заказов
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
                    sortedOrders = orderRepository.ordersSortByCreationDate(true);
                    transaction.commit();
                    break;
                case BY_END_DATE:
                    //по дате выполнения
                    sortedOrders = orderRepository.ordersSortByEndDate(true);
                    transaction.commit();
                    break;
                case BY_PRICE:
                    //по цене
                    sortedOrders = orderRepository.ordersSortByPrice(true);
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

    //4 заказ, выполняемый конкретным мастером
    public Order getOrderByMaster(Master master) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Order orderByMaster = orderRepository.getOrderByMaster(master);
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

    //4 заказы (выполненные/удаленные/отмененные) за промежуток времени
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
                    ordersAtCurrentTime = orderRepository.ordersSortByTimeFrameByCreationDate(start, end);
                    transaction.commit();
                    break;
                case BY_END_DATE:
                    //по дате выполнения
                    ordersAtCurrentTime = orderRepository.ordersSortByTimeFrameByEndDate(start, end);
                    transaction.commit();
                    break;
                case BY_PRICE:
                    //по цене
                    ordersAtCurrentTime = orderRepository.ordersSortByTimeFrameByPrice(start, end);
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
        orderRepository.save(order);
        return order.getId();
    }

    //сама записывает на ближайшее время
    public long addOrder(String description, int durationInHours, BigDecimal price) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bestStartTime = null;
        Master selectedMaster = null;
        GarageSpot selectedSpot = null;

        for (GarageSpot spot : this.getGarageSpotsWithCalendar()) {
            LocalDateTime candidateStart = spot.findNextAvailableSlotInGarageSpotSchedule(now, durationInHours);

            for (Master master : this.getMastersWithCalendar()) {
                if (master.isAvailable(candidateStart, candidateStart.plusHours(durationInHours))) {
                    if (bestStartTime == null || candidateStart.isBefore(bestStartTime)) {
                        bestStartTime = candidateStart;
                        selectedMaster = master;
                        selectedSpot = spot;
                    }

                }
            }
        }
        if (bestStartTime == null) {
            throw new RuntimeException("No available time slot found");
        }
        LocalDateTime endTime = bestStartTime.plusHours(durationInHours);
        return this.addOrder(description, selectedMaster, selectedSpot, bestStartTime, endTime, price);
    }

    //запись на конкретное время (-1 - записаться не удалось)
    public long addOrderAtCurrentTime(LocalDateTime date, String description, int durationInHours, BigDecimal price) {
        for (var garageSpot: this.getGarageSpotsWithCalendar()) {
            if (garageSpot.isAvailable(date, date.plusHours(durationInHours))) {
                for (var master: this.getMastersWithCalendar()) {
                    if (master.isAvailable(date, date.plusHours(durationInHours))) {
                        return this.addOrder(description, master, garageSpot, date, date.plusHours(durationInHours), price);
                    }
                }
            }
        }
        return -1;
    }

    public long addOrderWithCurrentMaster(String description, int durationInHours, BigDecimal price, Long masterId) {
        Master master = masterRepository.findById(masterId);
        if (master == null) {
            return -1;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bestStartTime = null;
        GarageSpot selectedSpot = null;

        for (GarageSpot spot : this.getGarageSpotsWithCalendar()) {
            LocalDateTime candidateStart = spot.findNextAvailableSlotInGarageSpotSchedule(now, durationInHours);

            if (master.isAvailable(candidateStart, candidateStart.plusHours(durationInHours))) {
                if (bestStartTime == null || candidateStart.isBefore(bestStartTime)) {
                    bestStartTime = candidateStart;
                    selectedSpot = spot;
                }
            }
        }
        if (bestStartTime == null) {
            throw new RuntimeException("No available time slot found");
        }
        LocalDateTime endTime = bestStartTime.plusHours(durationInHours);
        return this.addOrder(description, master, selectedSpot, bestStartTime, endTime, price);
    }


    public void update(Order order) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            orderRepository.update(order);
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
            orderRepository.delete(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error deleting order with id={}", id, e);
            throw new OrderException("Impossible to delete order: " + id + ", maybe order with this id doesn't exist", e);
        }
    }

    public List<Order> getOrders() {
        List<Order> orders = orderRepository.findAll();
        for (var order : orders) {
            if (order.getEndTime().isBefore(LocalDateTime.now())) {
                order.setOrderStatus(OrderStatus.CLOSED);
            }
        }
        return orders;
    }

    public Order getOrderById(long id) {
        Order order = orderRepository.findById(id);
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
            Order byId = orderRepository.findById(id);
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
            Order byId = orderRepository.findById(id);
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
            List<Order> orders = orderRepository.findAll();
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
        return orderRepository.count();
    }

    private long addOrder(String description, Master master, GarageSpot garageSpot, LocalDateTime startTime, LocalDateTime endtime, BigDecimal price) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        Order order = new Order(description, master, garageSpot, startTime, endtime, price);
        try {
            transaction = session.beginTransaction();
            orderRepository.save(order);
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



    private List<GarageSpot> getGarageSpotsWithCalendar() {
        List<GarageSpot> spots = garageSpotRepository.findAll();
        List<Object[]> slots = orderRepository.findTimeSlotsForAllGarageSpots();

        return GarageSpotDomainService.getGarageSpotsWithCalendar(spots, slots);
    }

    private List<Master> getMastersWithCalendar() {
        List<Master> masters = masterRepository.findAll();
        List<Object[]> slots = orderRepository.findTimeSlotsForAllMasters();

        return MasterDomainService.getMastersWithCalendar(masters, slots);
    }
}