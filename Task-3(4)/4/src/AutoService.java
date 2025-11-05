import manager.GarageSpotManager;
import manager.MasterManager;
import manager.OrderManager;
import model.*;


import java.math.BigDecimal;
import java.time.Duration;
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

    //4 список свободных мест в сервисных гаражах
    public List<GarageSpot> getFreeSpots(){
        return garageManager.getFreeSpots();
    }

    //4 список заказов
    public List<Order> ordersSort(int decision){
        return orderManager.ordersSort(decision);
    }

    //4 список авто-мастеров
    public List<Master> mastersSort(int decision, int durationInHours){
        return masterManager.mastersSort(decision, LocalDateTime.now(), durationInHours);
    }

    //4 список текущихвыполняемых заказов
    public List<Order> activeOrdersSort(int decision){
        return orderManager.activeOrdersSort(decision);
    }

    //4 заказ, выполняемый конкретным мастером
    public Order getOrderByMaster(Master master){
        return orderManager.getOrderByMaster(master);
    }

    //4 мастер, выполняющий конкретный заказ
    public Master getMasterByOrder(Order order){
        return masterManager.getMasterByOrder(order);
    }

    //4 заказы (выполненные/удаленные/отмененные) за промежуток времени
    public List<Order> ordersSortByTimeFrame(LocalDateTime start, LocalDateTime end, int decision){
        return orderManager.ordersSortByTime(start, end, decision);
    }

    //4 количество свободных мест на сервисе на любую дату в будующем
    public int getFreeLotsByDate(LocalDateTime date){
        int spotsCount=0;
        int mastersCount=0;
        for (var v: garageManager.getGarageSpots()){
            if (v.isAvailable(date, date.plusMinutes(1))){
                spotsCount+=1;
            }
        }
        for (var v: masterManager.getMasters()){
            if (v.isAvailable(date, date.plusMinutes(1))){
                mastersCount+=1;
            }
        }
        return min(mastersCount, spotsCount);
    }

    //4 ближайшая свободная дата
    public LocalDateTime getClosestDate(int durationInHours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bestStartTime = null;
        Master selectedMaster = null;
        GarageSpot selectedSpot = null;

        for (GarageSpot spot : garageManager.getGarageSpots()) {
            LocalDateTime candidateStart = spot.findNextAvailableSlotInGarageSpotSchedule(now, durationInHours);

            for (Master master : masterManager.getMasters()) {
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
        selectedSpot.addBusyTime(bestStartTime, endTime);
        selectedMaster.addBusyTime(bestStartTime, endTime);
        return bestStartTime;
    }

    public List<Order> getOrders(){
            return orderManager.getOrders();
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

    public Master getMasterById(long id){
        return masterManager.getMasterById(id);
    }
    public Order getOrderById(long id){
        return orderManager.getOrderById(id);
    }
    public GarageSpot getGarageSpotById(long id){
        return garageManager.getGarageSpotById(id);
    }

    //model.Order
    //запись на конкретное время (-1 - записаться не удалось)
    public long addOrderAtCurrentTime(LocalDateTime date, String description, int durationInHours, BigDecimal price){
        for (var v: garageManager.getGarageSpots()){
            if (v.isAvailable(date, date.plusHours(durationInHours))){
                for (var x: masterManager.getMasters()){
                    if (x.isAvailable(date, date.plusHours(durationInHours))){
                        v.addBusyTime(date, date.plusHours(durationInHours));
                        x.addBusyTime(date, date.plusHours(durationInHours));
                        return orderManager.addOrder(description, x, v, date, date.plusHours(durationInHours), price);
                    }
                }
            }
        }
        System.out.println("Нельзя добавить в данное время.");
        return -1;
    }

    //сама записывает на ближайшее время
    public long addOrder(String description, int durationInHours, BigDecimal price) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bestStartTime = null;
        Master selectedMaster = null;
        GarageSpot selectedSpot = null;

        for (GarageSpot spot : garageManager.getGarageSpots()) {
            LocalDateTime candidateStart = spot.findNextAvailableSlotInGarageSpotSchedule(now, durationInHours);

            for (Master master : masterManager.getMasters()) {
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
        selectedSpot.addBusyTime(bestStartTime, endTime);
        selectedMaster.addBusyTime(bestStartTime, endTime);
        return orderManager.addOrder(description, selectedMaster, selectedSpot, bestStartTime, endTime, price);
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

    public boolean shiftOrder(long id, int durationToShiftInHours) throws Exception {
        for (var v: masterManager.getMasters()){
            v.freeAllSchedule();
        }
        for (var v: garageManager.getGarageSpots()){
            v.freeAllSchedule();
        }
        return orderManager.shiftOrder(id, durationToShiftInHours);
    }




}
