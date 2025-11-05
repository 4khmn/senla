import model.GarageSpot;
import model.Master;
import model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AutoServiceRunner {
    public static void main(String[] args) throws Exception {
        AutoService autoService = new AutoService();


        long garageSpot1 = autoService.addGarageSpot();
        long garageSpot2 = autoService.addGarageSpot();
        long garageSpot3 = autoService.addGarageSpot();
        long garageSpot4 = autoService.addGarageSpot();
        long garageSpot5 = autoService.addGarageSpot();

        long master1 = autoService.addMaster("bob", new BigDecimal(150000));
        long master2 = autoService.addMaster("bob", new BigDecimal(150000));
        long master3 = autoService.addMaster("bob", new BigDecimal(150000));

    }

}
