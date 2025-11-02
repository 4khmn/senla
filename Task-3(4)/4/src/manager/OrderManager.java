package manager;

import model.GarageSpot;
import model.Master;
import model.Order;
import model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderManager {
    private List<Order> orders;

    //4 список заказов
    public List<Order> ordersSort(int decision) {
        List<Order> sortedOrders = orders.stream()
                .filter(v -> v.getOrderStatus() != OrderStatus.CANCELLED
                        && v.getOrderStatus() != OrderStatus.CLOSED)
                .toList();
        switch (decision) {
            case 1:
                //по дате подачи
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getCreatedAt))
                        .toList();
                break;
            case 2:
                //дата выполнения
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getEndTime))
                        .toList();
                break;
            case 3:
                //дата планируемого начала выполнения
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getStartTime))
                        .toList();
                break;
            case 4:
                //по цене
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getPrice))
                        .toList();
                break;
            default:
                //error
                return null;
        }
        return sortedOrders;
    }

    //4
    public List<Order> activeOrdersSort(int decision) {
        List<Order> sortedOrders = orders.stream()
                .filter(v -> v.getOrderStatus() != OrderStatus.CANCELLED
                        && v.getOrderStatus() != OrderStatus.CLOSED)
                .filter(v -> (v.getStartTime().isBefore(LocalDateTime.now())
                        && v.getEndTime().isAfter(LocalDateTime.now())))
                .toList();
        switch (decision) {
            case 1:
                //по дате подачи
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getCreatedAt))
                        .toList();
                break;
            case 2:
                //по дате выполнения
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getEndTime))
                        .toList();
                break;
            case 3:
                //по цене
                sortedOrders = sortedOrders.stream()
                        .sorted(Comparator.comparing(Order::getPrice))
                        .toList();
                break;
            default:
                //error
                return null;
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
    public List<Order> ordersSortByTime(LocalDateTime start, LocalDateTime end, int decision) {
        List<Order> ordersAtCurrentTime = orders.stream()
                .filter(v -> v.getStartTime().isBefore(start) && v.getEndTime().isAfter(end))
                .toList();
        switch (decision) {
            case 1:
                //по дате подачи
                ordersAtCurrentTime = ordersAtCurrentTime.stream()
                        .sorted(Comparator.comparing(Order::getCreatedAt))
                        .toList();
                break;
            case 2:
                //по дате выполнения
                ordersAtCurrentTime = ordersAtCurrentTime.stream()
                        .sorted(Comparator.comparing(Order::getEndTime))
                        .toList();
                break;
            case 3:
                //по цене
                ordersAtCurrentTime = ordersAtCurrentTime.stream()
                        .sorted(Comparator.comparing(Order::getPrice))
                        .toList();
                break;
            default:
                //error
                return null;
        }
        return ordersAtCurrentTime;
    }


    public OrderManager(List<Order> orders) {
        this.orders = orders;
    }

    private boolean overlaps(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private boolean isMasterAvailable(Master master, LocalDateTime start, LocalDateTime end) {
        for (var v : orders) {
            if (v.getMaster().equals(master)) {
                if (v.getOrderStatus() != OrderStatus.CLOSED &&
                        v.getOrderStatus() != OrderStatus.CANCELLED) {
                    if (overlaps(v.getStartTime(), v.getEndTime(), start, end)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isSpotAvailable(GarageSpot garageSpot, LocalDateTime start, LocalDateTime end) {
        for (var v : orders) {
            if (v.getGarageSpot().equals(garageSpot)) {
                if (v.getOrderStatus() != OrderStatus.CLOSED &&
                        v.getOrderStatus() != OrderStatus.CANCELLED) {
                    if (overlaps(v.getStartTime(), v.getEndTime(), start, end)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public long addOrder(String description, Master master, GarageSpot garageSpot, LocalDateTime startTime, LocalDateTime endtime,BigDecimal price) {
        Order order = new Order(description, master, garageSpot, startTime, endtime, price);
        orders.add(order);
        orders.sort(Comparator.comparing(Order::getStartTime));
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

    @Override
    public String toString() {
        return "OrderManager{" +
                "orders=" + orders +
                '}';
    }
}
