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

        long master1 = autoService.addMaster("bob", new BigDecimal(150000));
        long master2 = autoService.addMaster("bob", new BigDecimal(150000));
        long master3 = autoService.addMaster("bob", new BigDecimal(150000));
        System.out.println(autoService);
        autoService.addOrder("beebeb", 3, new BigDecimal(170));
        autoService.addOrder("beebeb", 1, new BigDecimal(100));
        autoService.addOrder("beebeb", 3, new BigDecimal(160));
        System.out.println("-----------мастера-------------");
        System.out.println(autoService.getMasterById(master1).getCalendar());
        System.out.println(autoService.getMasterById(master2).getCalendar());
        System.out.println(autoService.getMasterById(master3).getCalendar());
        System.out.println("-----------гаражи-------------");
        System.out.println(autoService.getGarageSpotById(garageSpot1).getCalendar());
        System.out.println(autoService.getGarageSpotById(garageSpot2).getCalendar());

        System.out.println(autoService.ordersSort(4));

        System.out.println(autoService.getClosestDate(1));

    }

}
