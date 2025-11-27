package autoservice.ui.actions.garageSpots;

import autoservice.model.AutoService;
import autoservice.ui.actions.IAction;

import java.util.Scanner;

public class AddGarageSpotAction implements IAction {
    private final AutoService service;

    public AddGarageSpotAction(AutoService autoService) {
        this.service = autoService;
    }
    @Override
    public void execute() {


        Scanner sc = new Scanner(System.in);
        System.out.println("Введите размер места в метрах квадратных");
        double size;
        boolean hasLift;
        boolean hasPit;
        while(true) {
            size = sc.nextDouble();
            if (size<=8.00){
                System.out.println("Минимальный резмер места - 8");
                sc.next();
            }
            else{
                break;
            }
        }
        while(true) {
            System.out.println("есть ли подъемник?");
            try{
                hasLift = sc.nextBoolean();
                break;
            } catch (Exception e) {
                System.out.println("неверный ввод");
                sc.next();
            }
        }
        while(true) {
            System.out.println("есть ли яма?");
            try{
                hasPit = sc.nextBoolean();
                break;
            } catch (Exception e) {
                System.out.println("неверный ввод");
                sc.next();
            }
        }


        service.addGarageSpot(size, hasLift, hasPit);
        System.out.println("Гаражное место добавлено!");
        System.out.println("Всего доступных гаражных мест: " + service.getGarageSpotsCount());
    }
}
