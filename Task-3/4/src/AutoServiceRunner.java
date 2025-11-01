import model.GarageSpot;
import model.Master;
import model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AutoServiceRunner {
    public static void main(String[] args) throws Exception {
        AutoService autoService = new AutoService();


        long garageSpot = autoService.addGarageSpot();

        long master = autoService.addMaster("bob", new BigDecimal(150000));
        System.out.println(autoService);
        autoService.addOrder("beebeb", 3, new BigDecimal(150));
        System.out.println("order 1 was added");
        autoService.addOrder("beebeb", 3, new BigDecimal(150));
        System.out.println("order 2 was added");
        autoService.addOrder("beebeb", 3, new BigDecimal(150));
        System.out.println("order 3 was added");
        autoService.addOrder("beebeb", 3, new BigDecimal(150));
        System.out.println("order 4 was added");
        autoService.shiftOrder(1, 1);
        for (int i=1; i<5; i++){
            System.out.println(autoService.getOrderById(i));
        }
        System.out.println("asdasddas");
        System.out.println(autoService.getMasterById(1).getCalendar());





        System.out.println(autoService);
    }

}
