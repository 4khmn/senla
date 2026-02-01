package autoservice.ui.actions.general;

import autoservice.model.service.GeneralService;
import autoservice.model.exceptions.DBException;
import autoservice.model.service.GarageSpotService;
import autoservice.model.service.MasterService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Scanner;
@RequiredArgsConstructor
public class GetClosestDateAction implements IAction {

    private final GeneralService service;
    private final MasterService masterService;
    private final GarageSpotService garageSpotService;


    @Override
    public void execute() {
        if (masterService.getMastersCount() == 0 || garageSpotService.getGarageSpotsCount() == 0) {
            System.out.println("В автосервисе отсувствуют мастера или гаражные места");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите продолжительность заказа в часах: ");
        int duration = sc.nextInt();
        if (duration > 0) {
            try {
                LocalDateTime closestDate = service.getClosestDate(duration);
                System.out.println("Ближайшее свободное время: " + closestDate);
            } catch (DBException e) {
                System.out.println(e.getMessage());
            }  catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Длительность введена некорректно");
        }
    }
}
