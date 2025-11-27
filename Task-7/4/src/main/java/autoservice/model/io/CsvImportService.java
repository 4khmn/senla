package main.java.autoservice.model.io;

import main.java.autoservice.model.entities.GarageSpot;
import main.java.autoservice.model.entities.Master;
import main.java.autoservice.model.entities.Order;
import main.java.autoservice.model.enums.OrderStatus;
import main.java.autoservice.model.exceptions.CsvParsingException;
import main.java.autoservice.model.exceptions.IllegalGarageSpotSize;
import main.java.autoservice.model.exceptions.ImportException;
import main.java.autoservice.model.manager.GarageSpotManager;
import main.java.autoservice.model.manager.MasterManager;
import main.java.autoservice.model.manager.OrderManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CsvImportService {
    OrderManager orderManager;
    GarageSpotManager garageSpotManager;
    MasterManager masterManager;
    public CsvImportService(OrderManager orderManager, GarageSpotManager garageSpotManager, MasterManager masterManager) {
        this.orderManager = orderManager;
        this.garageSpotManager = garageSpotManager;
        this.masterManager = masterManager;
    }

    public boolean importMasters() throws  IOException, ImportException, CsvParsingException{
        MasterManager tempManager;

        try {
            tempManager = masterManager.cloneManager();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Критическая ошибка: менеджер не поддерживает клонирование");
        }
        try(BufferedReader reader = new BufferedReader(new FileReader("Task-3(6)/4/data/masters.csv"))) {
            String header = reader.readLine();
            if (header == null) {
                return false;
            }
            if (!header.equals("id,name,salary")) {
                throw new ImportException("Некорректный хедер в CSV: " + header);
            }
            String line;
            while ((line = reader.readLine()) != null) {
                long id;
                String name;
                BigDecimal salary;
                try {
                    String[] split = line.split(",");
                    if (split.length!=3){
                        throw new ImportException("Неверная структура строки: " + line);
                    }
                    id = Integer.parseInt(split[0]);
                    name = split[1];
                    salary = new BigDecimal(split[2]);
                }catch(Exception e){
                    throw new CsvParsingException("Ошибка в строке (line - " + line + ")");
                }
                if (tempManager.getMasterById(id) != null) {
                    Master master = tempManager.getMasterById(id);
                    master.setName(name);
                    master.setSalary(salary);
                } else {
                    tempManager.addMaster(id, name, salary);
                    Master.updateGlobalId(id);
                }
            }
        }
        masterManager.getMasters().clear();
        masterManager.getMasters().addAll(tempManager.getMasters());
        return true;
    }

    public boolean importGarageSpots() throws ImportException, IOException, CsvParsingException, IllegalGarageSpotSize {
        GarageSpotManager tempManager;

        try {
            tempManager = garageSpotManager.cloneManager();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Критическая ошибка: менеджер не поддерживает клонирование");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("Task-3(6)/4/data/garageSpots.csv"))) {
            String header = reader.readLine();
            if (header == null) {
                return false;
            }
            if (!header.equals("id,size,hasLift,hasPit")) {
                throw new ImportException("Некорректный хедер в CSV: " + header);
            }
            String line;
            while ((line = reader.readLine()) != null) {
                long id;
                double size;
                boolean hasLift;
                boolean hasPit;
                try {
                    String[] split = line.split(",");
                    if (split.length != 4) {
                        throw new ImportException("Неверная структура строки: " + line);
                    }
                    id = Long.parseLong(split[0]);
                    size = Double.parseDouble(split[1]);
                    hasLift = Boolean.parseBoolean(split[2]);
                    hasPit = Boolean.parseBoolean(split[3]);
                } catch(Exception e){
                    throw new CsvParsingException("Ошибка в строке (line - " + line + ")");
                }
                if (size < 8) {
                    throw new IllegalGarageSpotSize("Минимальный размер гаража - 8 (line - " + line + ")");
                } else {
                    if (tempManager.getGarageSpotById(id) != null) {
                        GarageSpot garageSpot = tempManager.getGarageSpotById(id);
                        garageSpot.setSize(size);
                        garageSpot.setHasLift(hasLift);
                        garageSpot.setHasPit(hasPit);
                    } else {
                        tempManager.addGarageSpot(id, size, hasLift, hasPit);
                        GarageSpot.updateGlobalId(id);
                    }
                }
            }
        }
        garageSpotManager.getGarageSpots().clear();
        garageSpotManager.getGarageSpots().addAll(tempManager.getGarageSpots());
        return true;
    }


    public boolean importOrders() throws IOException, ImportException, CsvParsingException {
        MasterManager masterCopy;
        GarageSpotManager garageCopy;
        OrderManager orderCopy;

        try {
            masterCopy = masterManager.cloneManager();
            garageCopy = garageSpotManager.cloneManager();
            orderCopy = orderManager.cloneManager(masterCopy, garageCopy);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Критическая ошибка: менеджер не поддерживает клонирование");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("Task-3(6)/4/data/orders.csv"))) {
            String header = reader.readLine();
            if (header == null) {
                return false;
            }
            if (!header.equals("id,description,masterId,garageSpotId,startTime,endTime,orderStatus,price")) {
                throw new ImportException("Некорректный хедер в CSV: " + header);
            }
            String line;

            while ((line = reader.readLine()) != null) {
                long id;
                String description;
                long masterId;
                long garageId;
                LocalDateTime startTime;
                LocalDateTime endTime;
                OrderStatus orderStatus;
                BigDecimal price;
                try {
                    String[] split = line.split(",");
                    if (split.length != 8) {
                        throw new ImportException("Неверная структура строки: " + line);
                    }
                    id = Long.parseLong(split[0]);
                    description = split[1];
                    masterId = Long.parseLong(split[2]);
                    garageId = Long.parseLong(split[3]);
                    startTime = LocalDateTime.parse(split[4]);
                    endTime = LocalDateTime.parse(split[5]);
                    orderStatus = OrderStatus.valueOf(split[6]);
                    price = BigDecimal.valueOf(Long.parseLong(split[7]));
                } catch(Exception e){
                        throw new CsvParsingException("Ошибка в строке (line - " + line + ")");
                    }

                //если такой ордер уже есть - меняем его поля
                if (orderCopy.getOrderById(id) != null) {
                    Order order = orderCopy.getOrderById(id);
                    order.setOrderStatus(orderStatus);
                    order.setPrice(price);
                    order.setDescription(description);
                    Master master = masterCopy.getMasterById(masterId);
                    GarageSpot garageSpot = garageCopy.getGarageSpotById(garageId);
                    if (master != null && garageSpot != null) {
                        order.getMaster().freeTimeSlot(order.getStartTime(), order.getEndTime());
                        order.getGarageSpot().freeTimeSlot(order.getStartTime(), order.getEndTime());
                        if (master.isAvailable(startTime, endTime) && garageSpot.isAvailable(startTime, endTime)) {
                            order.setGarageSpot(garageSpot);
                            order.setMaster(master);
                            order.setStartTime(startTime);
                            order.setEndTime(endTime);
                            master.addBusyTime(startTime, endTime);
                            garageSpot.addBusyTime(startTime, endTime);
                        }
                        //невозможно добавить по новому мастеру или гаражу
                        else {
                            order.getMaster().addBusyTime(order.getStartTime(), order.getEndTime());
                            order.getGarageSpot().addBusyTime(order.getStartTime(), order.getEndTime());
                        }
                    } else {
                        //невалидный мастер или гараж
                        throw new ImportException("Невалидный мастер или гараж (line - " + line + ")");
                    }
                } else {
                    if (masterCopy.getMasterById(masterId) != null && garageCopy.getGarageSpotById(garageId) != null) {
                        Master master = masterCopy.getMasterById(masterId);
                        GarageSpot garageSpot = garageCopy.getGarageSpotById(garageId);
                        if (master.isAvailable(startTime, endTime) && garageSpot.isAvailable(startTime, endTime)) {
                            orderCopy.addOrder(id, description, master, garageSpot, startTime, endTime, price);
                            Order.updateGlobalId(id);
                        } else {
                            //нельзя из за расписания мастера или гаража
                            throw new ImportException("Нельзя добавить из-за расписания мастера или гаража (line - " + line + ")");
                        }
                    } else {
                        //такого мастера или гаража нету, невозможно создать связь
                        throw new ImportException("Такого мастера или гаража нету, невозможно создать связь (line - " + line + ")");
                    }
                }
            }
        }
        masterManager.replaceData(masterCopy);
        garageSpotManager.replaceData(garageCopy);
        orderManager.replaceData(orderCopy);
        return true;
    }
}
