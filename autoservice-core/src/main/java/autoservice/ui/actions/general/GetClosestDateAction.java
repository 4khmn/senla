package autoservice.ui.actions.general;

import autoservice.model.AutoService;
import autoservice.model.exceptions.DBException;
import autoservice.ui.actions.IAction;

import java.time.LocalDateTime;
import java.util.Scanner;

public class GetClosestDateAction implements IAction {

    private final AutoService service;

    public GetClosestDateAction(AutoService autoService) {
        this.service = autoService;
    }
    @Override
    public void execute() {
        if (service.getMastersCount() == 0 || service.getGarageSpotsCount() == 0) {
            System.out.println("В автосервисе отсувствуют мастера или гаражные места");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите продолжительность заказа в часах: ");
        int duration = sc.nextInt();
        if (duration>0){
            try {
                LocalDateTime closestDate = service.getClosestDate(duration);
                System.out.println("Ближайшее свобожное время: " + closestDate);
            }
            catch(DBException e){
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("Длительность введена некорректно");
        }
    }
}
