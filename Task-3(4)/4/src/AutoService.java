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
        Master master = orderManager.getOrderById(id).getMaster();
        GarageSpot garageSpot = orderManager.getOrderById(id).getGarageSpot();
        SortedSet<TimeSlot> reversedCalendar = master.getCalendar().reversed();
        //1. перенос всех задач на durationToShiftInHours с конца
        List<TimeSlot> timeSlotsToShift = new ArrayList<>(reversedCalendar);
        List<Order> orders = new ArrayList<>();
        for (TimeSlot currentTimeSlot : timeSlotsToShift) {
            master.freeTimeSlot(currentTimeSlot.getStart(), currentTimeSlot.getEnd());
            garageSpot.freeTimeSlot(currentTimeSlot.getStart(), currentTimeSlot.getEnd());
            orders.add(orderManager.getOrderById(orderManager.findOrderByTimeByCurrentMaster(master,currentTimeSlot.getStart())));
            if (!master.addBusyTime(currentTimeSlot.getStart().plusHours(durationToShiftInHours),
                    currentTimeSlot.getEnd().plusHours(durationToShiftInHours))){
                throw new Exception("Перенести задание не удалось");
            }
            if (!garageSpot.addBusyTime(currentTimeSlot.getStart().plusHours(durationToShiftInHours),
                    currentTimeSlot.getEnd().plusHours(durationToShiftInHours))){
                throw new Exception("Перенести задание не удалось");
            }
            //найти заказ по времени у конкретного машино-места
            for (var v: orderManager.getOrders()){
                if (v.getGarageSpot().equals(garageSpot)){
                    if (v.getStartTime().isEqual(currentTimeSlot.getStart())){
                        if (!v.getMaster().equals(master)){
                            //делаем сдвиги у других мастеров
                            shiftOrder(v.getId(), durationToShiftInHours);
                        }
                    }
                }
            }
        }
        TimeSlot firstTimeSlot = timeSlotsToShift.getLast();

        master.freeTimeSlot(firstTimeSlot.getStart().plusHours(durationToShiftInHours), firstTimeSlot.getEnd().plusHours(durationToShiftInHours));
        garageSpot.freeTimeSlot(firstTimeSlot.getStart().plusHours(durationToShiftInHours), firstTimeSlot.getEnd().plusHours(durationToShiftInHours));
        if(!master.addBusyTime(firstTimeSlot.getStart(), firstTimeSlot.getEnd().plusHours(durationToShiftInHours))){
            throw new Exception("Перенести задание не удалось");
        }
        if(!garageSpot.addBusyTime(firstTimeSlot.getStart(), firstTimeSlot.getEnd().plusHours(durationToShiftInHours))){
            throw new Exception("Перенести задание не удалось");
        }
        LocalDateTime previousEndAt = null;
        //2. возвращение если зря пододвинул в действии 1
        List<TimeSlot> timeSlotsToShift2 = new ArrayList<>(master.getCalendar());
        previousEndAt = timeSlotsToShift2.getFirst().getEnd();
        orders = orders.reversed();
        orders.getFirst().setEndTime(previousEndAt);
        for (int i=1; i<timeSlotsToShift2.size(); i++){
            TimeSlot currentTimeSlot = timeSlotsToShift2.get(i);
            if (currentTimeSlot.getStart().minusHours(durationToShiftInHours).isAfter(previousEndAt) ||
                    currentTimeSlot.getStart().minusHours(durationToShiftInHours).isEqual(previousEndAt)){
                master.freeTimeSlot(currentTimeSlot.getStart(), currentTimeSlot.getEnd());
                garageSpot.freeTimeSlot(currentTimeSlot.getStart(), currentTimeSlot.getEnd());
                orders.get(i).setCreatedAt(currentTimeSlot.getStart().minusHours(durationToShiftInHours));
                orders.get(i).setEndTime(currentTimeSlot.getEnd().minusHours(durationToShiftInHours));
                if(!master.addBusyTime(currentTimeSlot.getStart().minusHours(durationToShiftInHours),
                        currentTimeSlot.getEnd().minusHours(durationToShiftInHours))){
                    throw new Exception("Перенести задание не удалось");
                }
                if(!garageSpot.addBusyTime(currentTimeSlot.getStart().minusHours(durationToShiftInHours),
                        currentTimeSlot.getEnd().minusHours(durationToShiftInHours))){
                    throw new Exception("Перенести задание не удалось");
                }
                previousEndAt = currentTimeSlot.getEnd().minusHours(durationToShiftInHours);
            }
            else{
                master.freeTimeSlot(currentTimeSlot.getStart(), currentTimeSlot.getEnd());
                garageSpot.freeTimeSlot(currentTimeSlot.getStart(), currentTimeSlot.getEnd());
                long differenceInHours = Math.abs(Duration.between(currentTimeSlot.getStart(), previousEndAt).toHours());
                orders.get(i).setCreatedAt(currentTimeSlot.getStart().minusHours(differenceInHours));
                orders.get(i).setEndTime(currentTimeSlot.getEnd().minusHours(differenceInHours));
                if(!master.addBusyTime(currentTimeSlot.getStart().minusHours(differenceInHours),
                        currentTimeSlot.getEnd().minusHours(differenceInHours))){
                    throw new Exception("Перенести задание не удалось");
                }
                if(!garageSpot.addBusyTime(currentTimeSlot.getStart().minusHours(differenceInHours),
                        currentTimeSlot.getEnd().minusHours(differenceInHours))){
                    throw new Exception("Перенести задание не удалось");
                }
                previousEndAt = currentTimeSlot.getEnd().minusHours(differenceInHours);
            }
        }
        return true;
    }




}
