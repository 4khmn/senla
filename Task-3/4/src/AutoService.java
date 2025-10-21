import java.util.ArrayList;
import java.util.List;

public class AutoService {
    private List<Master> masters;
    private List<Order> orders;
    private List<GarageSpot> garageSpots;

    public AutoService() {
        masters = new ArrayList<>();
        orders = new ArrayList<>();
        garageSpots = new ArrayList<>();
    }
    //Master
    public void addMaster(Master master){
        if (!masters.contains(master)){
            masters.add(master);
        }
        else{
            System.out.println("This master is already exist in System");
        }
    }

    public void deleteMaster(Master master){
        if (masters.contains(master)){
            masters.remove(master);
        }
        else{
            System.out.println("There is no such master in System");
        }
    }
    //GarageSpot
    public void addGarageSpot(GarageSpot garageSpot){
        if (!garageSpots.contains(garageSpot)){
            garageSpots.add(garageSpot);
        }
        else{
            System.out.println("This garage spot is already exist in System");
        }
    }

    public void deleteGarageSpot(GarageSpot garageSpot){
        if (garageSpots.contains(garageSpot)){
            garageSpots.remove(garageSpot);
        }
        else{
            System.out.println("There is no such garage spot in System");
        }
    }
    //Order
    public void addOrder(Order order){
        if (!orders.contains(order)){
            orders.add(order);
        }
        else{
            System.out.println("This order is already exist in System");
        }
    }

    public void deleteOrder(Order order){
        if (orders.contains(order)){
            orders.remove(order);
        }
        else{
            System.out.println("There is no such order in System");
        }
    }

    public void closeOrder(Order order){
        if (order.getOrderStatus()==OrderStatus.CLOSED || order.getOrderStatus()==OrderStatus.CANCELLED){
            System.out.println("This order is already closed or canceled");
        }
        else{
            order.setOrderStatus(OrderStatus.CLOSED);
        }
    }

    public void cancelOrder(Order order){
        if (order.getOrderStatus()==OrderStatus.CANCELLED || order.getOrderStatus()==OrderStatus.CLOSED){
            System.out.println("This order is already canceled or closed");
        }
        else{
            order.setOrderStatus(OrderStatus.CANCELLED);
        }
    }

    public void shiftOrder(Order order, int hours){
        order.setStartTime(order.getStartTime().plusHours(hours));
        order.setEndTime(order.getEndTime().plusHours(hours));
        int index = orders.indexOf(order);
        for (int i=index+1; i<orders.size(); i++){
            Order current = orders.get(i);
            current.setStartTime(current.getStartTime().plusHours(hours));
            current.setEndTime(current.getEndTime().plusHours(hours));
        }
    }

}
