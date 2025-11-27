package autoservice.model.io;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.manager.GarageSpotManager;
import autoservice.model.manager.MasterManager;
import autoservice.model.manager.OrderManager;

import java.io.*;

public class CsvExportService {
    OrderManager orderManager;
    GarageSpotManager garageSpotManager;
    MasterManager masterManager;
    public CsvExportService(OrderManager orderManager, GarageSpotManager garageSpotManager, MasterManager masterManager) {
        this.orderManager = orderManager;
        this.garageSpotManager = garageSpotManager;
        this.masterManager = masterManager;
    }



    public void exportOrders() throws IOException {
        // Поля не должны содержать ','
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Task-3(6)/4/data/orders.csv"))) {
            writer.write("id,description,masterId,garageSpotId,startTime,endTime,orderStatus,price\n");
            for (Order order : orderManager.getOrders()) {
                writer.write(formatOrder(order));
                writer.newLine();
            }
        }
    }
    public void exportMasters() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Task-3(6)/4/data/masters.csv"))) {
            writer.write("id,name,salary\n");

            for (Master master : masterManager.getMasters()) {
                writer.write(formatMaster(master));
                writer.newLine();
            }
        }
    }
    public void exportGarageSpots() throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("Task-3(6)/4/data/garageSpots.csv"))){
            writer.write("id,size,hasLift,hasPit\n");

            for (GarageSpot garageSpot: garageSpotManager.getGarageSpots()){
                writer.write(formatGarageSpot(garageSpot));
                writer.newLine();
            }
        }
    }

    private String formatOrder(Order order) {
        return String.join(",",
                String.valueOf(order.getId()),
                order.getDescription(),
                String.valueOf(order.getMaster().getId()),
                String.valueOf(order.getGarageSpot().getId()),
                String.valueOf(order.getStartTime()),
                String.valueOf(order.getEndTime()),
                String.valueOf(order.getOrderStatus()),
                String.valueOf(order.getPrice())
        );
    }
    private String formatMaster(Master master) {
        return String.join(",",
                String.valueOf(master.getId()),
                master.getName(),
                String.valueOf(master.getSalary())
        );
    }
    private String formatGarageSpot(GarageSpot garageSpot){
        return String.join(",",
                String.valueOf(garageSpot.getId()),
                String.valueOf(garageSpot.getSize()),
                String.valueOf(garageSpot.isHasLift()),
                String.valueOf(garageSpot.isHasPit())
        );
    }
}