package autoservice.ui.actions.garageSpots;

import autoservice.model.exceptions.DBException;
import autoservice.model.service.GarageSpotService;
import autoservice.ui.actions.IAction;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;
@RequiredArgsConstructor
public class AddGarageSpotAction implements IAction {
    private final GarageSpotService garageSpotService;

    @Override
    public void execute() {

        Scanner sc = new Scanner(System.in);
        double size;
        boolean hasLift;
        boolean hasPit;
        while (true) {
            System.out.println("Введите размер места в метрах квадратных");
            size = sc.nextDouble();
            if (size < 8.00) {
                System.out.println("Минимальный резмер места - 8");
                sc.nextLine();
            } else {
                break;
            }
        } while (true) {
            System.out.println("есть ли подъемник?");
            try {
                hasLift = sc.nextBoolean();
                break;
            } catch (Exception e) {
                System.out.println("неверный ввод");
                sc.next();
            }
        }
        while (true) {
            System.out.println("есть ли яма?");
            try {
                hasPit = sc.nextBoolean();
                break;
            } catch (Exception e) {
                System.out.println("неверный ввод");
                sc.next();
            }
        }

        try {
            garageSpotService.addGarageSpot(size, hasLift, hasPit);
        } catch (DBException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Гаражное место добавлено!");
        System.out.println("Всего доступных гаражных мест: " + garageSpotService.getGarageSpotsCount());
    }
}
