import model.GarageSpot;
import model.Master;
import model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AutoServiceRunner {
    public static void main(String[] args) {

        AutoService autoService = new AutoService();


        long garageSpot = autoService.addGarageSpot();



        System.out.println(autoService);
    }

}
