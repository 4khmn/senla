package autoservice.model;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.enums.ActiveOrdersSortEnum;
import autoservice.model.enums.MastersSortEnum;
import autoservice.model.enums.OrdersSortByTimeFrameEnum;
import autoservice.model.enums.OrdersSortEnum;
import autoservice.model.exceptions.CsvParsingException;
import autoservice.model.exceptions.IllegalGarageSpotSize;
import autoservice.model.exceptions.ImportException;
import autoservice.model.io.imports.CsvImportService;
import autoservice.model.io.exports.GarageSpotsCsvExport;
import autoservice.model.io.exports.MastersCsvExport;
import autoservice.model.io.exports.OrdersCsvExport;
import autoservice.model.manager.GarageSpotManager;
import autoservice.model.manager.MasterManager;
import autoservice.model.manager.OrderManager;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Math.min;
@JsonAutoDetect
public class AutoService {
    @JsonIgnore
    private static AutoService instance;

    private final GarageSpotManager garageManager;
    private final OrderManager orderManager;
    private final MasterManager masterManager;
    @JsonIgnore
    private final CsvImportService importService;

    @JsonIgnore
    private final GarageSpotsCsvExport garageSpotsCsvExport;

    @JsonIgnore
    private final MastersCsvExport mastersCsvExport;

    @JsonIgnore
    private final OrdersCsvExport ordersCsvExport;


    private AutoService() {
        List<Master> masters = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        List<GarageSpot> spots = new ArrayList<>();

        this.garageManager = new GarageSpotManager(spots);
        this.orderManager = new OrderManager(orders);
        this.masterManager = new MasterManager(masters);
        this.importService = CsvImportService.getInstance(this.orderManager, this.garageManager, this.masterManager);
        this.garageSpotsCsvExport = new GarageSpotsCsvExport(garageManager);
        this.mastersCsvExport = new MastersCsvExport(masterManager);
        this.ordersCsvExport = new OrdersCsvExport(orderManager);
    }
    public static AutoService getInstance() {
        if (instance == null) {
            instance = new AutoService();
        }
        return instance;
    }
    public static void replaceInstance(AutoService service){
        instance = service;
    }

    @Override
    public String toString() {
        return "autoservice.model.AutoService{" +
                "garageManager=" + garageManager +
                ", orderManager=" + orderManager +
                ", masterManager=" + masterManager +
                '}';
    }

    //4 список свободных мест в сервисных гаражах
    @JsonIgnore
    public List<GarageSpot> getFreeSpots(){
        return garageManager.getFreeSpots();
    }

    //4 список заказов
    public List<Order> ordersSort(OrdersSortEnum decision){
        return orderManager.ordersSort(decision);
    }

    //4 список авто-мастеров
    public List<Master> mastersSort(MastersSortEnum decision){
        return masterManager.mastersSort(decision);
    }

    //4 список текущих выполняемых заказов
    public List<Order> activeOrdersSort(ActiveOrdersSortEnum decision){
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
    public List<Order> ordersSortByTimeFrame(LocalDateTime start, LocalDateTime end, OrdersSortByTimeFrameEnum type){
        return orderManager.ordersSortByTimeFrame(start, end, type);
    }

    //4 количество свободных мест на сервисе на любую дату в будующем
    public int getNumberOfFreeSpotsByDate(LocalDateTime date){
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



    //model.Master
    public long addMaster(String name, BigDecimal salary){
        return masterManager.addMaster(name, salary);
    }

    public boolean deleteMaster(long id){
        return masterManager.deleteMaster(id);
    }
    //model.GarageSpot
    public long addGarageSpot(double size, boolean hasLift, boolean hasPit) {
        return garageManager.addGarageSpot(size, hasLift, hasPit);
    }


    public boolean deleteGarageSpot(long id){
        return garageManager.deleteGarageSpot(id);
    }

    public MasterManager getMasterManager(){
        return masterManager;
    }
    public OrderManager getOrderManager() {
        return orderManager;
    }
    public GarageSpotManager getGarageManager(){
        return garageManager;
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
    @JsonIgnore
    public int getMastersCount(){
        return masterManager.getMasters().size();
    }
    @JsonIgnore
    public int getGarageSpotsCount(){
        return garageManager.getGarageSpots().size();
    }
    @JsonIgnore
    public int getOrdersCount(){
        return orderManager.getOrders().size();
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

    public boolean shiftOrder(long id, int durationToShiftInHours){
        for (var v: masterManager.getMasters()){
            v.freeAllSchedule();
        }
        for (var v: garageManager.getGarageSpots()){
            v.freeAllSchedule();
        }
        return orderManager.shiftOrder(id, durationToShiftInHours);
    }


    public boolean importMasters()  throws  IOException, ImportException, CsvParsingException {
        return importService.importMasters();
    }
    public boolean importGarageSpots() throws ImportException, IOException, CsvParsingException, IllegalGarageSpotSize {
        return importService.importGarageSpots();
    }

    public boolean importOrders() throws IOException, ImportException, CsvParsingException {
        return importService.importOrders();
    }

    public void exportMasters() throws IOException {
        mastersCsvExport.export();
    }
    public void exportGarageSpots() throws IOException {
        garageSpotsCsvExport.export();
    }
    public void exportOrders() throws IOException {
        ordersCsvExport.export();
    }





}
