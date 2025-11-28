package autoservice.model.io;

import autoservice.model.entities.GarageSpot;
import autoservice.model.entities.Master;
import autoservice.model.entities.Order;
import autoservice.model.enums.EntityType;
import autoservice.model.manager.GarageSpotManager;
import autoservice.model.manager.MasterManager;
import autoservice.model.manager.OrderManager;

import java.io.*;

public class CsvExportService {
    OrderManager orderManager;
    GarageSpotManager garageSpotManager;
    MasterManager masterManager;

    private final String orderHeader = "id,description,masterId,garageSpotId,startTime,endTime,orderStatus,price\n";
    private final String masterHeader = "id,name,salary\n";
    private final String garageSpotHeader = "id,size,hasLift,hasPit\n";
    public CsvExportService(OrderManager orderManager, GarageSpotManager garageSpotManager, MasterManager masterManager) {
        this.orderManager = orderManager;
        this.garageSpotManager = garageSpotManager;
        this.masterManager = masterManager;
    }


    public void export(EntityType entity) throws IOException {
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdirs();
        String fileName;
        if (entity.equals(EntityType.ORDER)){
            fileName = "orders.csv";
        }
        else if (entity.equals(EntityType.MASTER)){
            fileName = "masters.csv";
        }
        else if (entity.equals(EntityType.GARAGESPOT)){
            fileName = "garageSpots.csv";
        }
        else{
            throw new IllegalArgumentException("Unknown entity: " + entity);
        }

        File file = new File(dataDir, fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (entity.equals(EntityType.ORDER)) {

                writer.write(orderHeader);
                for (Order order : orderManager.getOrders()) {
                    writer.write(formatOrder(order));
                    writer.newLine();

                }
            } else if (entity.equals(EntityType.MASTER)) {

                writer.write(masterHeader);
                for (Master master : masterManager.getMasters()) {
                    writer.write(formatMaster(master));
                    writer.newLine();
                }

            } else if (entity.equals(EntityType.GARAGESPOT)) {

                writer.write(garageSpotHeader);
                for (GarageSpot garageSpot : garageSpotManager.getGarageSpots()) {
                    writer.write(formatGarageSpot(garageSpot));
                    writer.newLine();

                }
            }
        }
    }
    public void exportOrders() throws IOException {
        // Поля не должны содержать ','
        export(EntityType.ORDER);
    }
    public void exportMasters() throws IOException {
        export(EntityType.MASTER);
    }
    public void exportGarageSpots() throws IOException {
       export(EntityType.GARAGESPOT);
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