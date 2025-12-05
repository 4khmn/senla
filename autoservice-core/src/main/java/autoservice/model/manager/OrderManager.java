package autoservice.model.manager;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.enums.ActiveOrdersSortEnum;
import autoservice.model.enums.OrderStatus;
import autoservice.model.enums.OrdersSortByTimeFrameEnum;
import autoservice.model.enums.OrdersSortEnum;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
public class OrderManager {
    private List<Order> orders;

    ////////////
    public OrderManager() {}
    ////////////
    //4 список заказов
    public List<Order> ordersSort(OrdersSortEnum decision) {
        List<Order> sortedOrders = orders.stream()
                .filter(v -> v.getOrderStatus() != OrderStatus.CANCELLED
                        && v.getOrderStatus() != OrderStatus.CLOSED)
                .toList();
        switch (decision) {
            case BY_CREATION_DATE:
                //по дате подачи
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getCreatedAt))
                        .toList();
                break;
            case BY_END_DATE:
                //дата выполнения
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getEndTime))
                        .toList();
                break;
            case BY_START_DATE:
                //дата планируемого начала выполнения
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getStartTime))
                        .toList();
                break;
            case BY_PRICE:
                //по цене
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getPrice))
                        .toList();
                break;
            default:
                //error
                throw new IllegalArgumentException("Неизвестный тип: " + decision);
        }
        return sortedOrders;
    }

    //4
    public List<Order> activeOrdersSort(ActiveOrdersSortEnum decision) {
        List<Order> sortedOrders = orders.stream()
                .filter(v -> v.getOrderStatus() != OrderStatus.CANCELLED
                        && v.getOrderStatus() != OrderStatus.CLOSED)
                .filter(v -> (v.getStartTime().isBefore(LocalDateTime.now())
                        && v.getEndTime().isAfter(LocalDateTime.now())))
                .toList();
        switch (decision) {
            case BY_CREATION_DATE:
                //по дате подачи
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getCreatedAt))
                        .toList();
                break;
            case BY_END_DATE:
                //по дате выполнения
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getEndTime))
                        .toList();
                break;
            case BY_PRICE:
                //по цене
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getPrice))
                        .toList();
                break;
            default:
                //error
                throw new IllegalArgumentException("Неизвестный тип: " + decision);
        }
        return sortedOrders;
    }


    //4
    public Order getOrderByMaster(Master master) {
        Optional<Order> optionalOrder = orders.stream()
                .filter(v -> v.getMaster().equals(master))
                .findAny();
        return optionalOrder.orElse(null);
    }

    public List<Order> getOrders() {
        return orders;
    }

    //4
    public List<Order> ordersSortByTimeFrame(LocalDateTime start, LocalDateTime end, OrdersSortByTimeFrameEnum decision) {
        List<Order> ordersAtCurrentTime = orders.stream()
                .filter(v -> v.getStartTime().isBefore(start) && v.getEndTime().isAfter(end))
                .toList();
        switch (decision) {
            case BY_CREATION_DATE:
                //по дате подачи
                ordersAtCurrentTime = ordersAtCurrentTime.stream()
                        .sorted(Comparator.comparing(Order::getCreatedAt))
                        .toList();
                break;
            case BY_END_DATE:
                //по дате выполнения
                ordersAtCurrentTime = ordersAtCurrentTime.stream()
                        .sorted(Comparator.comparing(Order::getEndTime))
                        .toList();
                break;
            case BY_PRICE:
                //по цене
                ordersAtCurrentTime = ordersAtCurrentTime.stream()
                        .sorted(Comparator.comparing(Order::getPrice))
                        .toList();
                break;
            default:
                //error
                throw new IllegalArgumentException("Неизвестный тип: " + decision);
        }
        return ordersAtCurrentTime;
    }


    public OrderManager(List<Order> orders) {
        this.orders = orders;
    }

    public long addOrderTest(Order order){
        orders.add(order);
        order.getMaster().addBusyTime(order.getStartTime(), order.getEndTime());
        order.getGarageSpot().addBusyTime(order.getStartTime(), order.getEndTime());
        return order.getId();
    }
    public long addOrder(String description, Master master, GarageSpot garageSpot, LocalDateTime startTime, LocalDateTime endtime, BigDecimal price) {
        Order order = new Order(description, master, garageSpot, startTime, endtime, price);
        orders.add(order);
        Collections.sort(orders);
        order.getMaster().addBusyTime(order.getStartTime(), order.getEndTime());
        order.getGarageSpot().addBusyTime(order.getStartTime(), order.getEndTime());
        return order.getId();
        //System.out.println("Order #" + order.getId() + " was successfully added");
    }
    public long addOrder(long id, String description, Master master, GarageSpot garageSpot, LocalDateTime startTime, LocalDateTime endtime, BigDecimal price) {
        Order order = new Order(id, description, master, garageSpot, startTime, endtime, price);
        orders.add(order);
        Collections.sort(orders);
        order.getMaster().addBusyTime(order.getStartTime(), order.getEndTime());
        order.getGarageSpot().addBusyTime(order.getStartTime(), order.getEndTime());
        return order.getId();
        //System.out.println("Order #" + order.getId() + " was successfully added");
    }

    public boolean deleteOrder(long id){
        boolean removed=false;
        for (var v: orders) {
            if (v.getId()==id){
                orders.remove(v);
                removed=true;
            }
        }
        return removed;
    }

    public Order getOrderById(long id){
        for (var v: orders){
            if (v.getId()==id){
                return v;
            }
        }
        return null;
    }

    public boolean closeOrder(long id){
        boolean closed = false;
        for (var v: orders){
            if (v.getId()==id){
                v.setOrderStatus(OrderStatus.CLOSED);
                closed = true;
            }
        }
        return closed;
    }

    public boolean cancelOrder(long id){
        boolean canceled = false;
        for (var v: orders){
            if (v.getId()==id){
                v.setOrderStatus(OrderStatus.CANCELLED);
                canceled = true;
            }
        }
        return canceled;
    }
    public long findOrderByTimeByCurrentMaster(Master master, LocalDateTime date){
        for (var v: orders){
            if (v.getMaster().equals(master) && v.getStartTime().isEqual(date)){
                return v.getId();
            }
        }
        return -1;
    }

    public boolean shiftOrder(long id, int durationToShiftInHours){
        Order orderToShift = getOrderById(id);
        if (orderToShift == null){
            return false;
        }
        List<Order> ordersFromOrderToShift = orders.subList(orders.indexOf(orderToShift), orders.size());

        List<Order> copy = new ArrayList<>(ordersFromOrderToShift);
        copy.remove(orderToShift);
        orderToShift.setEndTime(orderToShift.getEndTime().plusHours(durationToShiftInHours));


        copy.add(orderToShift);
        int index = 1;
        Order next = copy.get(index);
        Collections.sort(copy);
        while(index<copy.size()) {
            List<Order> range = copy.subList(copy.indexOf(orderToShift), index);

            for (var v : new ArrayList<>(range)) {
                if (isConflict(v, next)) {
                    Duration shift = Duration.between(next.getStartTime(), v.getEndTime());
                    if (!shift.isNegative()) {
                        copy.remove(next);


                        next.setStartTime(next.getStartTime().plus(shift));
                        next.setEndTime(next.getEndTime().plus(shift));

                        copy.add(index, next);
                    }
                }
            }
            if (index<copy.size()-1) {
                next = copy.get(index + 1);
            }
            index+=1;
        }
        Collections.sort(orders);
        for (var v: orders){
            v.getMaster().addBusyTime(v.getStartTime(), v.getEndTime());
        }
        for (var v: orders){
            v.getGarageSpot().addBusyTime(v.getStartTime(), v.getEndTime());
        }
        return true;
    }

    private boolean isConflict(Order a, Order b) {
        boolean sameMaster = a.getMaster().equals(b.getMaster());
        boolean sameSpot = a.getGarageSpot().equals(b.getGarageSpot());
        boolean timeOverlap = !a.getEndTime().isBefore(b.getStartTime());
        return (sameMaster || sameSpot) && timeOverlap;
    }

    @Override
    public String toString() {
        return "OrderManager{" +
                "orders=" + orders +
                '}';
    }
    public OrderManager cloneManager(MasterManager masterCopy, GarageSpotManager garageCopy) {
        List<Order> copy = new ArrayList<>();
        for (Order o : this.orders) {
            Order clone = o.clone();
            System.out.println("1");
            clone.setMaster(masterCopy.getMasterById(o.getMaster().getId()));
            clone.setGarageSpot(garageCopy.getGarageSpotById(o.getGarageSpot().getId()));
            copy.add(clone);
        }
        return new OrderManager(copy);
    }
    public void replaceData(OrderManager other) {
        this.orders = other.orders;
    }
}
