package autoservice.model.io.imports;

import autoservice.model.exceptions.DBException;
import autoservice.model.repository.DBConnection;
import config.AppConfig;
import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.enums.OrderStatus;
import autoservice.model.exceptions.CsvParsingException;
import autoservice.model.exceptions.IllegalGarageSpotSize;
import autoservice.model.exceptions.ImportException;
import autoservice.model.service.GarageSpotService;
import autoservice.model.service.MasterService;
import autoservice.model.service.OrderService;
import config.annotation.Component;
import config.annotation.Inject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
@Slf4j
@Component
public class CsvImportService {
    private OrderService orderService;
    private GarageSpotService garageSpotService;
    private MasterService masterService;
    private final AppConfig appConfig;

    private final String garageSpotHeader = "id,size,hasLift,hasPit";
    private final String masterHeader = "id,name,salary";
    private final String orderHeader = "id,description,masterId,garageSpotId,startTime,endTime,orderStatus,price";

    @Inject
    private CsvImportService(OrderService orderService, GarageSpotService garageSpotService, MasterService masterService) {
        this.orderService = orderService;
        this.garageSpotService = garageSpotService;
        this.masterService = masterService;

        this.appConfig = new AppConfig();
    }

    public boolean importMasters() throws ImportException, CsvParsingException {
        log.info("Import masters started");
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false); // начало транзакции


            try (InputStream input = getClass().getClassLoader()
                    .getResourceAsStream("data/masters.csv")) {
                if (input == null) {
                    log.error("Import master file not found");
                    throw new RuntimeException("masters.csv не найден в resources");
                }


                try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                    String header = reader.readLine();
                    if (header == null) {
                        return false;
                    }
                    if (!header.equals(masterHeader)) {
                        log.error("Invalid header in master file");
                        throw new ImportException("Некорректный хедер в CSV: " + header);
                    }
                    String line;
                    while ((line = reader.readLine()) != null) {
                        long id;
                        String name;
                        BigDecimal salary;
                        try {
                            String[] split = line.split(",");
                            if (split.length != 3) {
                                log.error("Invalid structure of master file line: {}", line);
                                throw new ImportException("Неверная структура строки: " + line);
                            }
                            id = Integer.parseInt(split[0]);
                            name = split[1];
                            salary = new BigDecimal(split[2]);
                        } catch (Exception e) {
                            log.error("Invalid master file line: {}", line);
                            throw new CsvParsingException("Ошибка в строке (line - " + line + ")");
                        }
                        if (masterService.getMasterById(id) != null) {
                            Master master = masterService.getMasterById(id);
                            master.setName(name);
                            master.setSalary(salary);
                            masterService.update(master);
                        } else {
                            masterService.addMaster(name, salary);
                        }
                    }
                }
            }

            connection.commit();
            log.info("Import masters finished successfully");
            return true;

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DBException(ex.getMessage());
                }
            }
            log.error("Import masters failed", e);
            throw new ImportException("Импорт не выполнен: " + e.getMessage());

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new DBException(e.getMessage());
                }
            }
        }
    }

    public boolean importGarageSpots() throws ImportException, CsvParsingException, IllegalGarageSpotSize {
        log.info("Import garage spots started");
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false); // начало транзакции

            try (InputStream input = getClass().getClassLoader()
                    .getResourceAsStream("data/garageSpots.csv")) {
                if (input == null) {
                    log.error("Import garage spot file not found");
                    throw new RuntimeException("garageSpots.csv не найден в resources");
                }

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                    String header = reader.readLine();
                    if (header == null) {
                        return false;
                    }
                    if (!header.equals(garageSpotHeader)) {
                        log.error("Invalid header in garage spot file");
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
                                log.error("Invalid structure of garage spot file line: {}", line);
                                throw new ImportException("Неверная структура строки: " + line);
                            }
                            id = Long.parseLong(split[0]);
                            size = Double.parseDouble(split[1]);
                            hasLift = booleanParser(split[2]);
                            hasPit = booleanParser(split[3]);
                        } catch (Exception e) {
                            log.error("Invalid garage spot file line: {}", line);
                            throw new CsvParsingException("Ошибка в строке (line - " + line + ")\n" + e.getMessage());
                        }
                        if (size < 8) {
                            log.error("Invalid garage spot size in line: {}", line);
                            throw new IllegalGarageSpotSize("Минимальный размер гаража - 8 (line - " + line + ")");
                        } else {
                            if (garageSpotService.getGarageSpotById(id) != null) {
                                GarageSpot garageSpot = garageSpotService.getGarageSpotById(id);
                                garageSpot.setSize(size);
                                garageSpot.setHasLift(hasLift);
                                garageSpot.setHasPit(hasPit);
                                garageSpotService.update(garageSpot);
                            } else {
                                if (appConfig.isGarageSpotAllowToAddRemove()) {
                                    garageSpotService.addGarageSpot(size, hasLift, hasPit);
                                } else {
                                    log.error("Immpossible to add due properties file permission");
                                    throw new CsvParsingException("невозможно добавить из за properties (line - " + line + ")");
                                }
                            }
                        }
                    }
                }
            }
            connection.commit();
            log.info("Import garage spots finished successfully");
            return true;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DBException(ex.getMessage());
                }
            }
            log.error("Import garage spots failed", e);
            throw new ImportException("Импорт не выполнен: " + e.getMessage());

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new DBException(e.getMessage());
                }
            }
        }
    }


    public boolean importOrders() throws ImportException, CsvParsingException {
        log.info("Import orders started");
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false); // начало транзакции
            try (InputStream input = getClass().getClassLoader()
                    .getResourceAsStream("data/orders.csv")) {
                if (input == null) {
                    log.error("Import orders file not found");
                    throw new RuntimeException("orders.csv не найден в resources");
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                    String header = reader.readLine();
                    if (header == null) {
                        return false;
                    }
                    if (!header.equals(orderHeader)) {
                        log.error("Invalid header in orders file");
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
                                log.error("Invalid structure of orders file line: {}", line);
                                throw new ImportException("Неверная структура строки: " + line);
                            }
                            id = Long.parseLong(split[0]);
                            description = split[1];
                            masterId = Long.parseLong(split[2]);
                            garageId = Long.parseLong(split[3]);
                            startTime = LocalDateTime.parse(split[4]);
                            endTime = LocalDateTime.parse(split[5]);
                            orderStatus = OrderStatus.valueOf(split[6]);
                            price = new BigDecimal(split[7]);
                        } catch (Exception e) {
                            log.error("Invalid orders file line: {}", line);
                            throw new CsvParsingException("Ошибка в строке (line - " + line + ")\n" + e.getMessage());
                        }

                        //если такой ордер уже есть - меняем его поля
                        if (orderService.getOrderById(id) != null) {
                            Order order = orderService.getOrderById(id);
                            order.setOrderStatus(orderStatus);
                            order.setPrice(price);
                            order.setDescription(description);
                            Master master = masterService.getMasterById(masterId);
                            GarageSpot garageSpot = garageSpotService.getGarageSpotById(garageId);
                            if (master.getId() == order.getMasterId()) {
                                master = order.getMaster();
                            }
                            if (garageSpot.getId() == order.getGarageSpotId()) {
                                garageSpot = order.getGarageSpot();
                            }
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
                                    orderService.update(order);
                                } else {
                                    //невозможно добавить по новому мастеру или гаражу, возвращаем изначальное значение
                                    order.getMaster().addBusyTime(order.getStartTime(), order.getEndTime());
                                    order.getGarageSpot().addBusyTime(order.getStartTime(), order.getEndTime());
                                    log.error("Impossible to add due masters or garage spot schedule, line: {}", line);
                                    throw new ImportException("Нельзя изменить из-за расписания мастера или гаража (line - " + line + ")");
                                }
                            } else {
                                //невалидный мастер или гараж
                                log.error("Invalid master or garage spot, line: {}", line);
                                throw new ImportException("Невалидный мастер или гараж (line - " + line + ")");
                            }
                        } else {
                            //новый заказ
                            if (masterService.getMasterById(masterId) != null && garageSpotService.getGarageSpotById(garageId) != null) {
                                Master master = masterService.getMasterById(masterId);
                                GarageSpot garageSpot = garageSpotService.getGarageSpotById(garageId);
                                if (master.isAvailable(startTime, endTime) && garageSpot.isAvailable(startTime, endTime)) {
                                    orderService.addOrder(description, master, garageSpot, startTime, endTime, price);
                                } else {
                                    //нельзя из за расписания мастера или гаража
                                    log.error("Impossible to add due master or garage spot schedule, line: {}", line);
                                    throw new ImportException("Нельзя добавить из-за расписания мастера или гаража (line - " + line + ")");
                                }
                            } else {
                                //такого мастера или гаража нету, невозможно создать связь
                                log.error("Impossible to add due master or garage spot existence , line: {}", line);
                                throw new ImportException("Такого мастера или гаража нету, невозможно создать связь (line - " + line + ")");
                            }
                        }
                    }
                }
            }
            connection.commit();
            log.info("Import orders finished successfully");
            return true;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DBException(ex.getMessage());
                }
            }
            log.error("Import orders failed", e);
            throw new ImportException("Импорт не выполнен: " + e.getMessage());

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new DBException(e.getMessage());
                }
            }
        }
    }

    public boolean booleanParser(String value) {
        if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else {
            throw new CsvParsingException("Illegal boolean value: " + value);
        }
    }
}