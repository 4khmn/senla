import manager.GarageSpotManager;
import manager.MasterManager;
import manager.OrderManager;
import model.GarageSpot;
import model.Master;
import model.Order;
import result.GarageSpotResult;
import result.MasterResult;
import result.OrderResult;

import java.util.ArrayList;
import java.util.List;

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
