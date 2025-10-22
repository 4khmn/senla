package manager;

import model.GarageSpot;
import model.Master;
import model.Order;
import model.OrderStatus;
import result.OrderResult;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class OrderManager {
    private List<Order> orders;

    public OrderManager(List<Order> orders) {
        this.orders = orders;
    }
    private boolean overlaps(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private boolean isMasterAvailable(Master master, LocalDateTime start, LocalDateTime end){
        for (var v: orders){
            if (v.getMaster().equals(master)){
                if (v.getOrderStatus()!=OrderStatus.CLOSED &&
                v.getOrderStatus()!=OrderStatus.CANCELLED){
                    if(overlaps(v.getStartTime(), v.getEndTime(), start, end)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private boolean isSpotAvailable(GarageSpot garageSpot, LocalDateTime start, LocalDateTime end){
        for (var v: orders){
            if (v.getGarageSpot().equals(garageSpot)){
                if (v.getOrderStatus()!=OrderStatus.CLOSED &&
                v.getOrderStatus()!=OrderStatus.CANCELLED){
                    if (overlaps(v.getStartTime(), v.getEndTime(), start, end)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public OrderResult addOrder(Order order){
        if (!orders.contains(order)){
            Master master = order.getMaster();
            GarageSpot garageSpot = order.getGarageSpot();
            LocalDateTime start = order.getStartTime();
            LocalDateTime end = order.getEndTime();
            if (!isMasterAvailable(master, start, end) &&
            !isSpotAvailable(garageSpot, start, end)){
                return OrderResult.MASTER_BUSY_AND_SPOT_OCCUPIED;
                //System.out.println("Master and garage spot are not available at this time {order #" + order.getId() + "}");
            }
            else if(!isMasterAvailable(master, start, end) ) {
                return OrderResult.MASTER_BUSY;
                //System.out.println("Master is busy at this time {order #" + order.getId() + "}");
            }
            else if (!isSpotAvailable(garageSpot, start, end)){
                return OrderResult.SPOT_OCCUPIED;
                //System.out.println("Garage spot is occupied at this time {order #" + order.getId() + "}");
            }
            else{
                orders.add(order);
                orders.sort(Comparator.comparing(Order::getStartTime));
                return OrderResult.SUCCESS_ADDED;
                //System.out.println("Order #" + order.getId() + " was successfully added");
            }
        }
        else{
            return OrderResult.ALREADY_EXISTS;
            //System.out.println("This order is already exist in System {order #" + order.getId() + "}");
        }
    }

    public OrderResult deleteOrder(Order order){
        if (orders.contains(order)){
            orders.remove(order);
            return OrderResult.SUCCESS_DELETED;
            //System.out.println("Order #" + order.getId() + " was successfully deleted");
        }
        else{
            return OrderResult.NOT_FOUND;
            //System.out.println("There is no such order in System {order #" + order.getId() + "}");
        }
    }

    public OrderResult closeOrder(Order order){
        if (orders.contains(order)) {
            if (order.getOrderStatus() == OrderStatus.CLOSED || order.getOrderStatus() == OrderStatus.CANCELLED) {
                return OrderResult.ALREADY_CLOSED_OR_CANCELLED;
                //System.out.println("This order is already closed or canceled {order #" + order.getId() + "}");
            } else {
                order.setOrderStatus(OrderStatus.CLOSED);
                return OrderResult.SUCCESS_CLOSED;
                //System.out.println("Order #" + order.getId() + " was successfully closed");
            }
        }
        else{
            return OrderResult.NOT_FOUND;
            //System.out.println("There is no such order in System {order #" + order.getId() + "}");
        }
    }

    public OrderResult cancelOrder(Order order){
        if (orders.contains(order)) {
            if (order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.CLOSED) {
                return OrderResult.ALREADY_CLOSED_OR_CANCELLED;
                //ystem.out.println("This order is already canceled or closed {order #" + order.getId() + "}");
            } else {
                order.setOrderStatus(OrderStatus.CANCELLED);
                return OrderResult.SUCCESS_CANCELLED;
                //System.out.println("Order #" + order.getId() + " was successfully cancelled");
            }
        }
        else{
            return OrderResult.NOT_FOUND;
            //System.out.println("There is no such order in System {order #" + order.getId() + "}");
        }
    }



    public OrderResult shiftOrder(Order order, int hours){
        order.setEndTime(order.getEndTime().plusHours(hours));
        int index = orders.indexOf(order);
        if(index!= -1) {
            for (int i = index + 1; i < orders.size(); i++) {
                Order current = orders.get(i);
                if (current.getMaster().equals(order.getMaster()) ||
                        current.getGarageSpot().equals(order.getGarageSpot())) {
                    current.setStartTime(current.getStartTime().plusHours(hours));
                    current.setEndTime(current.getEndTime().plusHours(hours));
                }
            }
            return OrderResult.SUCCESS_SHIFTED;
            //System.out.println("Order #" + order.getId() + " was successfully shifted");
        }
        else{
            return OrderResult.NOT_FOUND;
            //System.out.println("There is no such order in System {order #" + order.getId() + "}");
        }
    }

    @Override
    public String toString() {
        return "OrderManager{" +
                "orders=" + orders +
                '}';
    }
}
