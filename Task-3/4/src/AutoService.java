import manager.GarageSpotManager;
import manager.MasterManager;
import manager.OrderManager;
import model.GarageSpot;
import model.Master;
import model.Order;
import model.OrderStatus;
import result.GarageSpotResult;
import result.MasterResult;
import result.OrderResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public LocalDateTime getClosestDate(int duration){ // (duration in hours)

    }

    //model.Master
    public MasterResult addMaster(Master master){
        return masterManager.addMaster(master);
    }

    public MasterResult deleteMaster(Master master){
        return masterManager.deleteMaster(master);
    }
    //model.GarageSpot
    public GarageSpotResult addGarageSpot(GarageSpot garageSpot){
        return garageManager.addGarageSpot(garageSpot);
    }

    public GarageSpotResult deleteGarageSpot(GarageSpot garageSpot){
        return garageManager.deleteGarageSpot(garageSpot);
    }
    //model.Order
    public OrderResult addOrder(Order order){
        return orderManager.addOrder(order);
    }

    public OrderResult deleteOrder(Order order){
        return orderManager.deleteOrder(order);
    }

    public OrderResult closeOrder(Order order){
        return orderManager.closeOrder(order);
    }

    public OrderResult cancelOrder(Order order){
        return orderManager.cancelOrder(order);
    }

    public OrderResult shiftOrder(Order order, int hours){
        return orderManager.shiftOrder(order, hours);
    }

}
