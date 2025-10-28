import model.GarageSpot;
import model.Master;
import model.Order;
import result.GarageSpotResult;
import result.MasterResult;
import result.OrderResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AutoServiceRunner {
    public static void main(String[] args) {
        Master master1 = new Master("Bob", new BigDecimal(120_000));
        Master master2 = new Master("Alex", new BigDecimal(115_000));
        GarageSpot garageSpot1 = new GarageSpot();
        GarageSpot garageSpot2 = new GarageSpot();
        GarageSpot garageSpot3 = new GarageSpot();
        Order order1 = new Order("tire fix", master1, garageSpot1, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        Order order2 = new Order("check engine", master2, garageSpot1, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3));
        Order order3 = new Order("new oil", master1, garageSpot3, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3));
        Order order4 = new Order("new color", master1, garageSpot3, LocalDateTime.now().plusHours(6), LocalDateTime.now().plusHours(8));

        System.out.println(order1);
        System.out.println(master1);
        System.out.println(master2);

        AutoService autoService = new AutoService();


        GarageSpotResult resultGarageSpot = autoService.addGarageSpot(garageSpot1);
        printResultAboutGarageSpot(resultGarageSpot, garageSpot1);

        resultGarageSpot = autoService.addGarageSpot(garageSpot2);
        printResultAboutGarageSpot(resultGarageSpot, garageSpot2);

        resultGarageSpot =autoService.addGarageSpot(garageSpot3);
        printResultAboutGarageSpot(resultGarageSpot, garageSpot3);

        resultGarageSpot = autoService.addGarageSpot(garageSpot3);
        printResultAboutGarageSpot(resultGarageSpot, garageSpot3);




        MasterResult resultMaster = autoService.addMaster(master1);
        printResultAboutMaster(resultMaster, master1);

        resultMaster = autoService.addMaster(master2);
        printResultAboutMaster(resultMaster, master2);

        resultMaster = autoService.addMaster(master2);
        printResultAboutMaster(resultMaster, master2);



        OrderResult resultOrder = autoService.addOrder(order1);
        printResultAboutOrder(resultOrder, order1);

        resultOrder = autoService.addOrder(order1);
        printResultAboutOrder(resultOrder, order1);

        resultOrder = autoService.addOrder(order2);
        printResultAboutOrder(resultOrder, order2);

        resultOrder = autoService.addOrder(order3);
        printResultAboutOrder(resultOrder, order3);

        resultOrder = autoService.deleteOrder(order1);
        printResultAboutOrder(resultOrder, order1);
        resultOrder = autoService.shiftOrder(order2, 3);
        printResultAboutOrder(resultOrder, order2);


        resultGarageSpot = autoService.deleteGarageSpot(garageSpot2);
        printResultAboutGarageSpot(resultGarageSpot, garageSpot2);

        resultOrder = autoService.cancelOrder(order4);
        printResultAboutOrder(resultOrder, order4);

        System.out.println(autoService);

    }

    public static void printResultAboutOrder(OrderResult result, Order order){
        switch(result){
            case SUCCESS_ADDED -> System.out.println("Order #" + order.getId() + " was successfully added");
            case SUCCESS_DELETED -> System.out.println("Order #" + order.getId() + " was successfully deleted");
            case SUCCESS_CLOSED -> System.out.println("Order #" + order.getId() + " was successfully closed");
            case SUCCESS_CANCELLED -> System.out.println("Order #" + order.getId() + " was successfully calcelled");
            case SUCCESS_SHIFTED -> System.out.println("Order #" + order.getId() + " was successfully shifted");
            case MASTER_BUSY -> System.out.println("Master is busy at this time {order #" + order.getId() + "}");
            case SPOT_OCCUPIED -> System.out.println("Garage spot is occupied at this time {order #" + order.getId() + "}");
            case MASTER_BUSY_AND_SPOT_OCCUPIED -> System.out.println("Master and garage spot are not available at this time {order #" + order.getId() + "}");
            case ALREADY_EXISTS -> System.out.println("This order is already exist in System {order #" + order.getId() + "}");
            case ALREADY_CLOSED_OR_CANCELLED -> System.out.println("This order is already closed or canceled {order #" + order.getId() + "}");
            case NOT_FOUND -> System.out.println("There is no such order in System {order #" + order.getId() + "}");
        }
    }

    public static void printResultAboutMaster(MasterResult result, Master master){
        switch(result){
            case SUCCESS_ADDED -> System.out.println("Master #" + master.getId() + " was successfully added");
            case SUCCESS_REMOVED -> System.out.println("Master #" + master.getId() + " was successfully removed");

            case ALREADY_EXISTS -> System.out.println("This master is already exist in System {master #" + master.getId() + "}");
            case NOT_FOUND -> System.out.println("There is no such master in System {master #" + master.getId() + "}");
        }
    }
    public static void printResultAboutGarageSpot(GarageSpotResult result, GarageSpot garageSpot ){
        switch(result){
            case SUCCESS_ADDED -> System.out.println("garageSpot #" + garageSpot.getId() + " was successfully added");
            case SUCCESS_REMOVED -> System.out.println("garageSpot #" + garageSpot.getId() + " was successfully removed");
            case ALREADY_EXISTS -> System.out.println("This garage spot is already exist in System {garageSpot #" + garageSpot.getId() + "}");
            case NOT_FOUND -> System.out.println("There is no such garage spot in System {garageSpot #" + garageSpot.getId() + "}");
        }
    }
}
