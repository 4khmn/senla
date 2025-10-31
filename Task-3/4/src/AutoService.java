import manager.GarageSpotManager;
import manager.MasterManager;
import manager.OrderManager;
import model.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class AutoService {
    private final GarageSpotManager garageManager;
    private final OrderManager orderManager;
    private final MasterManager masterManager;

    public AutoService() {
        List<Master> masters = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        List<GarageSpot> spots = new ArrayList<>();

        this.garageManager = new GarageSpotManager(spots);
        this.orderManager = new OrderManager(orders);
        this.masterManager = new MasterManager(masters);
    }

    @Override
    public String toString() {
        return "AutoService{" +
                "garageManager=" + garageManager +
                ", orderManager=" + orderManager +
                ", masterManager=" + masterManager +
                '}';
    }

    //4
    public List<GarageSpot> getFreeSpots(){
        List<GarageSpot> freeSpots = new ArrayList<>();
        List<Order> currentOrders = new ArrayList<>();
        for (var v: orderManager.getOrders()){
            if (v.getEndTime().isAfter(LocalDateTime.now()) && v.getStartTime().isBefore(LocalDateTime.now())){
                currentOrders.add(v);
            }
        }
        List<GarageSpot> currentGarageSpots = new ArrayList<>();;
        for (var v: currentOrders){
            currentGarageSpots.add(v.getGarageSpot());

        }
        for (var v: garageManager.getGarageSpots()){
            if (!currentGarageSpots.contains(v)){
                freeSpots.add(v);
            }
        }
        return freeSpots;
    }

    //4
    public List<Order> ordersSort(int decision){
        return orderManager.ordersSort(decision);
    }

    //4
    public List<Master> mastersSort(int decision, int duration){
        return masterManager.mastersSort(decision, LocalDateTime.now(), duration);
    }

    //4
    public List<Order> activeOrdersSort(int decision){
        return orderManager.activeOrdersSort(decision);
    }

    //4
    public Order getOrderByMaster(Master master){
        return orderManager.getOrderByMaster(master);
    }

    //4
    public Master getMasterByOrder(Order order){
        return masterManager.getMasterByOrder(order);
    }

    //4
    public List<Order> ordersSortByTime(LocalDateTime start, LocalDateTime end, int decision){
        return orderManager.ordersSortByTime(start, end, decision);
    }

    //4
    public int getFreeLotsByDate(LocalDateTime date) {
        List<Order> ordersAtCurrentTime = orderManager.getOrders().stream()
                .filter(v-> date.isBefore(v.getEndTime()) &&
                        date.isAfter(v.getStartTime()))
                .filter(v -> v.getOrderStatus() != OrderStatus.CANCELLED
                        && v.getOrderStatus() != OrderStatus.CLOSED)
                .toList();
        int freeMasters = masterManager.getMasters().size() - ordersAtCurrentTime.size();
        int freeGarageSpots = garageManager.getGarageSpots().size() - ordersAtCurrentTime.size();
        return min(freeMasters, freeGarageSpots);
    }

    //4
    public LocalDateTime getClosestDate(int duration) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkTime = now;

        while (true) {
            LocalDateTime slotEnd = checkTime.plusHours(duration);

            final LocalDateTime finalCheckTime = checkTime;
            final LocalDateTime finalSlotEnd = slotEnd;

            boolean hasFreeMaster = masterManager.getMasters().stream()
                    .anyMatch(master -> master.isAvailable(finalCheckTime, finalSlotEnd));

            boolean hasFreeSpot = garageManager.getGarageSpots().stream()
                    .anyMatch(spot -> spot.isAvailable(finalCheckTime, finalSlotEnd));

            if (hasFreeMaster && hasFreeSpot) {
                return checkTime;
            }

            checkTime = checkTime.plusMinutes(30);
        }
    }



    //model.Master
    public long addMaster(String name, BigDecimal salary){
        return masterManager.addMaster(name, salary);
    }

    public boolean deleteMaster(long id){
        return masterManager.deleteMaster(id);
    }
    //model.GarageSpot
    public long addGarageSpot(){
        return garageManager.addGarageSpot();
    }

    public boolean deleteGarageSpot(long id){
        return garageManager.deleteGarageSpot(id);
    }
    //model.Order


    public long addOrder(String description, int duration, BigDecimal price) {
        LocalDateTime now = LocalDateTime.now();
        Master selectedMaster = null;
        GarageSpot selectedSpot = null;
        LocalDateTime bestStartTime = null;

        for (GarageSpot spot : garageManager.getGarageSpots()) {
            LocalDateTime spotStartTime = now;
            while (spot.isAvailable(spotStartTime, spotStartTime.plusHours(duration))) {
                for (Master master : masterManager.getMasters()) {
                    if (master.isAvailable(spotStartTime, spotStartTime.plusHours(duration))) {
                        if (bestStartTime == null || spotStartTime.isBefore(bestStartTime)) {
                            bestStartTime = spotStartTime;
                            selectedMaster = master;
                            selectedSpot = spot;
                        }
                    }
                }
                spotStartTime = spotStartTime.plusMinutes(1);
            }
        }

        if (bestStartTime != null && selectedMaster != null && selectedSpot != null) {
            LocalDateTime endTime = bestStartTime.plusHours(duration);
            selectedSpot.addBusyTime(bestStartTime, endTime);
            selectedMaster.addBusyTime(bestStartTime, endTime);
            return orderManager.addOrder(description, selectedMaster, selectedSpot, bestStartTime, endTime, price);
        } else {
            throw new RuntimeException("No available time slots for the order.");
        }
    }

    public boolean deleteOrder(long id){
        Order order = orderManager.getOrderById(id);
        if (order!= null){
            LocalDateTime startTime = order.getStartTime();
            LocalDateTime endTime = order.getEndTime();

            order.getMaster().freeTimeSlot(startTime, endTime);
            order.getGarageSpot().freeTimeSlot(startTime, endTime);
            return orderManager.deleteOrder(id);
        }
        return false;
    }

    public boolean closeOrder(long id){
        Order order = orderManager.getOrderById(id);
        if (order!= null){
            LocalDateTime startTime = order.getStartTime();
            LocalDateTime endTime = order.getEndTime();

            order.getMaster().freeTimeSlot(startTime, endTime);
            order.getGarageSpot().freeTimeSlot(startTime, endTime);
            return orderManager.closeOrder(id);
        }
        return false;
    }

    public boolean cancelOrder(long id) {
        Order order = orderManager.getOrderById(id);

        if (order != null) {
            LocalDateTime startTime = order.getStartTime();
            LocalDateTime endTime = order.getEndTime();

            order.getMaster().freeTimeSlot(startTime, endTime);

            order.getGarageSpot().freeTimeSlot(startTime, endTime);

            return orderManager.cancelOrder(id);
        }

        return false;
    }

    public boolean shiftOrder(long id, int durationInHours){
        Master master = orderManager.getOrderById(id).getMaster();
        SortedSet<TimeSlot> reversedCalendar = master.getCalendar().reversed();
        Iterator<TimeSlot> iterator = reversedCalendar.iterator();
        if (iterator.hasNext()) {
            TimeSlot currentTimeSlot = iterator.next();
            master.freeTimeSlot(currentTimeSlot.getStart(), currentTimeSlot.getEnd());
            master.addBusyTime(currentTimeSlot.getStart().plusHours(durationInHours), currentTimeSlot.getEnd().plusHours(durationInHours));



        }





    }




}
